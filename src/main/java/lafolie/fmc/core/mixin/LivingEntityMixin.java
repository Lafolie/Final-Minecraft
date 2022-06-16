package lafolie.fmc.core.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;
import lafolie.fmc.core.elements.ElementalObject;
import lafolie.fmc.core.entity.DamageNumbers;
import lafolie.fmc.core.internal.network.HealthModifiedPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements DamageNumbers
{
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
		//TODO Auto-generated constructor stub
	}

	@Shadow
	@Final
	public abstract float getMaxHealth();

	@Shadow
    public abstract void heal(float amount);

	// @Shadow
	// public World world;

	// @Shadow
	// public World world;
	// @Inject(at = @At("TAIL"), method = "<init>")
	// public void Constructor(EntityType<? extends LivingEntity> entityType, World world, CallbackInfo info)
	// {
		
	// }

	private DamageSource source;
	private float modifiedDamage = 1;
	private ElementalAttribute lastAttributeused;

	// ------------------------------------------------------------------------
	// Damage Algorithm & Related
	// ------------------------------------------------------------------------

	@Override
	public ElementalAttribute getLastAttributeUsed()
	{
		return lastAttributeused;
	}

	@Inject(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	private void captureDamageSource(DamageSource source, float amount, CallbackInfoReturnable info)
	{
		// FinalMinecraft.log.info("First inject");
		this.source = source;
	}


	@ModifyVariable(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	private float modifyDamage(float amount)
	{
		if (this.world.isClient)
		{
			source = null;
			return amount;
		}

		if(FinalMinecraft.getConfig().enableElements)
		{
			adjustDamageElemental(source, amount);
		}
		adjustDamageAttackType(source, amount);
		source = null; // should probably do this so that it can be GC'd
		return amount;
	}

	private float adjustDamageElemental(DamageSource source, float amount)
	{

		// FinalMinecraft.log.info("Second inject");
		Entity attacker = source.getAttacker();
		ElementalObject self = (ElementalObject)this;
		self.getComponent();
		ElementalAttribute modifierAttribute = ElementalAttribute.WEAKNESS;
		ElementalAspect modifierElement = ElementalAspect.NONE;
		int modifierAmount = 0;

		if(attacker != null && attacker instanceof LivingEntity)
		{
			// FinalMinecraft.log.info("ATTACK");
			// FinalMinecraft.log.info("Original amount {}", amount);

			// Apparently CardinalComponents won't attack anything to Air
			ItemStack attackedWithStack = (((LivingEntity)attacker).getMainHandStack());
			if(attackedWithStack.getItem() == Items.AIR)
			{
				lastAttributeused = null;
				return amount;
			}

			ElementalObject attackedWith = (ElementalObject)(Object)attackedWithStack;

			// we use resistance to determine whether an item is elemental
			Map<ElementalAspect, Integer> elements = attackedWith.getElementalAffinities(ElementalAttribute.RESISTANCE);

			// find the most appropriate Element/Attribute combination to use
			for(Map.Entry<ElementalAspect, Integer> entry : elements.entrySet())
			{
				ElementalAttribute potentialAttribute = self.getAttributeForDamage(entry.getKey());
				// FinalMinecraft.log.info("\tPotential attribute {}", potentialAttribute.toString());
				// FinalMinecraft.log.info("\tPotential element {}", entry.getKey().toString());

				if(potentialAttribute.ordinal() >= modifierAttribute.ordinal())
				{
					// FinalMinecraft.log.info("\tPOTENTIAL SET");

					modifierAttribute = potentialAttribute;
					modifierElement = entry.getKey();
				}
			}

			lastAttributeused = modifierAttribute;
			switch(modifierAttribute)
			{
				case WEAKNESS:
				case RESISTANCE:
					modifierAmount = self.getWeakResistAffinity(modifierElement);
					// FinalMinecraft.log.info("WeakResist amount {}", modifierAmount);

					float modifier = FinalMinecraft.getConfig().defaultWeakResistAmount * modifierAmount;
					// FinalMinecraft.log.info("Modifier amount {}", modifier);

					amount *= modifier > 0 ? 1 - modifier : 1 + Math.abs(modifier);

					// needs to be set since the attribute being used may have changed just now
					if(modifier == 0)
					{
						lastAttributeused = null;
					}
					else
					{
						lastAttributeused = modifier > 0 ? ElementalAttribute.RESISTANCE : ElementalAttribute.WEAKNESS;
					}
					break;

				case IMMUNITY:
					amount = 0;
					break;

				case ABSORBTION:
					heal(amount);
					amount = 0;
					break;

				case FATAL:
					amount = getMaxHealth();
					break;

				case REVIVE:
					amount = 0;
					heal(getMaxHealth());
					//heal
					break;

				
			}
			// FinalMinecraft.log.info("Using element {}", modifierElement.toString());
			// FinalMinecraft.log.info("Using attribute {}", modifierAttribute.toString());
			// FinalMinecraft.log.info("Modified amount {}", amount);

		}
		// Map<ElementalAspect, Integer> attackerElements
		modifiedDamage = amount;
		return amount;
	}

	@Inject(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", cancellable = true)
	private void cancelDamage(DamageSource source, float amount, CallbackInfoReturnable info)
	{
		// FinalMinecraft.log.info("Third inject");
		if(modifiedDamage <= 0)
		{
			// FinalMinecraft.log.info("Damage was zero, cancelling");
			info.setReturnValue(false);
		}
		modifiedDamage = 1;
	}
	
	// unused as of yet
	private float adjustDamageAttackType(DamageSource source, float amount)
	{
		return amount;
	}

	// ------------------------------------------------------------------------
	// Damage Numbers
	// ------------------------------------------------------------------------
	@Inject(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setHealth(F)V", shift = At.Shift.AFTER))
	private void applyDamageNumbers(DamageSource source, float amount, CallbackInfo info)
	{
		sendHealthModifiedPacket(amount, getLastAttributeUsed());
	}

	@Inject(at = @At("TAIL"), method="heal(F)V")
	private void healNumbers(float amount, CallbackInfo info)
	{
		sendHealthModifiedPacket(amount, ElementalAttribute.ABSORBTION);
	}

}