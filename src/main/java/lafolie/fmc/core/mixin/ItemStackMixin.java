package lafolie.fmc.core.mixin;

import org.spongepowered.asm.mixin.Mixin;

import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;
import lafolie.fmc.core.elements.ElementalObject;
import lafolie.fmc.core.elements.InnateElementalAspect;

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

	// ------------------------------------------------------------------------
	// ELEMENTALOBJECT INTERFACE
	// ------------------------------------------------------------------------

	@Override
	public void AddElementalAspect(ElementalAspect element, ElementalAttribute attribute)
	{
		AddElement(attribute, element, (byte)1);
	}

	@Override
	public void RemoveElementalAspect(ElementalAspect element, ElementalAttribute attribute)
	{
		AddElement(attribute, element, (byte)-1);
	}

	@Override 
	public boolean HasElementalAspect(ElementalAspect element, ElementalAttribute attribute)
	{
		NbtCompound elements = GetOrCreateElementalNbt(attribute);
		return elements.contains(element.toString());
	}

	@Override
	public int GetElementalAttribute(ElementalAspect element, ElementalAttribute attribute)
	{
		NbtCompound weak = GetOrCreateElementalNbt(attribute);
		String key = element.toString();
		return (int)weak.getByte(key);
	}

	@Override
	public int GetTotalWeakResist(ElementalAspect element)
	{
		return GetElementalAttribute(element, ElementalAttribute.RESISTANCE) - GetElementalAttribute(element, ElementalAttribute.WEAKNESS);
	}

	// ------------------------------------------------------------------------
	// PRIVATE HELPERS
	// ------------------------------------------------------------------------

	/**
	 * Generic method to modify element stores
	 * @param nbtKey
	 * @param element
	 * @param inAmt
	 */
	private void AddElement(ElementalAttribute attribute, ElementalAspect element, byte inAmt)
	{
		String key = element.toString();

		NbtCompound elements = GetOrCreateElementalNbt(attribute);
		if(!elements.contains(key))
		{
			elements.putByte(key, inAmt);
		}
		else
		{
			byte amt = elements.getByte(key);
			amt += inAmt;
			if(amt > (byte)0)
			{
				elements.putByte(key, amt);
			}
			else
			{
				elements.remove(key);
			}
		}
	}

	private NbtCompound GetOrCreateElementalNbt(ElementalAttribute attribute)
	{
		String nbtKey = attribute.toNbtKey();
		ItemStack stack = ((ItemStack)(Object)this);
		NbtCompound nbt = stack.getOrCreateNbt();

		if(!nbt.contains(nbtKey, 10))
		{
			NbtCompound elements = stack.getOrCreateSubNbt(nbtKey);//(NbtCompound)nbt.put(nbtKey, new NbtCompound());

			// ensure that the items innate element is in the list
			if(attribute == ElementalAttribute.RESISTANCE)
			{
				InnateElementalAspect item = (InnateElementalAspect)stack.getItem();
				elements.putByte(item.GetElement().toString(), (byte)1);
			}
		}

		return stack.getSubNbt(nbtKey);
	}
}
