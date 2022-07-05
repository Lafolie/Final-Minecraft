package lafolie.fmc.core.internal.element;

import dev.onyxstudios.cca.api.v3.item.ItemComponent;

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
