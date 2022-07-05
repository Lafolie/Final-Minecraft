package lafolie.fmc.core.internal.element;

import java.util.Map;

import lafolie.fmc.core.element.ElementalAspect;
import lafolie.fmc.core.element.ElementalAttribute;

public interface InnateElemental
{
	public Map<ElementalAspect, Integer> getInnateElements(ElementalAttribute attribute);
}
