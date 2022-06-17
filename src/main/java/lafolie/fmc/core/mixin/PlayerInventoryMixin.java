package lafolie.fmc.core.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.elements.ElementalEquipment;
import lafolie.fmc.core.elements.ElementalObject;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
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

	/*
	damage(Lnet/minecraft/entity/damage/DamageSource;F[I)V
	*/
	@Inject
		(
			at = @At
			(
				value = "INVOKE",
				target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V",
				shift = At.Shift.AFTER
			),
			method = "damageArmor(Lnet/minecraft/entity/damage/DamageSource;F[I)V",
			locals = LocalCapture.CAPTURE_FAILHARD
		)
	private void damageArmorInject(DamageSource damageSource, float amount, int[] slots, CallbackInfo info, int var4[], int var5, int var6, int i, ItemStack stack)
	{
		if(!player.world.isClient && stack.isEmpty())
		{
			FinalMinecraft.log.info("DESTROYED ARMOR {}", stack);
			/*
			 * The stack currently has a count of 0 as it was destroyed. This
			 * means that when the ElementalObject called getItem, it will
			 * return AIR, which has no ElementalStatsComponent.
			 * Thus, we temporarily increase the count, then reset when we
			 * are done using it.
			 */
			stack.increment(1);
			((ElementalObject)(Object)stack).removeFromObject((ElementalObject)player);
			stack.decrement(1);
		}
	}

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
