package lafolie.fmc.core;

import lafolie.fmc.core.util.FMCIdentifier;
// #<item_import_package>

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

public final class Items
{
	// #<item_instance>

	public static final ItemGroup FMC_ITEMS = FabricItemGroupBuilder
		.create(FMCIdentifier.contentID("general"))
		.icon(() -> new ItemStack(Blocks.LARGE_AMETHYST_BUD))
		.appendItems(stacks ->
		{
			// #<item_group>
			stacks.add(ItemStack.EMPTY);
		})
		.build();
		
	public void init()
	{
		// #<item_register>
		
	}
}