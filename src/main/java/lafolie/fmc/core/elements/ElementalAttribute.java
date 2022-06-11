package lafolie.fmc.core.elements;

import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * Attributes define how ElementalAspects modify incoming damage.
 */
public enum ElementalAttribute
{
	/**
	 * Increases damage.
	 */
	WEAKNESS("FMC_ElementalWeakness"),
	/**
	 * Reduces damage.
	 */
	RESISTANCE("FMC_ElementalResistance"),
	/**
	 * Damage becomes healing.
	 */
	ABSORBTION("FMC_ElementalAbsorbtion"),
	/**
	 * Damage will be nullified (0).
	 */
	IMMUNITY("FMC_ElementalImmunity"),
	/**
	 * Damage will instantly kill.
	 */
	FATAL("FMC_ElementalFatal"),
	/**
	 * Damage will fully heal.
	 */
	REVIVE("FMC_ElementalRevive");

	public static final List<ElementalAttribute> PRIORITY_LIST = ImmutableList.of(
		REVIVE,
		FATAL,
		ABSORBTION,
		IMMUNITY,
		WEAKNESS,
		RESISTANCE);

	private String key;

	public String toNbtKey()
	{
		return key;
	}

	private ElementalAttribute(String str)
	{
		key = str;
	}
}
