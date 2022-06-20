package lafolie.fmc.core.chrono;

import net.minecraft.util.math.MathHelper;

/**
 * Represents in-game time. Time is measured in server ticks, not
 * real-world seconds.
 */
public class DateTime
{
	private static final float TICKS_PER_SECOND = 0.27f;
	private static final float TICKS_PER_MINUTE = 16.6f;
	private static final float TICKS_PER_HOUR = 1000f;
	private static final int TICKS_PER_DAY = 24000;
	private static final int SIX_AM_OFFSET = 6000;

	public static final int DAYS_PER_WEEK = 8;
	public static final int WEEKS_PER_MONTH = 4;
	public static final int MONTHS_PER_YEAR = 12;

	public static final int DAYS_PER_MONTH = DAYS_PER_WEEK * WEEKS_PER_MONTH;
	public static final int DAYS_PER_YEAR = DAYS_PER_MONTH * MONTHS_PER_YEAR;
	public static final int WEEKS_PER_YEAR = WEEKS_PER_MONTH * MONTHS_PER_YEAR;
	public static final int INITIAL_YEAR = 1000;

	private long ticks;
	private int daysPassed = -1;
	private int dayTime;
	private Day day;
	private Month month;
	private int week;
	private int year;

	/**
	 * Create a DateTime initialised to
	 * 01/01/1000
	 */
	public DateTime()
	{
		setTime(0);
	}

	/**
	 * Create a DateTime with the given number of ticks.
	 * @param time
	 */
	public DateTime(long time)
	{
		setTime(time);
	}

	/**
	 * Create a DateTime based on the given date.
	 * Note: the year should be at least 1000
	 */
	public DateTime(int dayOfTheMonth, int month, int year)
	{
		long time = dayOfTheMonth * TICKS_PER_DAY;
		time += month * TICKS_PER_DAY * DAYS_PER_MONTH;
		time += (year - INITIAL_YEAR) * TICKS_PER_DAY * DAYS_PER_YEAR;
		setTime(time);
	}

	/**
	 * Create a DateTime based on the given date.
	 * Note: the year should be at least 1000
	 */
	public DateTime(int dayOfTheMonth, Month month, int year)
	{
		this(dayOfTheMonth, month.ordinal(), year);
	}

	/**
	 * Create a DateTime based on the given date,
	 * with the year set to 1000.
	 */
	public DateTime(int dayOfTheMonth, int month)
	{
		this(dayOfTheMonth, month, 1000);
	}

	/**
	 * Create a DateTime based on the given date,
	 * with the year set to 1000.
	 */
	public DateTime(int dayOfTheMonth, Month month)
	{
		this(dayOfTheMonth, month.ordinal(), 1000);
	}

	/**
	 * Create a copy of a DateTime.
	 */
	public DateTime clone()
	{
		return new DateTime(ticks);
	}

	/**
	 * Set the time, measured in server ticks.
	 * @param time
	 */
	public void setTime(long time)
	{
		ticks = time;
		dayTime = (Math.floorMod(ticks, TICKS_PER_DAY) + SIX_AM_OFFSET) % TICKS_PER_DAY;
		
		int days = (int)Math.floorDiv(ticks, TICKS_PER_DAY);
		if(days != daysPassed)
		{
			daysPassed = days;
			day = Day.fromOrdinal(daysPassed % DAYS_PER_WEEK);
			week = MathHelper.floorDiv(daysPassed, DAYS_PER_WEEK) % WEEKS_PER_YEAR;
			month = Month.fromOrdinal(Math.floorDiv(daysPassed, DAYS_PER_MONTH) % MONTHS_PER_YEAR);
			year = MathHelper.floorDiv(daysPassed, DAYS_PER_YEAR) + INITIAL_YEAR;
		}

	}

	/**
	 * Find the difference between two DateTimes.
	 * @param other
	 * @return
	 */
	public DateTime differenceBetween(DateTime other)
	{
		return new DateTime(Math.abs(ticks - other.getTicks()));
	}

	/**
	 * Check whether two DateTimes are equal.
	 * @param other
	 * @return
	 */
	public boolean areEqual(DateTime other)
	{
		return ticks == other.getTicks();
	}

	/**
	 * Check whether the day & month matches. Does not take year into account.
	 * @param other
	 * @return
	 */
	public boolean dateMatches(DateTime other)
	{
		return getDayOfTheMonth() == other.getDayOfTheMonth() && month == other.getMonth();
	}

	/**
	 * Get the raw number of ticks passed.
	 * @return
	 */
	public long getTicks()
	{
		return ticks;
	}

	public String getTimeString(boolean use12Hour)
	{
		int hour = use12Hour ? getHour() % 12 : getHour();
		return String.format("%02d:%02d", hour, getMinute());
		
	}

	public String getDateString()
	{
		int date = getDayOfTheMonth();
		String suffix;

		int mod100 = date % 100;
		if(mod100 == 11 || mod100 == 12 || mod100 == 13)
		{
			suffix = "th";
		}
		else
		{
			switch(date % 10)
			{
				case 1:
					suffix = "st";
					break;
	
				case 2:
					suffix = "nd";
					break;
	
				case 3:
					suffix = "rd";
					break;
	
				default:
					suffix = "th";
			}
		}
		return String.format("%s, %d%s of %s %d", day.toString(), date, suffix, month.toString(), year);
	}

	public int getDayOfTheMonth()
	{
		return (week % 4) * DAYS_PER_WEEK + day.ordinal() + 1;
	}

	public int getDaysPassed()
	{
		return daysPassed;
	}

	public Day getDay()
	{
		return day;
	}

	public Month getMonth()
	{
		return month;
	}

	public int getYear()
	{
		return year;
	}

	public int getSecond()
	{
		return MathHelper.floor(dayTime / TICKS_PER_SECOND) % 60;
	}

	public int getMinute()
	{
		return MathHelper.floor(dayTime / TICKS_PER_MINUTE) % 60;
	}

	public int getHour()
	{
		return MathHelper.floor(dayTime / TICKS_PER_HOUR);
	}

	public Daypart getDaypart()
	{
		return Daypart.fromTicks(ticks);
	}
}
