package lafolie.fmc.core.elements;

import java.util.EnumMap;

/**
 * Static utility methods for ElementalObjects
 */
public final class Utils
{
	private Utils() {};

	public static final EnumMap<ElementalAspect, String> ElementLangKeys = new EnumMap<ElementalAspect, String>(ElementalAspect.class);
	static
	{
		ElementLangKeys.put(ElementalAspect.NONE, "fmc.core.element.tooltip.none");
		ElementLangKeys.put(ElementalAspect.FIRE, "fmc.core.element.tooltip.fire");
		ElementLangKeys.put(ElementalAspect.ICE, "fmc.core.element.tooltip.ice");
		ElementLangKeys.put(ElementalAspect.LIGHTNING, "fmc.core.element.tooltip.lightning");
		ElementLangKeys.put(ElementalAspect.WIND, "fmc.core.element.tooltip.wind");
		ElementLangKeys.put(ElementalAspect.WATER, "fmc.core.element.tooltip.water");
		ElementLangKeys.put(ElementalAspect.EARTH, "fmc.core.element.tooltip.earth");
		ElementLangKeys.put(ElementalAspect.POISON, "fmc.core.element.tooltip.poison");
		ElementLangKeys.put(ElementalAspect.HOLY, "fmc.core.element.tooltip.holy");
		ElementLangKeys.put(ElementalAspect.DARK, "fmc.core.element.tooltip.dark");
		ElementLangKeys.put(ElementalAspect.GRAVITY, "fmc.core.element.tooltip.gravity");
	}
}