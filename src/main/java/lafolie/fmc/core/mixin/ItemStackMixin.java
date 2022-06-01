package lafolie.fmc.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;
import lafolie.fmc.core.elements.ElementalObject;

import lafolie.fmc.core.internal.Components;
import lafolie.fmc.core.internal.elements.ElementalStatsComponent;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;


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
	@Inject(at = @At("TAIL"), method = "<init>*")
	private void constructor(ItemConvertible item, int count, CallbackInfo info)
	{
		// addBaseElementalAspect(ElementalAspect.randomElement(), ElementalAttribute.RESISTANCE, 1);
	}

	private boolean hasInitComponent = false;

	@Override
	public ElementalStatsComponent getComponent()
	{	
		if(!hasInitComponent)
		{
			hasInitComponent = true;
			addBaseElementalAspect(ElementalAspect.randomElement(), ElementalAttribute.RESISTANCE, 1);
		}
		return Components.ELEMENTAL_STATS_ITEM.get(this);
	}
}