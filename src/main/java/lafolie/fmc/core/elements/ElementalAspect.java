package lafolie.fmc.core.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * Enum representing Elemental Aspects
 */
public enum ElementalAspect
{
	NONE,
	FIRE,
	ICE,
	LIGHTNING,
	WIND,
	WATER,
	EARTH,
	POISON,
	HOLY,
	DARK,
	GRAVITY;

	/**
	 * Get a random element (excludes NONE)
	 * @return a random element
	 */
	public static ElementalAspect RandomElement()
	{
		ArrayList<ElementalAspect> list = new ArrayList<ElementalAspect>();
		for(ElementalAspect e : EnumSet.range(FIRE, GRAVITY))
		{
			list.add(e);
		}
		Collections.shuffle(list);
		return list.get(0);
	}

	/**
	 * Get a random element from the provided list
	 * @return random element
	 */
	public static ElementalAspect RandomElement(List<ElementalAspect> list)
	{
		List<ElementalAspect> temp = new ArrayList<ElementalAspect>();
		Collections.copy(temp, list);
		Collections.shuffle(temp);
		return list.get(0);
	}
}
