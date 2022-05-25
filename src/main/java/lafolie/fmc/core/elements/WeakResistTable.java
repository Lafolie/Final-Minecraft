package lafolie.fmc.core.elements;

import java.util.HashMap;
import java.util.List;

import lafolie.fmc.core.elements.Element;
import net.minecraft.util.Pair;

public class WeakResistTable
{
	private HashMap<Element, Float> weakResist = new HashMap<Element, Float>();

	/**
	 * Base constructor.
	 */
	public WeakResistTable()
	{
		//hashmap should always be populated
		for(Element element : Element.values())
		{
			weakResist.put(element, 1f);
		}
	}

	/**
	 * Constructs a table with base weakness/resistances.
	 * @param baseElements list of weak/resist Pairs
	 */
	public WeakResistTable(List<Pair<Element, Float>> baseElements)
	{
		for(Pair<Element, Float> pair : baseElements)
		{
			weakResist.put(pair.getLeft(), pair.getRight());
		}

		for(Element element : Element.values())
		{
			weakResist.put(element, 1f);
		}
	}

	//Adds an elemental weakness/resistance of the specified value
	public void AddElement(Element element, float value)
	{
		float current = weakResist.get(element);
		weakResist.replace(element, current + value);
	}

	//Adds an elemental weakness/resistance using the default value (0.2f)
	public void AddElement(Element element)
	{
		AddElement(element, 0.2f);
	}

	//Removes an elemental weakness/resistance of the specified value
	public void RemoveElement(Element element, float value)
	{
		float current = weakResist.get(element);
		weakResist.replace(element, current - value);
	}

	//Removes an elemental weakness/resistance using the default value (0.2f)
	public void RemoveElement(Element element)
	{
		RemoveElement(element, 0.2f);
	}

	public String PrintElement(Element el)
	{
		return String.format("%s - %f", el.toString(), weakResist.get(el));
	}
}
