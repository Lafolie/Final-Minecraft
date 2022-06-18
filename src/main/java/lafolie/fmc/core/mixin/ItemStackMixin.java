package lafolie.fmc.core.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;
import lafolie.fmc.core.elements.ElementalEquipment;
import lafolie.fmc.core.elements.ElementalObject;

import lafolie.fmc.core.internal.Components;
import lafolie.fmc.core.internal.elements.ElementalStatsComponent;
import lafolie.fmc.core.internal.elements.InnateElemental;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin 
	implements ElementalObject, ElementalEquipment
{
	@Shadow
	public abstract Item getItem();

	// @Inject(at = @At("TAIL"), method = "<init>*")
	// private void constructor(ItemConvertible item, int count, CallbackInfo info)
	// {
	// 	// addBaseElementalAspect(ElementalAspect.randomElement(), ElementalAttribute.RESISTANCE, 1);
	// }

	@Override
	public ElementalStatsComponent getComponent()
	{	
		ElementalStatsComponent component = Components.ELEMENTAL_STATS_ITEM.get(this);
		
		if(!component.hasInitInnate())
		{
			// add innate elements to the component
			component.setHasInitInnate();
			Item item = getItem();
			InnateElemental innate = (InnateElemental)item;
			for(Map.Entry<ElementalAspect, Integer> entry : innate.getInnateElements(ElementalAttribute.RESISTANCE).entrySet())
			{
				if(item.isDamageable())
				{
					addInnateElementalResistance(entry.getKey(), 2);
				}
				else
				{
					addElementalAspectRaw(entry.getKey(), ElementalAttribute.RESISTANCE, 2);
				}
			}
			// addInnate* requires manual sync
			component.trySync();
		}
		return component;
	}
}