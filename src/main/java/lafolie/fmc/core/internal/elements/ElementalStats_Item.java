package lafolie.fmc.core.internal.elements;

import java.util.Map;

import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;
import lafolie.fmc.core.elements.ElementalObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

/**
 * This class only exists because of ItemComponent...
 */
public class ElementalStats_Item extends ItemComponent implements ElementalStatsComponent
{
	public ElementalStats_Item(ItemStack stack)
	{
		super(stack);
	}

	@Override
	public NbtCompound getElementNbt()
	{
		return stack.getOrCreateSubNbt(KEY);
	}

	@Override
	public void setElementNbt(NbtCompound nbt)
	{
		stack.setSubNbt(KEY, nbt);
	}


}
