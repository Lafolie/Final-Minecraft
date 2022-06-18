package lafolie.fmc.core.zodiac;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Signs of the Zodiac. Used in damage calculations and
 * by some special abilities.
 */
public enum ZodiacSign
{
	ARIES,
	TAURUS,
	GEMINI,
	CANCER,
	LEO,
	VIRGO,
	LIBRA,
	SCORPIO,
	SAGITTARIUS,
	CAPRICORN,
	AQUARIUS,
	PISCES;

	private static Map<ZodiacSign, ZodiacAlignment> alignment = new EnumMap<>(ZodiacSign.class);
	private static List<ZodiacSign> ords = new ArrayList<>();
	static
	{
		for(ZodiacSign sign : ZodiacSign.values())
		{
			ords.add(sign);
		}

		for(ZodiacSign sign : ZodiacSign.values())
		{
			alignment.put(sign, new ZodiacAlignment(sign));
		}
	}

	/**
	 * Get a ZodiacSign from an ordinal index
	 * @param ordinal index
	 * @return ZodiacSign
	 */
	public static ZodiacSign from(int ordinal)
	{
		return ords.get(ordinal);
	}

	/**
	 * Get a ZodiacSign by rotating across the Zodiac 'wheel'.
	 * See {@link}https://gamefaqs.gamespot.com/ps/197339-final-fantasy-tactics/map/6667-zodiac-compatibility-chart
	 * @param offset number of indices rotate by
	 * @return ZodiacSign
	 */
	public ZodiacSign rotate(int offset)
	{
		int n = (this.ordinal() + offset) % 12;
		return ords.get(n);
	}

	/**
	 * Get the compatibility multiplier for the given sign
	 * @param sign sign to check for
	 * @return compatibility
	 */
	public float getCompatibility(ZodiacSign sign)
	{
		return alignment.get(this).get(sign);
	}

	/**
	 * Get the compatibility multiplier between two signs
	 * @param signA
	 * @param signB
	 * @return compatibility
	 */
	public static float getCompatibility(ZodiacSign signA, ZodiacSign signB)
	{
		return alignment.get(signA).get(signB);
	}
}
