package lafolie.fmc.core.chrono;

import java.util.HashMap;
import java.util.Map;

/**
 * Period of time within a day. See
 * {@link}https://en.wikipedia.org/wiki/Dayparting
 */
public enum Daypart
{
	MORNING(0L),
	DAY(1000L),
	NOON(6000L),
	AFTERNOON(9000L),
	SUNSET(12000L),
	NIGHT(13000L),
	MIDNIGHT(18000L),
	SUNRISE(23000L);

	public final long ticks;
	private static Map<Long, Daypart> TICKS = new HashMap<>();
	
	static
	{
		TICKS.put(0L,     MORNING  );
		TICKS.put(1600L,  DAY      );
		TICKS.put(6000L,  NOON     );
		TICKS.put(9000L,  AFTERNOON);
		TICKS.put(12000L, SUNSET   );
		TICKS.put(13000L, NIGHT    );
		TICKS.put(18000L, MIDNIGHT );
		TICKS.put(23000L, SUNRISE  );
	}

	private Daypart(long ticks)
	{
		this.ticks = ticks;
	}

	public static Daypart fromTicks(long ticks)
	{
		long t = Math.floorMod(ticks, 23000L);
		long previous = 0;
		for(Long i : TICKS.keySet())
		{
			if(i > t)
			{
				return TICKS.get(previous);
			}
			previous = i;
		}
		return MORNING;
	}
}
