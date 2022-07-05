package lafolie.fmc.core.zodiac;

import lafolie.fmc.core.element.ElementalAspect;
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
	 * Get the compatibility with another birthsign based on
	 * Zodiac & Element
	 * @param other other birthsign to compare with
	 * @return compatibility multiplier
	 */
	public default float getFullCompatibility(BirthsignEntity other)
	{
		Birthsign sign = getComponent();
		float compatibility = sign.getZodiacSign().getCompatibility(other.getZodiacSign());
		if(sign.getElementalAspect() == other.getElementalAspect().getStrongTo())
		{
			compatibility = 0.75f;
		}
		return compatibility;
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
