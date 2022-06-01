package lafolie.fmc.core.mixin;

import org.spongepowered.asm.mixin.Mixin;

import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;
import lafolie.fmc.core.elements.ElementalObject;

import lafolie.fmc.core.internal.Components;
import lafolie.fmc.core.internal.elements.ElementalStats_Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

/**
 * Methods for adding and removing elemental aspects to an item.
 * Element tags are a byte representing the total element modifiers.
 * Tags are removed when the count is 0.
 * Adding/removing elements stack their strengths together, except for absorbtion.
 * An item is considered to be 'X Elemental' when the total weak/resist is >0.
 */
@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ElementalObject
{
	// @Inject(at = @At("TAIL"), method = "<init>")
	// private void Constructor(ItemConvertible item, int count, CallbackInfo info)
	// {
		
	// }
	private ElementalStats_Item GetComponent()
	{
		return Components.ELEMENTAL_STATS_ITEM.get(this);
	}

	// ------------------------------------------------------------------------
	// ELEMENTALOBJECT INTERFACE
	// ------------------------------------------------------------------------

	/**
	 * Adds a base elemental aspect to the item.
	 * 
	 * @param element
	 * @param attribute
	 * @param amount the amount to add, must be positive!
	 */
	@Override
	public void AddBaseElementalAspect(ElementalAspect element, ElementalAttribute attribute, int amount)
	{
		assert amount > 0: "Base ElementalAspects must be >0!"; //TODO: replace this with try/catch
		GetComponent().AddElement(element, attribute, (byte)amount);
	}

	/**
	 * Adds an elememental aspect to the item.
	 * If the attribute is WEAKNESS or RESISTANCE, the corresponding element's RESISTANCE/WEAKNESS
	 * will increase as well.
	 * For example, setting a FIRE RESISTANCE will increase WATER WEAKNESS
	 */
	@Override
	public void AddElementalAspect(ElementalAspect element, ElementalAttribute attribute)
	{
		ElementalStats_Item stats = GetComponent();
		stats.AddElement(element, attribute, (byte)1);
		if(attribute == ElementalAttribute.WEAKNESS)
		{
			stats.AddElement(element.GetStrongTo(), ElementalAttribute.RESISTANCE, (byte)1);
		}
		else if(attribute == ElementalAttribute.RESISTANCE)
		{
			stats.AddElement(element.GetWeakTo(), ElementalAttribute.WEAKNESS, (byte)1);
		}
	}

	/**
	 * Removes an elememental aspect to the item.
	 * If the attribute is WEAKNESS or RESISTANCE, the corresponding element's RESISTANCE/WEAKNESS
	 * will increase as well.
	 * For example, removing a FIRE RESISTANCE will lower WATER WEAKNESS
	 * An element's tag will be removed if the value drops to 0.
	 */
	@Override
	public void RemoveElementalAspect(ElementalAspect element, ElementalAttribute attribute)
	{
		ElementalStats_Item stats = GetComponent();
		stats.AddElement(element, attribute, (byte)-1);
		if(attribute == ElementalAttribute.WEAKNESS)
		{
			stats.AddElement(element.GetStrongTo(), ElementalAttribute.RESISTANCE, (byte)-1);
		}
		else if(attribute == ElementalAttribute.RESISTANCE)
		{
			stats.AddElement(element.GetWeakTo(), ElementalAttribute.WEAKNESS, (byte)-1);
		}
	}

	@Override 
	public boolean HasElementalAspect(ElementalAspect element, ElementalAttribute attribute)
	{
		NbtCompound elements = GetComponent().GetOrCreateElementalNbt(attribute);
		return elements.contains(element.toString());
	}

	@Override
	public int GetElementalAttribute(ElementalAspect element, ElementalAttribute attribute)
	{
		NbtCompound nbt = GetComponent().GetOrCreateElementalNbt(attribute);
		String key = element.toString();
		return (int)nbt.getByte(key);
	}

	@Override
	public int GetElementalAffinity(ElementalAspect element)
	{
		return GetElementalAttribute(element, ElementalAttribute.RESISTANCE) - GetElementalAttribute(element, ElementalAttribute.WEAKNESS);
	}
}
