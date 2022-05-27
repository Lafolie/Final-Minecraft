package lafolie.fmc.core.elements;

import java.util.HashMap;
import java.util.List;

import lafolie.fmc.core.elements.ElementalAspect;
import net.minecraft.util.Pair;

public class WeakResistTable
{
	private HashMap<ElementalAspect, Float> weakResist = new HashMap<ElementalAspect, Float>();

	/**
	 * Base constructor.
	 */
	public WeakResistTable()
	{
		//hashmap should always be populated
		for(ElementalAspect element : ElementalAspect.values())
		{
			weakResist.put(element, 1f);
		}
	}

	/**
	 * Constructs a table with base weakness/resistances.
	 * @param baseElements list of weak/resist Pairs
	 */
	public WeakResistTable(List<Pair<ElementalAspect, Float>> baseElements)
	{
		for(Pair<ElementalAspect, Float> pair : baseElements)
		{
			weakResist.put(pair.getLeft(), pair.getRight());
		}

		for(ElementalAspect element : ElementalAspect.values())
		{
			weakResist.put(element, 1f);
		}
	}

	//Adds an elemental weakness/resistance of the specified value
	public void AddElement(ElementalAspect element, float value)
	{
		float current = weakResist.get(element);
		weakResist.replace(element, current + value);
	}

	//Adds an elemental weakness/resistance using the default value (0.2f)
	public void AddElement(ElementalAspect element)
	{
		AddElement(element, 0.2f);
	}

	//Removes an elemental weakness/resistance of the specified value
	public void RemoveElement(ElementalAspect element, float value)
	{
		float current = weakResist.get(element);
		weakResist.replace(element, current - value);
	}

	//Removes an elemental weakness/resistance using the default value (0.2f)
	public void RemoveElement(ElementalAspect element)
	{
		RemoveElement(element, 0.2f);
	}

	public String PrintElement(ElementalAspect el)
	{
		return String.format("%s - %f", el.toString(), weakResist.get(el));
	}
}
