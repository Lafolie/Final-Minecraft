package lafolie.fmc.core;

import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;
import lafolie.fmc.core.elements.ElementalObject;
import lafolie.fmc.core.elements.Utils;
import lafolie.fmc.core.mixin.ItemStackMixin;
import lafolie.fmc.core.particles.Particles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class FinalMinecraftClient implements ClientModInitializer
{

	@Override
	public void onInitializeClient()
	{
		InitContent();

		ItemTooltipCallback.EVENT.register((stack, context, lines) -> 
		{
			//read NBT and stuff
			ElementalObject elStack = (ElementalObject)(Object)stack;
			
			for(ElementalAspect element : ElementalAspect.values())
			{
				int total = elStack.getWeakResistAffinity(element);
				if(total > 0)
				{
					lines.add(
						new TranslatableText("fmc.core.element.tooltip.elemental",
						new TranslatableText(Utils.ElementLangKeys.get(element))));
						lines.add(new LiteralText(String.format("Total: %s", total))); //ALSO REMOVE LATER
				}

			}
		});
	}
	
	private void InitContent()
	{
		Particles.initClient();
	}
}
