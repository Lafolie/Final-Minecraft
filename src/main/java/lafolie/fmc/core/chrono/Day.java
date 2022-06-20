package lafolie.fmc.core.chrono;

import java.util.ArrayList;
import java.util.List;

import lafolie.fmc.core.elements.ElementalAspect;

/**
 * Days of the week.
 * Naming scheme: elements.
 */
public enum Day
{
	FIRESDAY,
	ICEDAY,
	THUNDERSDAY,
	WINDSDAY,
	WATERSDAY,
	EARTHSDAY,
	HOLYDAY,
	DARKSDAY;

	private static List<Day> ORDS = new ArrayList<>();

	static
	{
		for(Day day : Day.values())
		{
			ORDS.add(day);
		}
	}

	public static Day fromOrdinal(int ord)
	{
		return ORDS.get(ord);
	}

	public static ElementalAspect getElementalAspect(Day day)
	{
		return ElementalAspect.fromOrdinal(day.ordinal());
	}

	public ElementalAspect getElementalAspect()
	{
		return ElementalAspect.fromOrdinal(this.ordinal() + 1);
	}
}
