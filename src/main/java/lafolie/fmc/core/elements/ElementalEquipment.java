package lafolie.fmc.core.elements;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface ElementalEquipment
{
	public default void addEffects(LivingEntity entity, ItemStack stack)
	{
		ElementalObject eStack =  (ElementalObject)(Object)stack;
		ElementalObject ePlayer = (ElementalObject)entity;
		eStack.applyToObject(ePlayer);

	}

	public default void removeEffects(LivingEntity entity, ItemStack stack)
	{
		ElementalObject eStack =  (ElementalObject)(Object)stack;
		ElementalObject ePlayer = (ElementalObject)entity;
		eStack.removeFromObject(ePlayer);
	}
}
