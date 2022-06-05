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
import lafolie.fmc.core.elements.ElementalItemTags;
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
	private EnumMap<ElementalAspect, Integer> innateElements;

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

	private void setInnateElements()
	{
		FinalMinecraft.log.info("setInnateElements! {}", registryEntry.toString());
		innateElements = new EnumMap<>(ElementalAspect.class);
		for(Map.Entry<ElementalAspect, TagKey<Item>> entry : ElementalItemTags.TAGS.entrySet())
		{
			FinalMinecraft.log.info("	checking for element {}", entry.getValue().toString());
			if(registryEntry.isIn(ItemTags.BEDS))
			{
				FinalMinecraft.log.debug("Registered {} as {}", registryEntry.toString(), entry.toString());
				// innateElements.put(entry.getKey(), 1);
			}
		}
	}

	@Override
	public EnumMap<ElementalAspect, Integer> getInnateElements()
	{
		FinalMinecraft.log.info("getInnateElements! {}", registryEntry.toString());
		if(innateElements == null)
		{
			setInnateElements();
		}
		return innateElements.clone();
	}
}
