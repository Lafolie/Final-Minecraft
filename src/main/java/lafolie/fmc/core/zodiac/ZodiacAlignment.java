package lafolie.fmc.core.zodiac;

import java.util.EnumMap;
import java.util.Map;

public class ZodiacAlignment
{
	private final Map<ZodiacSign, Float> alignment = new EnumMap<>(ZodiacSign.class);

	public ZodiacAlignment(ZodiacSign sign)
	{
		alignment.put(sign.rotate(6), 4f);
		alignment.put(sign.rotate(4), 2f);
		alignment.put(sign.rotate(8), 2f);
		alignment.put(sign.rotate(3), .5f);
		alignment.put(sign.rotate(9), .5f);

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
