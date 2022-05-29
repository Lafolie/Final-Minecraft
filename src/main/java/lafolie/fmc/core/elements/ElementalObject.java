package lafolie.fmc.core.elements;

public interface ElementalObject
{
	public void AddElementalAspect(ElementalAspect element, ElementalAttribute attribute);
	public void RemoveElementalAspect(ElementalAspect element, ElementalAttribute attribute);
	public boolean HasElementalAspect(ElementalAspect element, ElementalAttribute attribute);
	public int GetElementalAttribute(ElementalAspect element, ElementalAttribute attribute);
	
	public int GetElementalAffinity(ElementalAspect element);
}
