package lafolie.fmc.core.element;

/**
 * Attributes define how ElementalAspects modify incoming damage.
 */
public enum ElementalAttribute
{
	/**
	 * Increases damage.
	 */
	WEAKNESS("weak"),
	/**
	 * Reduces damage.
	 */
	RESISTANCE("res"),
	/**
	 * Damage will be nullified (0).
	 */
	IMMUNITY("immune"),
	/**
	 * Damage becomes healing.
	 */
	ABSORBTION("absorb"),
	/**
	 * Damage will instantly kill.
	 */
	FATAL("fatal"),
	/**
	 * Damage will fully heal.
	 */
	REVIVE("revive");

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
