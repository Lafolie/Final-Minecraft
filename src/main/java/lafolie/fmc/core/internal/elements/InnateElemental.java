package lafolie.fmc.core.internal.elements;

import java.util.Map;

import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;

public interface InnateElemental
{
	public Map<ElementalAspect, Integer> getInnateElements(ElementalAttribute attribute);
}
