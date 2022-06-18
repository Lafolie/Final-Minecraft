package lafolie.fmc.core.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ElementalAspects affect damage according to the sources' aspect(s) and
 * the target ElementalObject's stats.
 * <p><pre>&nbsp;None should generally not be used.
 * Poison and Gravity have no opposing aspects.</pre></p>
 * <p><ul>
 * <li><pre>Fire melts Ice</pre></li>
 * <li><pre>Ice blocks Wind</pre></li>
 * <li><pre>Wind erodes Earth</pre></li>
 * <li><pre>Earth grounds Thunder</pre></li>
 * <li><pre>Thunder shocks Water</pre></li>
 * <li><pre>Water douses Fire</pre></li>
 * <li><pre>Holy illuminates Dark</pre></li>
 * <li><pre>Dark eclipses Holy</pre></li>
 * </ul>
 * </p>
 * 
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

	private static Map<ElementalAspect, ElementalAspect> WEAK = new EnumMap<>(ElementalAspect.class);
	private static Map<ElementalAspect, ElementalAspect> STRONG = new EnumMap<>(ElementalAspect.class);
	private static Map<String, ElementalAspect> NBT_KEYS = new HashMap<>();
	public static final Map<ElementalAspect, String> LANG_KEYS = new EnumMap<>(ElementalAspect.class);
	
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

		for(ElementalAspect element : ElementalAspect.values())
		{
			NBT_KEYS.put(element.toNbtKey(), element);
		}
	}

	/**
	 * Get a random element (excludes NONE)
	 * @return a random element
	 */
	public static ElementalAspect randomElement()
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
	public static ElementalAspect randomElement(List<ElementalAspect> list)
	{
		List<ElementalAspect> temp = new ArrayList<ElementalAspect>();
		Collections.copy(temp, list);
		Collections.shuffle(temp);
		return list.get(0);
	}

	public String getLangKey()
	{
		return LANG_KEYS.get(this);
	}

	public static String getLangKey(ElementalAspect element)
	{
		return LANG_KEYS.get(element);
	}

	public static ElementalAspect getStrongTo(ElementalAspect element)
	{
		return STRONG.get(element);
	}

	public ElementalAspect getStrongTo()
	{
		return STRONG.get(this);
	}

	public static ElementalAspect getWeakTo(ElementalAspect element)
	{
		return WEAK.get(element);
	}

	public ElementalAspect getWeakTo()
	{
		return WEAK.get(this);
	}

	public String toNbtKey()
	{
		return this.toString();
	}

	public static String toNbtKey(ElementalAspect aspect)
	{
		return aspect.toString();
	}

	public static ElementalAspect fromNbtKey(String key)
	{
		return NBT_KEYS.containsKey(key) ? NBT_KEYS.get(key) : NONE;
	}

}
