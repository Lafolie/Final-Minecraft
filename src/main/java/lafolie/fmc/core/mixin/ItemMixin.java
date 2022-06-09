package lafolie.fmc.core.mixin;

import java.util.EnumMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;
import lafolie.fmc.core.internal.elements.ElementalItemTags;
import lafolie.fmc.core.internal.elements.InnateElemental;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

@Mixin(Item.class)
public abstract class ItemMixin implements InnateElemental
{
	// @Inject(at = @At("HEAD"), method = "appendTooltip")
	// private void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo info)
	// {
	// 	if(innateElement != Element.NONE)
	// 	{
	// 		tooltip.add(new TranslatableText(Element.TOOLTIP_TEXT.get(innateElement)));
	// 	}
	// }

	@Shadow
	private RegistryEntry.Reference<Item> registryEntry;

	@Inject(at = @At("TAIL"), method = "<init>")
	private void constructor(Settings settings, CallbackInfo info)
	{

	}

	@Override
	public EnumMap<ElementalAspect, Integer> getInnateElements(ElementalAttribute attribute)
	{
		EnumMap<ElementalAspect, Integer> result = new EnumMap<>(ElementalAspect.class);

		// Items always use resistance for innate elements
		if(attribute != ElementalAttribute.RESISTANCE)
		{
			return result;
		}

		ElementalAspect element = ElementalItemTags.getItemElementalTag(registryEntry);
		if(element != ElementalAspect.NONE)
		{
			result.put(element, 1);
		}
		return result;
	}
}
