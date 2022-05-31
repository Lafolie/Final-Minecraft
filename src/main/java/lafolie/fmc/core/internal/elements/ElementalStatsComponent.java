package lafolie.fmc.core.internal.elements;

import net.fabricmc.fabric.api.util.NbtType;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;

import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;

import net.minecraft.nbt.NbtCompound;

public interface ElementalStatsComponent extends ComponentV3
{
	public NbtCompound GetElementNbt();
	public void SetElementNbt(NbtCompound nbt);

	public default void AddElement(ElementalAttribute attribute, ElementalAspect element, byte inAmt)
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

	public default NbtCompound GetOrCreateElementalNbt(ElementalAttribute attribute)
	{
		NbtCompound nbt = GetElementNbt();
		String nbtKey = attribute.toNbtKey();

		if(!nbt.contains(nbtKey, NbtType.COMPOUND))
		{
			nbt.put(nbtKey, new NbtCompound());
			SetElementNbt(nbt);
		}

		return nbt.getCompound(nbtKey);
	}
}
