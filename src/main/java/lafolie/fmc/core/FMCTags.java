package lafolie.fmc.core;


import lafolie.fmc.core.util.FMCIdentifier;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public final class FMCTags
{
	public static TagKey<Item> CRYSTAL_ITEMS = registerItem("crystal_items");
	public static TagKey<Item> CRYSTAL_BLOCK_ITEMS = registerItem("crystal_block_items");

	public static TagKey<Block> CRYSTAL_BLOCKS = registerBlock("crystal_blocks");

	private static TagKey<Item> registerItem(String id)
	{
		return TagKey.of(Registry.ITEM_KEY, FMCIdentifier.contentID(id));
	}

	private static TagKey<Block> registerBlock(String id)
	{
		return TagKey.of(Registry.BLOCK_KEY, FMCIdentifier.contentID(id));
	}

	// private static TagKey<EntityType> registerEntity(String id)
	// {
	// 	return TagKey.of(Registry.ENTITY_TYPE_KEY, FMCIdentifier.contentID(id));
	// }
}
