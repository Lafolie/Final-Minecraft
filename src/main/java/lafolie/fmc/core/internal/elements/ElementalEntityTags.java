package lafolie.fmc.core.internal.elements;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ToolItem;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

public class ElementalEntityTags
{
	private static class ElementTagEntry
	{
		public ElementalAspect element;
		public TagKey<EntityType<?>> tag;

		public ElementTagEntry(ElementalAspect element, TagKey<EntityType<?>> tag)
		{
			this.element = element;
			this.tag = tag;
		}
	}

	// these could potentially be combined into a class as above, but it probably uses less memory this way
	private static ListMultimap<RegistryEntry.Reference<EntityType<?>>, ElementalAspect> WEAKRESIST_CACHE = ArrayListMultimap.create();
	private static ListMultimap<RegistryEntry.Reference<EntityType<?>>, ElementalAspect> ABSORB_CACHE = ArrayListMultimap.create();
	private static ListMultimap<RegistryEntry.Reference<EntityType<?>>, ElementalAspect> IMMUNITY_CACHE = ArrayListMultimap.create();
	private static ListMultimap<RegistryEntry.Reference<EntityType<?>>, ElementalAspect> FATAL_CACHE = ArrayListMultimap.create();
	private static ListMultimap<RegistryEntry.Reference<EntityType<?>>, ElementalAspect> REVIVE_CACHE = ArrayListMultimap.create();

	// private static final Map<ElementalAspect, TagKey<EntityType<?>>> TAGS = new EnumMap<>(ElementalAspect.class);
	private static final ListMultimap<ElementalAttribute, ElementTagEntry> TAGS = ArrayListMultimap.create();
	static
	{
		for(ElementalAttribute attribute : ElementalAttribute.values())
		{
			String attributeID = attribute.toString().toLowerCase();

			if(attribute == ElementalAttribute.WEAKNESS)
			{
				continue;
			}

			for(ElementalAspect element : ElementalAspect.values())
			{
				String id;
				if(attribute == ElementalAttribute.RESISTANCE)
				{
					id = String.format("%s_elemental_entities", element.toString().toLowerCase());
				}
				else
				{
					id = String.format("%s_%s_elemental_entities", attributeID, element.toString().toLowerCase());
				}
				TagKey<EntityType<?>> tag = TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier("c", id));
				TAGS.put(attribute, new ElementTagEntry(element, tag));
				FinalMinecraft.log.info("Registered element tag {}", id);
			}
		}
	}

	public static void clearCache()
	{
		WEAKRESIST_CACHE.clear();
		IMMUNITY_CACHE.clear();
		ABSORB_CACHE.clear();
		FATAL_CACHE.clear();
		REVIVE_CACHE.clear();
	}

	private static List<ElementalAspect> findTags(
		RegistryEntry.Reference<EntityType<?>> entityEntry,
		ElementalAttribute attribute)
	{
		List<ElementalAspect> result = new ArrayList<ElementalAspect>();
		List<ElementTagEntry> list = TAGS.get(attribute);

		for(ElementTagEntry entry : list)
		{
			if(entityEntry.isIn(entry.tag))
			{
				result.add(entry.element);
			}
		}
		return result;
	}

	public static List<ElementalAspect> getEntityElementalTags(RegistryEntry.Reference<EntityType<?>> entity, ElementalAttribute attribute)
	{
		switch (attribute)
		{
			case RESISTANCE:
			case WEAKNESS:
				if(!WEAKRESIST_CACHE.containsKey(entity))
				{
					// always use resistance in place of weakness
					List<ElementalAspect> elements = findTags(entity, ElementalAttribute.RESISTANCE);
					WEAKRESIST_CACHE.putAll(entity, elements);
				}
				return WEAKRESIST_CACHE.get(entity);
			case ABSORBTION:
				if(!ABSORB_CACHE.containsKey(entity))
				{
					List<ElementalAspect> elements = findTags(entity, ElementalAttribute.ABSORBTION);
					ABSORB_CACHE.putAll(entity, elements);
				}
				return ABSORB_CACHE.get(entity);
			case IMMUNITY:
				if(!IMMUNITY_CACHE.containsKey(entity))
				{
					List<ElementalAspect> elements = findTags(entity, ElementalAttribute.IMMUNITY);
					IMMUNITY_CACHE.putAll(entity, elements);
				}
				return IMMUNITY_CACHE.get(entity);
			case FATAL:
				if(!FATAL_CACHE.containsKey(entity))
				{
					List<ElementalAspect> elements = findTags(entity, ElementalAttribute.FATAL);
					FATAL_CACHE.putAll(entity, elements);
				}
				return FATAL_CACHE.get(entity);
			case REVIVE:
				if(!REVIVE_CACHE.containsKey(entity))
				{
					List<ElementalAspect> elements = findTags(entity, ElementalAttribute.REVIVE);
					REVIVE_CACHE.putAll(entity, elements);
				}
				return REVIVE_CACHE.get(entity);
			default:
				break;
		}
		return new ArrayList<>();
	}
}