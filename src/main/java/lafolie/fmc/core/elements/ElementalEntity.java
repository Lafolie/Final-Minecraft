package lafolie.fmc.core.elements;

import java.util.List;

import net.minecraft.util.Pair;

/**
 * Adds elemental functionality to the entity.
 */
public interface ElementalEntity
{
	/**
	 * Initialiser, creates the WeakResisTable etc.
	 */
	public void InitElementalEntity();

	/**
	 * Initialiser, creates the WeakResisTable etc.
	 * @param baseWeakResist List of Element/Float pairs to use as base statistics
	 */
	public void InitElementalEntity(List<Pair<ElementalAspect, Float>> baseWeakResist);
	
	
	public WeakResistTable GetWeakResistTable();
}
