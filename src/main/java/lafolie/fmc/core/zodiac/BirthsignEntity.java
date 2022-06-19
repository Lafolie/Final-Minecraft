package lafolie.fmc.core.zodiac;

import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.internal.Components;
import lafolie.fmc.core.internal.zodiac.Birthsign;

public interface BirthsignEntity
{
	private Birthsign getComponent()
	{
		return Components.BIRTHSIGN.get(this);
	}

	/**
	 * Get the Zodiac birthsign for this entity
	 * @return
	 */
	public default ZodiacSign getZodiacSign()
	{
		return getComponent().getZodiacSign();
	}

	/**
	 * Get the Element birthsign for this entity
	 * @return
	 */
	public default ElementalAspect getElementalAspect()
	{
		return getComponent().getElementalAspect();
	}

	/**
	 * Initialises the birthsign.
	 * Called internally.
	 */
	public default void init()
	{
		getComponent().init();
	}
}
