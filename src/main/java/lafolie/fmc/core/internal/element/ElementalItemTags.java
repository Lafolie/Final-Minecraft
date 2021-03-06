package lafolie.fmc.core.internal.element;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import lafolie.fmc.core.element.ElementalAspect;
import lafolie.fmc.core.util.FMCIdentifier;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

public class ElementalItemTags
{
	private static Map<RegistryEntry.Reference<Item>, ElementalAspect> CACHE = new HashMap<>();
	private static final Map<ElementalAspect, TagKey<Item>> TAGS = new EnumMap<>(ElementalAspect.class);
	
	static
	{
		for(ElementalAspect element : ElementalAspect.values())
		{
			String id = element.toString().toLowerCase(Locale.ROOT).concat("_elemental_items");
			TAGS.put(element, TagKey.of(Registry.ITEM_KEY, FMCIdentifier.commonTagID(id)));
		}
	}

	private static Optional<ElementalAspect> findTag(RegistryEntry.Reference<Item> itemEntry)
	{
		// we use Optional because tags are not loaded until the server is started
		// TODO: REDO THIS TO NOT USE OPTIONAL
		for(Map.Entry<ElementalAspect, TagKey<Item>> entry : TAGS.entrySet())
		{
			if(itemEntry.isIn(entry.getValue()))
			{
				return Optional.of(entry.getKey());
			}
		}
		return Optional.empty();
	}

	public static void clearCache()
	{
		CACHE.clear();
	}

	public static ElementalAspect getItemElementalTag(RegistryEntry.Reference<Item> itemEntry)
	{
		if(!CACHE.containsKey(itemEntry))
		{
			Optional<ElementalAspect> result = findTag(itemEntry);
			if(result.isPresent())
			{
				// datapacks are loaded, so use the cache from hereon
				CACHE.put(itemEntry, result.get());
			}
			else
			{
				// datapacks not loaded, so return none
				return ElementalAspect.NONE;
			}
		}
		return CACHE.get(itemEntry);
	}
}
