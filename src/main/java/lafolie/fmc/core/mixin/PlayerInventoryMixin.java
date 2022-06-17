package lafolie.fmc.core.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.elements.ElementalEquipment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin
{
	@Final
	@Shadow
    public PlayerEntity player;
	// @Inject(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setHealth(F)V", shift = At.Shift.AFTER))

	// @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;set(ILjava/lang/Object;)Ljava/lang/Object;"), method = "setStack")
	@Inject(at = @At("HEAD"), method = "setStack(ILnet/minecraft/item/ItemStack;)V")
	private void onStackSet(int slot, ItemStack stack, CallbackInfo info)
	{

		if(!player.world.isClient && isEquipmentSlot(slot))
		{
			FinalMinecraft.log.info("SLOT: {}", slot);
			FinalMinecraft.log.info("\tequip stack {} {}", slot, stack.toString());
			ItemStack currentEquip = ((PlayerInventory)(Object)this).getStack(slot);
			FinalMinecraft.log.info("\tReplacing: {}", currentEquip.toString());
			
			if(!currentEquip.isEmpty())
			{
				((ElementalEquipment)(Object)currentEquip).removeEffects(player, currentEquip);
			}
			
			if(!stack.isEmpty())
			{
				((ElementalEquipment)(Object)stack).addEffects(player, stack);
			}
			FinalMinecraft.log.info("-----------------------------------------------------------");

		}
	}

	// public void removeOne(ItemStack stack)
	// {
	// 	stackRemoved(slot);
	// }

	@Inject(at = @At("HEAD"), method = "removeStack(II)Lnet/minecraft/item/ItemStack;")
	private void onStackRemoved(int slot, int amount, CallbackInfoReturnable info)
	{
		stackRemoved(slot);
	}


	@Inject(at = @At("HEAD"), method = "removeStack(I)Lnet/minecraft/item/ItemStack;")
	private void onStackRemoved(int slot, CallbackInfoReturnable info)
	{
		stackRemoved(slot);
	}

	private void stackRemoved(int slot)
	{
		if(!player.world.isClient && isEquipmentSlot(slot))
		{
			ItemStack currentEquip = ((PlayerInventory)(Object)this).getStack(slot);
			FinalMinecraft.log.info("REMOVING {}", currentEquip.toString());
			if(!currentEquip.isEmpty())
			{
				((ElementalEquipment)(Object)currentEquip).removeEffects(player, currentEquip);
			}
		}
	}

	/**
	 * Check whether a slot is an equipment slot
	 * @param slot slot index
	 * @return true if the slot is an equipment slot
	 */
	private boolean isEquipmentSlot(int slot)
	{
		return slot >= 35 && slot <= 39;
	}
}
