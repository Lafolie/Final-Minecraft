package lafolie.fmc.core.mixin;

import java.util.EnumMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;
import lafolie.fmc.core.elements.ElementalObject;

import lafolie.fmc.core.internal.Components;
import lafolie.fmc.core.internal.elements.ElementalStatsComponent;
import lafolie.fmc.core.internal.elements.InnateElemental;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ElementalObject
{
	@Shadow
	public abstract Item getItem();

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
			Item item = this.getItem();
			InnateElemental innate = (InnateElemental)item;
			for(Map.Entry<ElementalAspect, Integer> entry : innate.getInnateElements().entrySet())
			{
				addBaseElementalAspect(entry.getKey(), ElementalAttribute.RESISTANCE, entry.getValue());
			}
			// addBaseElementalAspect(ElementalAspect.randomElement(), ElementalAttribute.RESISTANCE, 1);

		}
		return Components.ELEMENTAL_STATS_ITEM.get(this);
	}
}