package lafolie.fmc.core.elements;

import lafolie.fmc.core.internal.elements.ElementalStats;
import lafolie.fmc.core.internal.elements.ElementalStatsComponent;
import net.minecraft.nbt.NbtCompound;

public interface ElementalObject
{
	/**
	 * This function is for internal use and should not be called.
	 * @return the ElementalStats component for this object
	 */
	public default ElementalStatsComponent getComponent()
	{
		return null;
	}

	/**
	 * Adds a base elemental aspect to the item.
	 * 
	 * @param element
	 * @param attribute
	 * @param amount the amount to add, should be positive
	 */
	public default void addBaseElementalAspect(ElementalAspect element, ElementalAttribute attribute, int amount)
	{
		byte amt = (byte)amount;
		ElementalStatsComponent stats = getComponent();
		stats.addElement(element, attribute, amt);
		if(attribute == ElementalAttribute.WEAKNESS)
		{
			stats.addElement(element.getStrongTo(), ElementalAttribute.RESISTANCE, amt);
		}
		else if(attribute == ElementalAttribute.RESISTANCE)
		{
			stats.addElement(element.getWeakTo(), ElementalAttribute.WEAKNESS, amt);
		}
	}

	/**
	 * Adds an elememental aspect to the item.
	 * If the attribute is WEAKNESS or RESISTANCE, the corresponding element's RESISTANCE/WEAKNESS
	 * will increase as well.
	 * For example, setting a FIRE RESISTANCE will increase WATER WEAKNESS
	 */
	public default void addElementalAspect(ElementalAspect element, ElementalAttribute attribute)
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
	public default void removeElementalAspect(ElementalAspect element, ElementalAttribute attribute)
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

	/**
	 * Checks whether an elemental attribute exists in the NBT.
	 * @param element
	 * @param attribute
	 * @return
	 */
	public default boolean hasElementalAspect(ElementalAspect element, ElementalAttribute attribute)
	{
		NbtCompound elements = getComponent().getOrCreateElementalNbt(attribute);
		return elements.contains(element.toString());
	}

	/**
	 * Get the integer value for the elemental attribute. Returns 0 if the
	 * elemental attribute does not exist.
	 * @param element
	 * @param attribute
	 * @return value of the elemental attribute
	 */
	public default int getElementalAttribute(ElementalAspect element, ElementalAttribute attribute)
	{
		ElementalStatsComponent stats = getComponent();
		NbtCompound nbt = stats.getOrCreateElementalNbt(attribute);
		String key = element.toString();
		return nbt.contains(key) ? (int)nbt.getByte(key) : 0;
	}

	/**
	 * Gets the total affinity for the specified element. The affinity is calculated as
	 * the total RESISTANCE - total WEAKNESS.
	 * @param element
	 * @return total affinity for the specified element
	 */
	public default int getElementalAffinity(ElementalAspect element)
	{
		return getElementalAttribute(element, ElementalAttribute.RESISTANCE) - getElementalAttribute(element, ElementalAttribute.WEAKNESS);
	}
}
