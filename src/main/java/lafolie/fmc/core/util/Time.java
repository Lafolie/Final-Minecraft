package lafolie.fmc.core.util;

import java.util.Locale;

public final class Time
{
	public static final int SECONDS_PER_HOUR = 3600;

	public static String formatSeconds(int time)
	{
		return String.format(Locale.ROOT, "%02d:%02d", time / 60, time % 60);
	}
}
