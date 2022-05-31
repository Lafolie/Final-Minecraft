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
	private static final String KEY = "FMC_ElementalStats";

	private NbtCompound nbt = new NbtCompound();

	private Object provider;

	public ElementalStats(Object object)
	{
		provider = object;
	}

	@Override
	public NbtCompound GetElementNbt()
	{
		return nbt;
	}

	@Override
	public void SetElementNbt(NbtCompound nbt)
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

	private void SyncTag()
	{

	}
}