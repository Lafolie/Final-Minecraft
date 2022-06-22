package lafolie.fmc.core;

import lafolie.fmc.core.util.FMCIdentifier;

import java.util.ArrayList;

import lafolie.fmc.core.item.CrystalShardItem;
import lafolie.fmc.core.item.CrystalItem;










// #<item_import_package>

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public final class FMCItems
{
	public static final CrystalShardItem CRYSTAL_SHARD = new CrystalShardItem(new FabricItemSettings());
	public static final CrystalItem CRYSTAL = new CrystalItem(new FabricItemSettings());
	public static final Item FIRE_CRYSTAL = new Item(new FabricItemSettings());
	public static final Item ICE_CRYSTAL = new Item(new FabricItemSettings());
	public static final Item THUNDER_CRYSTAL = new Item(new FabricItemSettings());
	public static final Item WIND_CRYSTAL = new Item(new FabricItemSettings());
	public static final Item WATER_CRYSTAL = new Item(new FabricItemSettings());
	public static final Item EARTH_CRYSTAL = new Item(new FabricItemSettings());
	public static final Item HOLY_CRYSTAL = new Item(new FabricItemSettings());
	public static final Item DARK_CRYSTAL = new Item(new FabricItemSettings());
	public static final Item POISON_CRYSTAL = new Item(new FabricItemSettings());
	public static final Item GRAVITY_CRYSTAL = new Item(new FabricItemSettings());
	// #<item_instance>

	public static final ItemGroup FMC_ITEMS = FabricItemGroupBuilder
		.create(FMCIdentifier.contentID("general"))
		.icon(() -> new ItemStack(Blocks.LARGE_AMETHYST_BUD))
		.appendItems(stacks ->
		{
			stacks.add(new ItemStack(CRYSTAL_SHARD));
			stacks.add(new ItemStack(CRYSTAL));
			stacks.add(new ItemStack(FIRE_CRYSTAL));
			stacks.add(new ItemStack(ICE_CRYSTAL));
			stacks.add(new ItemStack(THUNDER_CRYSTAL));
			stacks.add(new ItemStack(WIND_CRYSTAL));
			stacks.add(new ItemStack(WATER_CRYSTAL));
			stacks.add(new ItemStack(EARTH_CRYSTAL));
			stacks.add(new ItemStack(HOLY_CRYSTAL));
			stacks.add(new ItemStack(DARK_CRYSTAL));
			stacks.add(new ItemStack(POISON_CRYSTAL));
			stacks.add(new ItemStack(GRAVITY_CRYSTAL));
			// #<item_group>
			stacks.add(ItemStack.EMPTY);
		})
		.build();

	public static final ItemGroup FMC_BLOCKS = FabricItemGroupBuilder
	.create(FMCIdentifier.contentID("general"))
	.icon(() -> new ItemStack(Blocks.LARGE_AMETHYST_BUD)).build();

	public static void init()
	{
		Registry.register(Registry.ITEM, FMCIdentifier.contentID("crystal_shard_item"), CRYSTAL_SHARD);
		Registry.register(Registry.ITEM, FMCIdentifier.contentID("crystal_item"), CRYSTAL);
		Registry.register(Registry.ITEM, FMCIdentifier.contentID("fire_crystal_item"), FIRE_CRYSTAL);
		Registry.register(Registry.ITEM, FMCIdentifier.contentID("ice_crystal_item"), ICE_CRYSTAL);
		Registry.register(Registry.ITEM, FMCIdentifier.contentID("thunder_crystal_item"), THUNDER_CRYSTAL);
		Registry.register(Registry.ITEM, FMCIdentifier.contentID("wind_crystal_item"), WIND_CRYSTAL);
		Registry.register(Registry.ITEM, FMCIdentifier.contentID("water_crystal_item"), WATER_CRYSTAL);
		Registry.register(Registry.ITEM, FMCIdentifier.contentID("earth_crystal_item"), EARTH_CRYSTAL);
		Registry.register(Registry.ITEM, FMCIdentifier.contentID("holy_crystal_item"), HOLY_CRYSTAL);
		Registry.register(Registry.ITEM, FMCIdentifier.contentID("dark_crystal_item"), DARK_CRYSTAL);
		Registry.register(Registry.ITEM, FMCIdentifier.contentID("poison_crystal_item"), POISON_CRYSTAL);
		Registry.register(Registry.ITEM, FMCIdentifier.contentID("gravity_crystal_item"), GRAVITY_CRYSTAL);
		// #<item_register>
		
	}
}