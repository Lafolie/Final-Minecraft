package lafolie.fmc.core.elements;

import net.minecraft.nbt.NbtCompound;

public interface ElementalNbtProvider
{
	public void AddElementalWeakness(Element element);
	public void AddElementalResistance(Element element);
	public void AddElementalAbsorbtion(Element element);

	public void RemoveElementalWeakness(Element element);
	public void RemoveElementalResistance(Element element);
	public void RemoveElementalAbsorbtion(Element element);

	public boolean HasElementalWeakness(Element element);
	public boolean HasElementalResistance(Element element);
	public boolean HasElementalAbsorbtion(Element element);
	
	public int GetTotalWeakResist(Element element);
}
