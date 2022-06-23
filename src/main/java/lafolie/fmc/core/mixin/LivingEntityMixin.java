package lafolie.fmc.core.mixin;

import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import lafolie.fmc.core.FMCItems;
import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.chrono.DateTime;
import lafolie.fmc.core.config.FMCConfig;
import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;
import lafolie.fmc.core.elements.ElementalObject;
import lafolie.fmc.core.entity.DamageNumbers;
import lafolie.fmc.core.zodiac.BirthsignEntity;
import lafolie.fmc.core.zodiac.ZodiacSign;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements DamageNumbers
{
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
		//Auto-generated constructor stub
	}

	@Shadow
	@Final
	public abstract float getMaxHealth();

	@Shadow
	public abstract void heal(float amount);

	@Shadow
	protected abstract boolean shouldDropLoot();


	// @Inject(at = @At("TAIL"), method = "<init>")
	// public void Constructor(EntityType<? extends LivingEntity> entityType, World world, CallbackInfo info)
	// {
		
	// }

	private DamageSource source;
	private float modifiedDamage = 1;
	private ElementalAttribute lastAttributeused;

	// ------------------------------------------------------------------------
	// Equipment
	// ------------------------------------------------------------------------
	
	@Inject(at = @At("HEAD"), method = "onEquipStack(Lnet/minecraft/item/ItemStack;)V")
	private void euipmentChanged(ItemStack stack, CallbackInfo info)
	{
		// FinalMinecraft.log.info("Itemstack {}", stack.toString());
	}
	
	// ------------------------------------------------------------------------
	// Death
	// ------------------------------------------------------------------------

	@Inject(at = @At("TAIL"), method = "drop(Lnet/minecraft/entity/damage/DamageSource;)V")
	private void onDrop(DamageSource source, CallbackInfo info)
	{
		if(!(shouldDropLoot() && this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)))
		{
			return;
		}

		DateTime today = new DateTime(world.getTimeOfDay());
		BirthsignEntity selfSign = (BirthsignEntity)this;
		boolean isAnimal = (Entity)this instanceof AnimalEntity;
		// pseudo-birthday check ()
		if(!((Entity)this instanceof PlayerEntity || isAnimal) && selfSign.getZodiacSign() == today.getZodiacSign() && selfSign.getElementalAspect() == today.getElementalAspect())
		{
			world.spawnEntity(new ItemEntity(world, getX(), getY(), getZ(), new ItemStack(FMCItems.CRYSTAL_SHARD, today.getDayOfTheMonth() % 3 + 1)));
			FinalMinecraft.log.info("BIRTHDAY SHARDS!!!");
			return;
		}

		int chance = random.nextInt(103);
		if(lastAttributeused != null)
		{
			switch(lastAttributeused)
			{
				case WEAKNESS:
					chance += 8;
					break;

				case RESISTANCE:
					chance += 12;
					break;

				default:
					break;
			}
		}

		int bonus = 0;
		if(source instanceof EntityDamageSource)
		{
			Entity attacker = source.getAttacker();
			if(attacker instanceof BirthsignEntity)
			{
				BirthsignEntity attackerSign = (BirthsignEntity)attacker;
				bonus -= attackerSign.getElementalAspect().getWeakTo() == selfSign.getElementalAspect() ? 15 : 1;
				bonus += attackerSign.getElementalAspect().getStrongTo() == selfSign.getElementalAspect() ? 15 : 1;
				bonus += today.getZodiacSign() == attackerSign.getZodiacSign() ? 6 : 0;
				bonus += today.getElementalAspect() == attackerSign.getElementalAspect() ? 6 : 0;
				for(ItemStack stack : attacker.getItemsHand())
				{
					bonus += stack.hasEnchantments() ? 3 : 0;
					bonus += EnchantmentHelper.getLevel(Enchantments.FORTUNE, stack) * 2;
					bonus += EnchantmentHelper.getLevel(Enchantments.LOOTING, stack) * 2;
					bonus += EnchantmentHelper.getLevel(Enchantments.LUCK_OF_THE_SEA, stack) * 4; // hell yeah fishing rods
					bonus -= EnchantmentHelper.getLevel(Enchantments.BINDING_CURSE, stack) * 20;
					bonus -= EnchantmentHelper.getLevel(Enchantments.VANISHING_CURSE, stack) * 20;
					bonus -= EnchantmentHelper.getLevel(Enchantments.MENDING, stack) * 6;
					bonus -= stack.getMaxCount() > 1 ? 2 : 0;
				}
				
				bonus += attacker instanceof PlayerEntity ? 0 : 20;
				bonus += MathHelper.floor(Math.max(0f, (20 - ((LivingEntity)attacker).getHealth()) / 2));

				float alignment = attackerSign.getFullCompatibility(attackerSign);
				bonus = MathHelper.floor(bonus * alignment);
			}
		}

		FinalMinecraft.log.info("Chance: {}", chance);
		FinalMinecraft.log.info("Bonus: {}", bonus);
		bonus *= isAnimal ? 0.5f : 1;
		chance += bonus;

		// crystal shard drops
		if(chance > 100)
		{
			world.spawnEntity(new ItemEntity(world, getX(), getY(), getZ(), new ItemStack(FMCItems.CRYSTAL_SHARD, (chance - 100) % 3 + 1)));
		}
	}

	// ------------------------------------------------------------------------
	// Damage Algorithm & Related
	// ------------------------------------------------------------------------

	@Override
	public ElementalAttribute getLastAttributeUsed()
	{
		return lastAttributeused;
	}

	@Inject(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	private void captureDamageSource(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info)
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
		FMCConfig config = FinalMinecraft.getConfig();
		if(config.enableElements)
		{
			amount = adjustDamageElemental(source, amount);
		}

		if(config.combatEnableBirthsign)
		{
			amount = adjustDamageBirthsign(source, amount);
		}

		adjustDamageAttackType(source, amount);
		
		// handle healing
		if(amount < 0 )
		{
			heal(amount);
			amount = 0;
		}
		
		modifiedDamage = amount;
		source = null; // should probably do this so that it can be GC'd
		return amount;
	}

	private float adjustDamageBirthsign(DamageSource source, float amount)
	{
		Entity attacker = source.getAttacker();
		if(this instanceof BirthsignEntity && attacker != null && attacker instanceof BirthsignEntity)
		{
			amount *= ((BirthsignEntity)this).getFullCompatibility((BirthsignEntity)attacker);
		}
		return amount;
	}

	private float adjustDamageElemental(DamageSource source, float amount)
	{
		// FinalMinecraft.log.info("Second inject");
		Entity attacker = source.getAttacker();
		ElementalObject self = (ElementalObject)this;
		ElementalAttribute modifierAttribute = ElementalAttribute.WEAKNESS;
		ElementalAspect modifierElement = ElementalAspect.NONE;
		int modifierAmount = 0;

		if(attacker != null && attacker instanceof LivingEntity)
		{
			// FinalMinecraft.log.info("ATTACK");
			// FinalMinecraft.log.info("Original amount {}", amount);

			// Apparently CardinalComponents won't attach anything to AIR
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

				// when elemental boosts are implemented this will need to change
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
					amount *= -1;
					break;

				case FATAL:
					amount = getMaxHealth();
					break;

				case REVIVE:
					amount = -getMaxHealth();
					break;
			}
			// FinalMinecraft.log.info("Using element {}", modifierElement.toString());
			// FinalMinecraft.log.info("Using attribute {}", modifierAttribute.toString());
			// FinalMinecraft.log.info("Modified amount {}", amount);

		}
		return amount;
	}

	// Called check that the amount set by adjustDamageElemental is non-zero
	@Inject(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", cancellable = true)
	private void cancelDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info)
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
		if(amount > 0)
		{
			sendHealthModifiedPacket(amount, ElementalAttribute.ABSORBTION);
		}
	}

}