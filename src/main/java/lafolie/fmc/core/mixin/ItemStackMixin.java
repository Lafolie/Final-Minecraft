package lafolie.fmc.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import lafolie.fmc.core.elements.Element;
import lafolie.fmc.core.elements.ElementalNbtProvider;
import lafolie.fmc.core.elements.ElementalObject;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

/**
 * Methods for adding and removing elemental aspects to an item.
 * Element tags are a byte representing the total element modifiers.
 * Tags are removed when the count is 0.
 * Adding/removing elements stack their strengths together, except for absorbtion.
 */
@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ElementalNbtProvider
{
	private static final String ELEMENTAL_WEAKNESS_KEY = "ElementalWeakness";
	private static final String ELEMENTAL_RESISTANCE_KEY = "ElementalResistance";
	private static final String ELEMENTAL_ABSORB_KEY = "ElementalAbsorb";

	@Inject(at = @At("TAIL"), method = "<init>")
	private void Constructor(ItemConvertible item, int count, CallbackInfo info)
	{
	
	}
	// ------------------------------------------------------------------------
	// WEAKNESS
	// ------------------------------------------------------------------------

	/**
	 * Add an elemental weakness to the NBT store.
	 * @param element the element to add
	 */
	@Override
	public void AddElementalWeakness(Element element)
	{
		AddElement(ELEMENTAL_WEAKNESS_KEY, element, (byte)1);
	}

	/**
	 * Remove an elemental weakness to the NBT store.
	 * @param element the element to remove
	 */
	@Override
	public void RemoveElementalWeakness(Element element)
	{
		AddElement(ELEMENTAL_WEAKNESS_KEY, element, (byte)-1);
	}

	/**
	 * Check whether an elemental weakness is in the NBT store.
	 * @param element the element to check
	 * @return true if the element is present
	 */
	@Override 
	public boolean HasElementalWeakness(Element element)
	{
		NbtCompound elements = GetOrCreateElementalNbt(ELEMENTAL_WEAKNESS_KEY);
		return elements.contains(element.toString());
	}

	// ------------------------------------------------------------------------
	// RESISTANCE
	// ------------------------------------------------------------------------

	/**
	 * Add an elemental weakness to the NBT store.
	 * @param element the element to add
	 */
	@Override
	public void AddElementalResistance(Element element)
	{
		AddElement(ELEMENTAL_RESISTANCE_KEY, element, (byte)1);
	}

	/**
	 * Remove an elemental weakness to the NBT store.
	 * @param element the element to remove
	 */
	@Override
	public void RemoveElementalResistance(Element element)
	{
		AddElement(ELEMENTAL_RESISTANCE_KEY, element, (byte)-1);
	}

	/**
	 * Check whether an elemental weakness is in the NBT store.
	 * @param element the element to check
	 * @return true if the element is present
	 */
	@Override 
	public boolean HasElementalResistance(Element element)
	{
		NbtCompound elements = GetOrCreateElementalNbt(ELEMENTAL_RESISTANCE_KEY);
		return elements.contains(element.toString());
	}

	// ------------------------------------------------------------------------
	// ABSORBTION
	// ------------------------------------------------------------------------

	/**
	 * Add an elemental weakness to the NBT store.
	 * @param element the element to add
	 */
	@Override
	public void AddElementalAbsorbtion(Element element)
	{
		AddElement(ELEMENTAL_ABSORB_KEY, element, (byte)1);
	}

	/**
	 * Remove an elemental weakness to the NBT store.
	 * @param element the element to remove
	 */
	@Override
	public void RemoveElementalAbsorbtion(Element element)
	{
		AddElement(ELEMENTAL_ABSORB_KEY, element, (byte)-1);
	}

	/**
	 * Check whether an elemental weakness is in the NBT store.
	 * @param element the element to check
	 * @return true if the element is present
	 */
	@Override 
	public boolean HasElementalAbsorbtion(Element element)
	{
		NbtCompound elements = GetOrCreateElementalNbt(ELEMENTAL_ABSORB_KEY);
		return elements.contains(element.toString());
	}

	// ------------------------------------------------------------------------
	// TOTAL
	// ------------------------------------------------------------------------

	/**
	 * Gets the total weak/resist amount for the specified element
	 * @param element the element to calculate
	 * @return the total weak/resist value
	 */
	@Override
	public int GetTotalWeakResist(Element element)
	{
		NbtCompound weak = GetOrCreateElementalNbt(ELEMENTAL_WEAKNESS_KEY);
		NbtCompound resist = GetOrCreateElementalNbt(ELEMENTAL_RESISTANCE_KEY);
		String key = element.toString();
		return (int)(resist.getByte(key) - weak.getByte(key));
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
	private void AddElement(String nbtKey, Element element, byte inAmt)
	{
		String key = element.toString();

		NbtCompound elements = GetOrCreateElementalNbt(nbtKey);
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

	private NbtCompound GetOrCreateElementalNbt(String nbtKey)
	{
		ItemStack stack = ((ItemStack)(Object)this);
		NbtCompound nbt = stack.getOrCreateNbt();

		if(!nbt.contains(ELEMENTAL_RESISTANCE_KEY, 10))
		{
			NbtCompound elements = (NbtCompound)nbt.put(nbtKey, new NbtCompound());

			// ensure that the items innate element is in the list
			ElementalObject item = (ElementalObject)stack.getItem();
			elements.putByte(item.GetElement().toString(), (byte)1);
		}

		return stack.getSubNbt(nbtKey);
	}
}
