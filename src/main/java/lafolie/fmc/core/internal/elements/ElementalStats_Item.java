package lafolie.fmc.core.internal.elements;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
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
	public NbtCompound GetElementNbt()
	{
		return stack.getOrCreateSubNbt("");
	}

	@Override
	public void SetElementNbt(NbtCompound nbt)
	{
		stack.setSubNbt("", nbt);
	}
}
