package lafolie.fmc.core.zodiac;

import java.util.EnumMap;
import java.util.Map;

public class ZodiacAlignment
{
	private final Map<ZodiacSign, Float> alignment = new EnumMap<>(ZodiacSign.class);

	public ZodiacAlignment(ZodiacSign sign)
	{
		alignment.put(sign.rotate(6), 1.3f);
		alignment.put(sign.rotate(4), 1.15f);
		alignment.put(sign.rotate(8), 1.15f);
		alignment.put(sign.rotate(3), .85f);
		alignment.put(sign.rotate(9), .85f);

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
