package lafolie.fmc.core.elements;

import lafolie.fmc.core.internal.elements.ElementalStats;
import lafolie.fmc.core.internal.elements.ElementalStatsComponent;
import net.minecraft.nbt.NbtCompound;

public abstract class ElementalObject
{
	/**
	 * This function is for internal use and should not be called.
	 * @return the ElementalStats component for this object
	 */
	protected ElementalStats getComponent()
	{
		return null;
	}

	/**
	 * Adds a base elemental aspect to the item.
	 * 
	 * @param element
	 * @param attribute
	 * @param amount the amount to add, must be positive!
	 */
	public void addBaseElementalAspect(ElementalAspect element, ElementalAttribute attribute, int amount)
	{
		getComponent().addElement(element, attribute, (byte)amount);
	}

	/**
	 * Adds an elememental aspect to the item.
	 * If the attribute is WEAKNESS or RESISTANCE, the corresponding element's RESISTANCE/WEAKNESS
	 * will increase as well.
	 * For example, setting a FIRE RESISTANCE will increase WATER WEAKNESS
	 */
	public void addElementalAspect(ElementalAspect element, ElementalAttribute attribute)
	{
		ElementalStatsComponent stats = getComponent();
		stats.addElement(element, attribute, (byte)1);
		if(attribute == ElementalAttribute.WEAKNESS)
		{
			stats.addElement(element.getStrongTo(), ElementalAttribute.RESISTANCE, (byte)1);
		}
		else if(attribute == ElementalAttribute.RESISTANCE)
		{
			stats.addElement(element.getWeakTo(), ElementalAttribute.WEAKNESS, (byte)1);
		}
	}

	/**
	 * Removes an elememental aspect to the item.
	 * If the attribute is WEAKNESS or RESISTANCE, the corresponding element's RESISTANCE/WEAKNESS
	 * will increase as well.
	 * For example, removing a FIRE RESISTANCE will lower WATER WEAKNESS
	 * An element's tag will be removed if the value drops to 0.
	 */
	public void removeElementalAspect(ElementalAspect element, ElementalAttribute attribute)
	{
		ElementalStatsComponent stats = getComponent();
		stats.addElement(element, attribute, (byte)-1);
		if(attribute == ElementalAttribute.WEAKNESS)
		{
			stats.addElement(element.getStrongTo(), ElementalAttribute.RESISTANCE, (byte)-1);
		}
		else if(attribute == ElementalAttribute.RESISTANCE)
		{
			stats.addElement(element.getWeakTo(), ElementalAttribute.WEAKNESS, (byte)-1);
		}
	}

	public boolean hasElementalAspect(ElementalAspect element, ElementalAttribute attribute)
	{
		NbtCompound elements = getComponent().getOrCreateElementalNbt(attribute);
		return elements.contains(element.toString());
	}

	public int getElementalAttribute(ElementalAspect element, ElementalAttribute attribute)
	{
		NbtCompound nbt = getComponent().getOrCreateElementalNbt(attribute);
		String key = element.toString();
		return (int)nbt.getByte(key);
	}

	public int getElementalAffinity(ElementalAspect element)
	{
		return getElementalAttribute(element, ElementalAttribute.RESISTANCE) - getElementalAttribute(element, ElementalAttribute.WEAKNESS);
	}
}
