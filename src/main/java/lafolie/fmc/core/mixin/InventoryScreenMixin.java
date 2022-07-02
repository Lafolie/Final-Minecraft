package lafolie.fmc.core.mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;
import lafolie.fmc.core.elements.ElementalObject;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler>
{
	public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
		//TODO Auto-generated constructor stub
	}

	@Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V")
	private void renderInject(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo info)
	{
		//26,8		74, 77
		int minX = x + 26;
		int minY = y + 8;
		// int maxX = x + 74;
		// int maxY = y + 77;
		if(isPointWithinBounds(minX, minY, 74, 77, mouseX, mouseY))
		{
			renderElementalAffinityTooltip(matrices, mouseX, mouseY);
		}
	}

	private void renderElementalAffinityTooltip(MatrixStack matrices, int mouseX, int mouseY)
	{
		List<Text> lines = new ArrayList<>();
		ElementalObject player = (ElementalObject)client.player;
		float i = FinalMinecraft.getConfig().defaultWeakResistAmount * 100;
		for(ElementalAspect element : ElementalAspect.values())
		{
			if(element == ElementalAspect.NONE)
			{
				lines.add(new TranslatableText("fmc.core.element.tooltip.affinities"));
				continue;
			}

			ElementalAttribute attribute =  player.getAttributeForDamage(element);
			switch(attribute)
			{
				case WEAKNESS:
					lines.add(new TranslatableText(element.getLangKey()).append(String.format(": -%2.0f%%", player.getWeakResistAffinity(element) * i)));
					break;

				case RESISTANCE:
					lines.add(new TranslatableText(element.getLangKey()).append(String.format(": %2.0f%%", player.getWeakResistAffinity(element) * i)));
					break;

				case IMMUNITY:
					lines.add(new TranslatableText(element.getLangKey()).append(": ").append(new TranslatableText("fmc.core.attributes.tooltip.immune")));
					break;

				case ABSORBTION:
					lines.add(new TranslatableText(element.getLangKey()).append(": ").append(new TranslatableText("fmc.core.attributes.tooltip.absorb")));
					break;

				case FATAL:
					lines.add(new TranslatableText(element.getLangKey()).append(": ").append(new TranslatableText("fmc.core.attributes.tooltip.fatal")));
					break;

				case REVIVE:
					lines.add(new TranslatableText(element.getLangKey()).append(": ").append(new TranslatableText("fmc.core.attributes.tooltip.revive")));
					break;

				default:
					lines.add(new TranslatableText(element.getLangKey()).append(": 0%"));

			}
		}
		
		this.renderTooltip(matrices, lines, Optional.empty(), mouseX, mouseY);
	}
	
}
