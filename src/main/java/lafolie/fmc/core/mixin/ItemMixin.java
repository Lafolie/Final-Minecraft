package lafolie.fmc.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.InnateElementalAspect;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;

@Mixin(Item.class)
public abstract class ItemMixin implements InnateElementalAspect
{
	private ElementalAspect innateElement = ElementalAspect.randomElement();

	@Override
	public ElementalAspect getElement()
	{
		return innateElement;
	}


	// @Inject(at = @At("HEAD"), method = "appendTooltip")
	// private void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo info)
	// {
	// 	if(innateElement != Element.NONE)
	// 	{
	// 		tooltip.add(new TranslatableText(Element.TOOLTIP_TEXT.get(innateElement)));
	// 	}
	// }

	@Inject(at = @At("TAIL"), method = "<init>")
	private void constructor(Settings settings, CallbackInfo info)
	{

	}
}
