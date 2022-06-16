package lafolie.fmc.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.entity.DamageNumbers;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements DamageNumbers
{
	@Inject(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setHealth(F)V", shift = At.Shift.AFTER))
	private void applyDamage(DamageSource source, float amount, CallbackInfo info)
	{
		sendHealthModifiedPacket(amount, getLastAttributeUsed());
	}

	@Inject(at = @At("HEAD"), method = "equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V")
	public void equipStack(EquipmentSlot slot, ItemStack stack, CallbackInfo info)
	{
		FinalMinecraft.log.info("equipStack {} {}", slot.toString(), stack.toString());
	}

}
