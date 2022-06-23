package lafolie.fmc.core.chrono;

import java.util.ArrayList;
import java.util.List;

import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.zodiac.ZodiacSign;

/**
 * Months of the year.
 * Naming scheme: unused espers.
 */
public enum Month
{
	BISMARCK,
	KIRIN,
	EDEN,
	SYLDRA,
	MADUIN,
	BOCO,
	GILGAMESH,
	PANDEMONA,
	KUJATA,
	VALEFOR,
	IXION,
	MOG;

	private static List<Month> ORDS = new ArrayList<>();
	private static int ZODIAC_OFFSET = 16;
	static
	{
		for(Month month : Month.values())
		{
			ORDS.add(month);
		}
	}

	public static Month fromOrdinal(int ord)
	{
		return ORDS.get(ord);
	}

	public ZodiacSign getZodiacSign(int dayOfTheMonth)
	{
		// FinalMinecraft.log.info("--Day of the month: {}--", dayOfTheMonth);
		// int n = (dayOfTheMonth - ZODIAC_OFFSET) % 12;
		int n = dayOfTheMonth < ZODIAC_OFFSET ? this.ordinal() : (this.ordinal() + 1) % 12;
		return ZodiacSign.from(n);
	}
}
