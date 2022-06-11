package lafolie.fmc.core.elements;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.google.errorprone.annotations.DoNotCall;

import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.internal.elements.ElementalStats;
import lafolie.fmc.core.internal.elements.ElementalStatsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;

/**
 * Represents an Entity/Item/Player/etc with elemental properties.
 * Some methods should not be called but exist here because mixins cannot inherit from an
 * abstract class. Isn't OOP wonderful?
 */
public interface ElementalObject
{
	// public final Map<ElementalAttribute, AffinityCache> affinityCaches = new EnumMap(ElementalAttribute.class);

	// public static class AffinityCache
	// {
	// 	private ElementalObject owner;
	// 	private ElementalAttribute attribute;

	// 	private boolean isDirty = true;
	// 	private final List<ElementalAspect> elements = new ArrayList<>();

	// 	public AffinityCache(ElementalObject owner, ElementalAttribute attribute)
	// 	{
	// 		this.owner = owner;
	// 		this.attribute = attribute;
	// 	}

	// 	private void rebuild()
	// 	{
	// 		elements.clear();
	// 		for(ElementalAspect element : ElementalAspect.values())
	// 		{
	// 			if(owner.getElementalAffinity(element, attribute) > 0)
	// 			{
	// 				elements.add(element);
	// 			}
	// 		}
	// 		isDirty = false;
	// 	}

	// 	public void markDirty()
	// 	{
	// 		isDirty = true;
	// 	}

	// 	public List<ElementalAspect> getList()
	// 	{
	// 		if(isDirty)
	// 		{
	// 			rebuild();
	// 		}
	// 		return elements;
	// 	}
	// }
	
	// /**
	//  * This function is for internal used and should not be called.
	//  * @return
	//  */
	//// public default AffinityCache getOrCreateAffinityCache(ElementalAttribute attribute)
	// {
	// 	if(!affinityCaches.containsKey(attribute))
	// 	{
	// 		affinityCaches.put(attribute, new AffinityCache(this, attribute));
	// 	}
	// 	return affinityCaches.get(attribute);
	// }

	/**
	 * This function is for internal use and should not be called.
	 * @return the ElementalStats component for this object
	 */
	public default ElementalStatsComponent getComponent()
	{
		return null;
	}

	// /**
	//  * Adds a base elemental aspect to the object.
	//  * This method is intended for innate (base) aspects, i.e. for mobs that
	//  * have an intrisic weak/resist for the specified type that would otherwise
	//  * require multiple inefficient calls to addElementalAspect
	//  * 
	//  * @param element
	//  * @param attribute
	//  * @param amount the amount to add, should be positive
	//  */
	// public default void addBaseElementalAspect(ElementalAspect element, ElementalAttribute attribute, int amount)
	// {
	// 	byte amt = (byte)amount;
	// 	ElementalStatsComponent stats = getComponent();
	// 	stats.addElement(element, attribute, amt);
	// 	if(attribute == ElementalAttribute.WEAKNESS)
	// 	{
	// 		stats.addElement(element.getStrongTo(), ElementalAttribute.RESISTANCE, amt);
	// 		//getOrCreateAffinityCache(ElementalAttribute.RESISTANCE).markDirty();

	// 	}
	// 	else if(attribute == ElementalAttribute.RESISTANCE)
	// 	{
	// 		stats.addElement(element.getWeakTo(), ElementalAttribute.WEAKNESS, amt);
	// 		//getOrCreateAffinityCache(ElementalAttribute.WEAKNESS).markDirty();

	// 	}

	// 	//getOrCreateAffinityCache(attribute).markDirty();
	// }

	/**
	 * Add an Elemental Aspect to the object.
	 * @param element the Elemental Aspect to apply
	 * @param attribute the Elemental Attribute to add to
	 * @param amount the amount of points to add
	 */
	public default void addElementalAspect(ElementalAspect element, ElementalAttribute attribute, int amount)
	{
		ElementalStatsComponent stats = getComponent();
		stats.addElement(element, attribute, (byte)amount);
		stats.trySync();
	}

	/**
	 * Remove an Elemental Aspect from the object.
	 * @param element the Elemental Aspect to remove
	 * @param attribute the Elemental Attribute to add to
	 * @param amount the amount of points to add
	 */
	public default void removeElementalAspect(ElementalAspect element, ElementalAttribute attribute, int amount)
	{
		ElementalStatsComponent stats = getComponent();
		stats.addElement(element, attribute, (byte)-amount);
		stats.trySync();
	}

	/**
	 * Adds a restance to object (value 2) whilst automatically increasing resistance to
	 * the strongTo element (value 1), and increasing weakness to weakTo element (value 2).
	 * Holy and Dark are accounted for.
	 * @param element
	 */
	public default void addElementalResistance(ElementalAspect element)
	{
		ElementalStatsComponent stats = getComponent();
		stats.addElement(element, ElementalAttribute.RESISTANCE, (byte)2);
		stats.addElement(element.getWeakTo(), ElementalAttribute.WEAKNESS, (byte)2);
		if(element != ElementalAspect.DARK && element != ElementalAspect.HOLY)
		{
			stats.addElement(element.getStrongTo(), ElementalAttribute.RESISTANCE, (byte)1);
		}
		stats.trySync();
	}

	/**
	 * Removes resistance from object (value 2) whilst automatically removing resistance to
	 * the strongTo element (value 1), and removing weakness to weakTo element (value 2).
	 * Holy and Dark are accounted for.
	 * @param element
	 */
	public default void removeElementalResistance(ElementalAspect element)
	{
		ElementalStatsComponent stats = getComponent();
		stats.addElement(element, ElementalAttribute.RESISTANCE, (byte)-2);
		stats.addElement(element.getWeakTo(), ElementalAttribute.WEAKNESS, (byte)-2);
		if(element != ElementalAspect.DARK && element != ElementalAspect.HOLY)
		{
			stats.addElement(element.getStrongTo(), ElementalAttribute.RESISTANCE, (byte)-1);
		}
		stats.trySync();
	}

	/**
	 * Add an aspect without triggering a trySync().
	 * FOR INTERNAL USE ONLY
	 * @param element
	 * @param attribute
	 * @param amount
	 */
	public default void addInnateElementalAspect(ElementalAspect element, ElementalAttribute attribute, int amount)
	{
		ElementalStatsComponent stats = getComponent();
		stats.addElement(element, attribute, (byte)amount);
	}

	/**
	 * Add an aspect resistance with auto-weak/sub-resist without triggering a trySync().
	 * Intended for mobs.
	 * FOR INTERNAL USE ONLY
	 * @param element
	 * @param attribute
	 * @param amount
	 */
	public default void addInnateElementalResistance(ElementalAspect element, int amount)
	{
		ElementalStatsComponent stats = getComponent();
		stats.addElement(element, ElementalAttribute.RESISTANCE, (byte)amount);
		stats.addElement(element.getWeakTo(), ElementalAttribute.WEAKNESS, (byte)amount);
		if(element != ElementalAspect.DARK && element != ElementalAspect.HOLY)
		{
			stats.addElement(element.getStrongTo(), ElementalAttribute.RESISTANCE, (byte)MathHelper.floor(amount / 2));
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
	public default int getElementalAffinity(ElementalAspect element, ElementalAttribute attribute)
	{
		ElementalStatsComponent stats = getComponent();
		NbtCompound nbt = stats.getOrCreateElementalNbt(attribute);
		String key = element.toString();
		return nbt.contains(key) ? (int)nbt.getByte(key) : 0;
	}

	/**
	 * Gets the total weak/resist integer for the specified element. The affinity is calculated as
	 * the total RESISTANCE - total WEAKNESS.
	 * A positive value implies resistant affinity.
	 * A negative value implies weakness affinity.
	 * @param element
	 * @return total affinity for the specified element
	 */
	public default int getWeakResistAffinity(ElementalAspect element)
	{
		return getElementalAffinity(element, ElementalAttribute.RESISTANCE) - getElementalAffinity(element, ElementalAttribute.WEAKNESS);
	}

	/**
	 * Get a map of all the elemental affinities for the provided attribute
	 */
	public default Map<ElementalAspect, Integer> getElementalAffinities(ElementalAttribute attribute)
	{
		Map<ElementalAspect, Integer> result = new EnumMap<>(ElementalAspect.class);
		
		for(ElementalAspect element : ElementalAspect.values())
		{
			int affinity = getElementalAffinity(element, attribute);
			if(affinity > 0 )
			{
				result.put(element, affinity);
			}
		}

		return result;
	}

	public default ElementalAttribute getSomeAttribute(ElementalAspect element)
	{
		return ElementalAttribute.RESISTANCE;
	}
}
