package lafolie.fmc.core.elements;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;

public class ElementalAffinityMap
{
	private EnumMap<ElementalAspect, Integer> affinities;

	public ElementalAffinityMap()
	{
		affinities = new EnumMap<>(ElementalAspect.class);

	}
}
