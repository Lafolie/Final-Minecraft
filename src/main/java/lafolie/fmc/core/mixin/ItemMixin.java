package lafolie.fmc.core.mixin;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lafolie.fmc.core.elements.Element;
import lafolie.fmc.core.elements.ElementalObject;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.Settings;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

@Mixin(Item.class)
public abstract class ItemMixin implements ElementalObject
{
	private Element innateElement = Element.EARTH;

	@Override
	public Element GetElement()
	{
		return innateElement;
	}


	@Inject(at = @At("HEAD"), method = "appendTooltip")
	private void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo info)
	{
		if(innateElement != Element.NONE)
		{
			tooltip.add(new TranslatableText(Element.TOOLTIP_TEXT.get(innateElement)));
		}
	}

	@Inject(at = @At("TAIL"), method = "<init>")
	private void Constructor(Settings settings, CallbackInfo info)
	{

	}
}
