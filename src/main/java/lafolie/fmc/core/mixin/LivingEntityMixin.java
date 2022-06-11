package lafolie.fmc.core.mixin;

import java.util.List;

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
		if(source.getAttacker() instanceof PlayerEntity)
		{
			return amount *= 999;
		}
		source = null; // should probably do this so that it can be GC'd
		return amount;
	}


	// (Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/damage/DamageSource;F)Z
	// (Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/entity/damage/DamageSource;F)Z

	// @Override
	// public void addElementalAspect(ElementalAspect element, ElementalAttribute attribute) {
	// 	// TODO Auto-generated method stub
		
	// }

	// @Override
	// public int getElementalAttribute(ElementalAspect element, ElementalAttribute attribute) {
	// 	// TODO Auto-generated method stub
	// 	return 0;
	// }

	// @Override
	// public int getElementalAffinity(ElementalAspect element) {
	// 	// TODO Auto-generated method stub
	// 	return 0;
	// }

	// @Override
	// public boolean hasElementalAspect(ElementalAspect element, ElementalAttribute attribute) {
	// 	// TODO Auto-generated method stub
	// 	return false;
	// }

	// @Override
	// public void removeElementalAspect(ElementalAspect element, ElementalAttribute attribute) {
	// 	// TODO Auto-generated method stub
		
	// }
}
