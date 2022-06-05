package lafolie.fmc.core.elements;

import java.util.EnumMap;

import lafolie.fmc.core.FinalMinecraft;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ElementalItemTags
{
	public static final EnumMap<ElementalAspect, TagKey<Item>> TAGS = new EnumMap<>(ElementalAspect.class);

	static
	{
		for(ElementalAspect element : ElementalAspect.values())
		{
			String id = element.toString().toLowerCase().concat("_elemental");
			TAGS.put(element, TagKey.of(Registry.ITEM_KEY, new Identifier("c", id)));
			FinalMinecraft.log.info("Registered element {}", id);
		}
	}
}
