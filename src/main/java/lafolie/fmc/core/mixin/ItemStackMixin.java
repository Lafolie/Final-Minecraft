package lafolie.fmc.core.mixin;

import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;
import lafolie.fmc.core.elements.ElementalEquipment;
import lafolie.fmc.core.elements.ElementalObject;

import lafolie.fmc.core.internal.Components;
import lafolie.fmc.core.internal.elements.ElementalStatsComponent;
import lafolie.fmc.core.internal.elements.InnateElemental;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ElementalObject
{
	@Shadow
	public abstract Item getItem();

	// @Inject(at = @At("TAIL"), method = "<init>*")
	// private void constructor(ItemConvertible item, int count, CallbackInfo info)
	// {
	// 	// addBaseElementalAspect(ElementalAspect.randomElement(), ElementalAttribute.RESISTANCE, 1);
	// }

	@Override
	public ElementalStatsComponent getComponent()
	{	
		ElementalStatsComponent component = Components.ELEMENTAL_STATS_ITEM.get(this);
		
		if(!component.hasInitInnate())
		{
			// add innate elements to the component
			component.setHasInitInnate();
			Item item = getItem();
			InnateElemental innate = (InnateElemental)item;
			for(Map.Entry<ElementalAspect, Integer> entry : innate.getInnateElements(ElementalAttribute.RESISTANCE).entrySet())
			{
				if(item.isDamageable())
				{
					addInnateElementalResistance(entry.getKey(), 2);
				}
				else
				{
					addElementalAspectRaw(entry.getKey(), ElementalAttribute.RESISTANCE, 2);
				}
			}
			// addInnate* requires manual sync
			component.trySync();
		}
		return component;
	}

	// @Inject(at = @At("HEAD"), method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V")
	// private <T extends LivingEntity> void damage(int amount, T entity, Consumer<T> breakCallback, CallbackInfoReturnable info)
	// {

	// }
	
	// @Inject(at = @At("HEAD"), method = "damage(ILjava/util/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z")
	// private boolean damage(int amount, Random random, @Nullable ServerPlayerEntity player, CallbackInfoReturnable info)
	// {
	// 	FinalMinecraft.log.info("ITEM DAMAGED: {} {}", this.toString(), amount);
	// }
}