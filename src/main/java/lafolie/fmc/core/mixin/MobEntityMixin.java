package lafolie.fmc.core.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.elements.ElementalEquipment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity
{
	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
		//Auto-generated constructor stub
	}


	@Final
	@Shadow
	private DefaultedList<ItemStack> armorItems;

	// may have to change this later, or add another inject for weapons (hand slot)
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;set(ILjava/lang/Object;)Ljava/lang/Object;"), method = "equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V")
	private void armorEquipped(EquipmentSlot slot, ItemStack stack, CallbackInfo info)
	{
		if(world.isClient)
		{
			return;
		}

		ItemStack currentEquip = armorItems.get(slot.getEntitySlotId());
		if(!currentEquip.isEmpty())
		{
			FinalMinecraft.log.info("Removing effects from broken mob item");
			ElementalEquipment.removeEffects(this, currentEquip);
		}
		
		if(!stack.isEmpty())
		{
			FinalMinecraft.log.info("Adding effects from equipped mob item");
			ElementalEquipment.addEffects(this, stack);
		}
	}
}
