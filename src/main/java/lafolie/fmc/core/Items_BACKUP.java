package lafolie.fmc.core;

import lafolie.fmc.core.util.FMCIdentifier;
// #<item_import_package>

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public final class Items_BACKUP
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
