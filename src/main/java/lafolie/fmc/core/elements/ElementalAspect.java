package lafolie.fmc.core.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
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

	private static EnumMap<ElementalAspect, ElementalAspect> WEAK = new EnumMap<ElementalAspect, ElementalAspect>(ElementalAspect.class);
	private static EnumMap<ElementalAspect, ElementalAspect> STRONG = new EnumMap<ElementalAspect, ElementalAspect>(ElementalAspect.class);
	public static final EnumMap<ElementalAspect, String> LANG_KEYS = new EnumMap<ElementalAspect, String>(ElementalAspect.class);
	
	static
	{
		// FFXI system
		/*
		 * Fire melts Ice
		 * Ice blocks Wind
		 * Wind erodes Earth
		 * Earth grounds Thunder
		 * Thunder shocks Water
		 * Water douses Fire
		 * Light illuminates Dark
		 * Dark eclipses Light
		 */
		
		WEAK.put(ElementalAspect.NONE, ElementalAspect.NONE);
		WEAK.put(ElementalAspect.FIRE, ElementalAspect.WATER);
		WEAK.put(ElementalAspect.ICE, ElementalAspect.FIRE);
		WEAK.put(ElementalAspect.LIGHTNING, ElementalAspect.EARTH);
		WEAK.put(ElementalAspect.WIND, ElementalAspect.ICE);
		WEAK.put(ElementalAspect.WATER, ElementalAspect.LIGHTNING);
		WEAK.put(ElementalAspect.EARTH, ElementalAspect.WIND);
		WEAK.put(ElementalAspect.POISON, ElementalAspect.NONE);
		WEAK.put(ElementalAspect.HOLY, ElementalAspect.DARK);
		WEAK.put(ElementalAspect.DARK, ElementalAspect.HOLY);
		WEAK.put(ElementalAspect.GRAVITY, ElementalAspect.NONE);

		STRONG.put(ElementalAspect.NONE, ElementalAspect.NONE);
		STRONG.put(ElementalAspect.FIRE, ElementalAspect.ICE);
		STRONG.put(ElementalAspect.ICE, ElementalAspect.WIND);
		STRONG.put(ElementalAspect.LIGHTNING, ElementalAspect.WATER);
		STRONG.put(ElementalAspect.WIND, ElementalAspect.EARTH);
		STRONG.put(ElementalAspect.WATER, ElementalAspect.ICE);
		STRONG.put(ElementalAspect.EARTH, ElementalAspect.LIGHTNING);
		STRONG.put(ElementalAspect.POISON, ElementalAspect.NONE);
		STRONG.put(ElementalAspect.HOLY, ElementalAspect.DARK);
		STRONG.put(ElementalAspect.DARK, ElementalAspect.HOLY);
		STRONG.put(ElementalAspect.GRAVITY, ElementalAspect.NONE);

		LANG_KEYS.put(ElementalAspect.NONE, "fmc.core.element.tooltip.none");
		LANG_KEYS.put(ElementalAspect.FIRE, "fmc.core.element.tooltip.fire");
		LANG_KEYS.put(ElementalAspect.ICE, "fmc.core.element.tooltip.ice");
		LANG_KEYS.put(ElementalAspect.LIGHTNING, "fmc.core.element.tooltip.lightning");
		LANG_KEYS.put(ElementalAspect.WIND, "fmc.core.element.tooltip.wind");
		LANG_KEYS.put(ElementalAspect.WATER, "fmc.core.element.tooltip.water");
		LANG_KEYS.put(ElementalAspect.EARTH, "fmc.core.element.tooltip.earth");
		LANG_KEYS.put(ElementalAspect.POISON, "fmc.core.element.tooltip.poison");
		LANG_KEYS.put(ElementalAspect.HOLY, "fmc.core.element.tooltip.holy");
		LANG_KEYS.put(ElementalAspect.DARK, "fmc.core.element.tooltip.dark");
		LANG_KEYS.put(ElementalAspect.GRAVITY, "fmc.core.element.tooltip.gravity");
	}

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

	public String GetLangKey()
	{
		return LANG_KEYS.get(this);
	}

	public static String GetLangKey(ElementalAspect element)
	{
		return LANG_KEYS.get(element);
	}

	public static ElementalAspect GetStrongTo(ElementalAspect element)
	{
		return STRONG.get(element);
	}

	public ElementalAspect GetStrongTo()
	{
		return STRONG.get(this);
	}

	public static ElementalAspect GetWeakTo(ElementalAspect element)
	{
		return WEAK.get(element);
	}

	public ElementalAspect GetWeakTo()
	{
		return WEAK.get(this);
	}
}
