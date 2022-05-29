package lafolie.fmc.core.internal.elements;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;
import net.minecraft.nbt.NbtCompound;

/**
 * This class implements ComponentV3 directly since we are going to abstract the component stuff away.
 */
public class ElementalStats implements ComponentV3, AutoSyncedComponent
{
	private static final String KEY = "FMC_ElementalAffinity";

	private NbtCompound nbt = new NbtCompound();

	public ElementalStats()
	{
		
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

	public void AddElement(ElementalAttribute attribute, ElementalAspect element, byte inAmt)
	{
		String key = element.toString();

		NbtCompound elements = GetOrCreateElementalNbt(attribute);
		if(!elements.contains(key))
		{
			elements.putByte(key, inAmt);
		}
		else
		{
			byte amt = elements.getByte(key);
			amt += inAmt;
			if(amt > (byte)0)
			{
				elements.putByte(key, amt);
			}
			else
			{
				elements.remove(key);
			}
		}
	}

	private NbtCompound GetOrCreateElementalNbt(ElementalAttribute attribute)
	{
		String nbtKey = attribute.toNbtKey();

		if(!nbt.contains(nbtKey, 10))
		{
			nbt.put(nbtKey, new NbtCompound());
		}

		return nbt.getCompound(nbtKey);
	}
}