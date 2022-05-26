package lafolie.fmc.core.elements;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import net.minecraft.nbt.NbtByte;

/**
 * Enum representing Elemental Aspects
 */
public enum Element
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

	public static final ImmutableMap<Element, String> TOOLTIP_TEXT = 
		new ImmutableMap.Builder<Element, String>()
			.put(Element.NONE, "element.tooltip.none")
			.put(Element.FIRE, "element.tooltip.fire")
			.put(Element.ICE, "element.tooltip.ice")
			.put(Element.LIGHTNING, "element.tooltip.lightning")
			.put(Element.WIND, "element.tooltip.wind")
			.put(Element.WATER, "element.tooltip.water")
			.put(Element.EARTH, "element.tooltip.earth")
			.put(Element.POISON, "element.tooltip.poison")
			.put(Element.HOLY, "element.tooltip.holy")
			.put(Element.DARK, "element.tooltip.dark")
			.put(Element.GRAVITY, "element.tooltip.gravity")
			.build();
}
