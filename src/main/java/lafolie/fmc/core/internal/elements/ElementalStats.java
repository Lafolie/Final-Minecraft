package lafolie.fmc.core.internal.elements;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import lafolie.fmc.core.internal.Components;
import net.minecraft.nbt.NbtCompound;

public class ElementalStats implements ElementalStatsComponent, AutoSyncedComponent
{
	private NbtCompound nbt = new NbtCompound();

	private Object provider;

	public ElementalStats(Object object)
	{
		provider = object;
	}

	// ElementalStatsComponent methods

	@Override
	public NbtCompound getElementNbt()
	{
		return nbt;
	}

	@Override
	public void setElementNbt(NbtCompound nbt)
	{
		this.nbt = nbt;
	}

	@Override
	public void trySync()
	{
		Components.ELEMENTAL_STATS.sync(provider);
	}

	// Component methods (disk I/O)

	@Override
	public void readFromNbt(NbtCompound tag)
	{
		nbt = tag.getCompound(KEY);
	}

	@Override
	public void writeToNbt(NbtCompound tag)
	{
		tag.put(KEY, nbt);
	}
}