package lafolie.fmc.core.zodiac;

import java.util.EnumMap;
import java.util.Map;

public class ZodiacAlignment
{
	private final Map<ZodiacSign, Float> alignment = new EnumMap<>(ZodiacSign.class);

	public ZodiacAlignment(ZodiacSign sign)
	{
		alignment.put(sign.rotate(6), 1.5f);
		alignment.put(sign.rotate(4), 1.25f);
		alignment.put(sign.rotate(8), 1.25f);
		alignment.put(sign.rotate(3), .75f);
		alignment.put(sign.rotate(9), .75f);

		for(ZodiacSign zs : ZodiacSign.values())
		{
			alignment.putIfAbsent(zs, 1f);
		}
	}

	public float get(ZodiacSign sign)
	{
		return alignment.get(sign);
	}
}
