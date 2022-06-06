package lafolie.fmc.core.internal.elements;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

/**
 * This class implements ComponentV3 directly since we are going to abstract the component stuff away.
 */
public class ElementalStats implements ElementalStatsComponent, AutoSyncedComponent
{
	private NbtCompound nbt = new NbtCompound();
	private boolean initInnate = false;

	private Object provider;

	public ElementalStats(Object object)
	{
		provider = object;
	}

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