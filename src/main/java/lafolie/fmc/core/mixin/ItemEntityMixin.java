package lafolie.fmc.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import lafolie.fmc.core.elements.ElementalObject;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin
{
	@Inject(at = @At("HEAD"), method = "setStack(Lnet/minecraft/item/ItemStack;)V")
	public void getStack(ItemStack stack, CallbackInfo info)
	{
		// Calling this initialises the stack's elemental stats and prevents bugs
		((ElementalObject)(Object)stack).getComponent();
	}
}
