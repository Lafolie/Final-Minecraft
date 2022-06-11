package lafolie.fmc.core.mixin;

import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import lafolie.fmc.core.FinalMinecraft;
// import lafolie.fmc.core.Mod;
import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;
import lafolie.fmc.core.elements.ElementalEntity;
import lafolie.fmc.core.elements.ElementalObject;
import lafolie.fmc.core.elements.WeakResistTable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
	// @Shadow
	// public World world;
	// @Inject(at = @At("TAIL"), method = "<init>")
	// public void Constructor(EntityType<? extends LivingEntity> entityType, World world, CallbackInfo info)
	// {
		
	// }

	private DamageSource source;

	@Inject(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	private void damageMixin(DamageSource source, float amount, CallbackInfoReturnable info)
	{
		this.source = source;
	}


	@ModifyVariable(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	private float modifyDamage(float amount)
	{
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
		Entity attacker = (Entity)source.getAttacker();
		ElementalObject self = (ElementalObject)this;
		self.getComponent();

		if(attacker instanceof LivingEntity)
		{
			FinalMinecraft.log.info("ATTACK");
			// EW!
			ElementalObject attackedWith = (ElementalObject)(Object)(((LivingEntity)attacker).getMainHandStack());
			// we use resistance to determine whether an item is elemental
			Map<ElementalAspect, Integer> elements = attackedWith.getElementalAffinities(ElementalAttribute.RESISTANCE);
			for(Map.Entry<ElementalAspect, Integer> entry : elements.entrySet())
			{
				
			}
		}
		// Map<ElementalAspect, Integer> attackerElements
		
		return amount;
	}


	// unused as of yet
	private float adjustDamageAttackType(DamageSource source, float amount)
	{
		return amount;
	}
}
