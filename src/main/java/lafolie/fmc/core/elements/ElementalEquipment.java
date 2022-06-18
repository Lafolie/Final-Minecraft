package lafolie.fmc.core.elements;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public abstract class ElementalEquipment
{
	public static void addEffects(LivingEntity entity, ItemStack stack)
	{
		ElementalObject eStack =  (ElementalObject)(Object)stack;
		ElementalObject ePlayer = (ElementalObject)entity;
		eStack.applyToObject(ePlayer);

	}

	public static void removeEffects(LivingEntity entity, ItemStack stack)
	{
		ElementalObject eStack =  (ElementalObject)(Object)stack;
		ElementalObject ePlayer = (ElementalObject)entity;
		eStack.removeFromObject(ePlayer);
	}
}
