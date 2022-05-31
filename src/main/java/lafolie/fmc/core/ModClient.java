package lafolie.fmc.core;

import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalObject;
import lafolie.fmc.core.elements.ElementalObjectUtils;
import lafolie.fmc.core.mixin.ItemStackMixin;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;

public class ModClient implements ClientModInitializer
{

	@Override
	public void onInitializeClient()
	{
		ItemTooltipCallback.EVENT.register((stack, context, lines) -> 
		{
			//read NBT and stuff
			ElementalObject elStack = (ElementalObject)(Object)stack;
			
			for(ElementalAspect element : ElementalAspect.values())
			{
				int total = elStack.GetElementalAffinity(element);
				if(total > 0)
				{
				lines.add(
					new TranslatableText("fmc.core.element.tooltip.elemental",
					new TranslatableText(ElementalObjectUtils.ElementLangKeys.get(element))));
				}
			}
		});
	}
	
}