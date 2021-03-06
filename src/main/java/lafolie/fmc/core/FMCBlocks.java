package lafolie.fmc.core;

import lafolie.fmc.core.util.FMCIdentifier;
import lafolie.fmc.core.FMCItems;
import lafolie.fmc.core.block.CrystalBlock;
import lafolie.fmc.core.block.HomeCrystalBlock;
import lafolie.fmc.core.block.HomeCrystalBlockEntity;
import lafolie.fmc.core.block.HomeCrystalRenderer;
import lafolie.fmc.core.block.CrystalPedestalBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
// #<block_import_package>
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.item.BlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

public final class FMCBlocks
{
	public static final CrystalBlock CRYSTAL = new CrystalBlock(FabricBlockSettings.of(Material.GLASS).strength(4.0f).nonOpaque().sounds(BlockSoundGroup.AMETHYST_BLOCK));
	public static final Block FIRE_CRYSTAL    = new Block(FabricBlockSettings.of(Material.GLASS).strength(4.0f).nonOpaque().sounds(BlockSoundGroup.AMETHYST_BLOCK));
	public static final Block ICE_CRYSTAL     = new Block(FabricBlockSettings.of(Material.GLASS).strength(4.0f).nonOpaque().sounds(BlockSoundGroup.AMETHYST_BLOCK));
	public static final Block THUNDER_CRYSTAL = new Block(FabricBlockSettings.of(Material.GLASS).strength(4.0f).nonOpaque().sounds(BlockSoundGroup.AMETHYST_BLOCK));
	public static final Block WIND_CRYSTAL    = new Block(FabricBlockSettings.of(Material.GLASS).strength(4.0f).nonOpaque().sounds(BlockSoundGroup.AMETHYST_BLOCK));
	public static final Block WATER_CRYSTAL   = new Block(FabricBlockSettings.of(Material.GLASS).strength(4.0f).nonOpaque().sounds(BlockSoundGroup.AMETHYST_BLOCK));
	public static final Block EARTH_CRYSTAL   = new Block(FabricBlockSettings.of(Material.GLASS).strength(4.0f).nonOpaque().sounds(BlockSoundGroup.AMETHYST_BLOCK));
	public static final Block HOLY_CRYSTAL    = new Block(FabricBlockSettings.of(Material.GLASS).strength(4.0f).nonOpaque().sounds(BlockSoundGroup.AMETHYST_BLOCK));
	public static final Block DARK_CRYSTAL    = new Block(FabricBlockSettings.of(Material.GLASS).strength(4.0f).nonOpaque().sounds(BlockSoundGroup.AMETHYST_BLOCK));
	public static final Block POISON_CRYSTAL  = new Block(FabricBlockSettings.of(Material.GLASS).strength(4.0f).nonOpaque().sounds(BlockSoundGroup.AMETHYST_BLOCK));
	public static final Block GRAVITY_CRYSTAL = new Block(FabricBlockSettings.of(Material.GLASS).strength(4.0f).nonOpaque().sounds(BlockSoundGroup.AMETHYST_BLOCK));
	
	public static final HomeCrystalBlock HOME_CRYSTAL = new HomeCrystalBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f).nonOpaque().sounds(BlockSoundGroup.AMETHYST_BLOCK).luminance((state) -> 15));
	public static BlockEntityType<HomeCrystalBlockEntity> HOME_CRYSTAL_ENTITY;
	public static final CrystalPedestalBlock CRYSTAL_PEDESTAL = new CrystalPedestalBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f));
	// #<block_instance>

	public static void init()
	{
		Registry.register(Registry.BLOCK, FMCIdentifier.contentID("crystal_block"), CRYSTAL);
		Registry.register(Registry.ITEM,  FMCIdentifier.contentID("crystal_block"), new BlockItem(CRYSTAL, new FabricItemSettings().group(FMCItems.FMC_BLOCKS)));
		Registry.register(Registry.BLOCK, FMCIdentifier.contentID("fire_crystal_block"), FIRE_CRYSTAL);
		Registry.register(Registry.ITEM,  FMCIdentifier.contentID("fire_crystal_block"), new BlockItem(FIRE_CRYSTAL, new FabricItemSettings().group(FMCItems.FMC_BLOCKS)));
		Registry.register(Registry.BLOCK, FMCIdentifier.contentID("ice_crystal_block"), ICE_CRYSTAL);
		Registry.register(Registry.ITEM,  FMCIdentifier.contentID("ice_crystal_block"), new BlockItem(ICE_CRYSTAL, new FabricItemSettings().group(FMCItems.FMC_BLOCKS)));
		Registry.register(Registry.BLOCK, FMCIdentifier.contentID("thunder_crystal_block"), THUNDER_CRYSTAL);
		Registry.register(Registry.ITEM,  FMCIdentifier.contentID("thunder_crystal_block"), new BlockItem(THUNDER_CRYSTAL, new FabricItemSettings().group(FMCItems.FMC_BLOCKS)));
		Registry.register(Registry.BLOCK, FMCIdentifier.contentID("wind_crystal_block"), WIND_CRYSTAL);
		Registry.register(Registry.ITEM,  FMCIdentifier.contentID("wind_crystal_block"), new BlockItem(WIND_CRYSTAL, new FabricItemSettings().group(FMCItems.FMC_BLOCKS)));
		Registry.register(Registry.BLOCK, FMCIdentifier.contentID("water_crystal_block"), WATER_CRYSTAL);
		Registry.register(Registry.ITEM,  FMCIdentifier.contentID("water_crystal_block"), new BlockItem(WATER_CRYSTAL, new FabricItemSettings().group(FMCItems.FMC_BLOCKS)));
		Registry.register(Registry.BLOCK, FMCIdentifier.contentID("earth_crystal_block"), EARTH_CRYSTAL);
		Registry.register(Registry.ITEM,  FMCIdentifier.contentID("earth_crystal_block"), new BlockItem(EARTH_CRYSTAL, new FabricItemSettings().group(FMCItems.FMC_BLOCKS)));
		Registry.register(Registry.BLOCK, FMCIdentifier.contentID("holy_crystal_block"), HOLY_CRYSTAL);
		Registry.register(Registry.ITEM,  FMCIdentifier.contentID("holy_crystal_block"), new BlockItem(HOLY_CRYSTAL, new FabricItemSettings().group(FMCItems.FMC_BLOCKS)));
		Registry.register(Registry.BLOCK, FMCIdentifier.contentID("dark_crystal_block"), DARK_CRYSTAL);
		Registry.register(Registry.ITEM,  FMCIdentifier.contentID("dark_crystal_block"), new BlockItem(DARK_CRYSTAL, new FabricItemSettings().group(FMCItems.FMC_BLOCKS)));
		Registry.register(Registry.BLOCK, FMCIdentifier.contentID("poison_crystal_block"), POISON_CRYSTAL);
		Registry.register(Registry.ITEM,  FMCIdentifier.contentID("poison_crystal_block"), new BlockItem(POISON_CRYSTAL, new FabricItemSettings().group(FMCItems.FMC_BLOCKS)));
		Registry.register(Registry.BLOCK, FMCIdentifier.contentID("gravity_crystal_block"), GRAVITY_CRYSTAL);
		Registry.register(Registry.ITEM,  FMCIdentifier.contentID("gravity_crystal_block"), new BlockItem(GRAVITY_CRYSTAL, new FabricItemSettings().group(FMCItems.FMC_BLOCKS)));
		
		Registry.register(Registry.BLOCK, FMCIdentifier.contentID("home_crystal_block"), HOME_CRYSTAL);
		Registry.register(Registry.ITEM,  FMCIdentifier.contentID("home_crystal_block"), new BlockItem(HOME_CRYSTAL, new FabricItemSettings().group(FMCItems.FMC_BLOCKS)));
		HOME_CRYSTAL_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, FMCIdentifier.contentID("home_crystal_block_entity"), FabricBlockEntityTypeBuilder.create(HomeCrystalBlockEntity::new, HOME_CRYSTAL).build(null));
		
		Registry.register(Registry.BLOCK, FMCIdentifier.contentID("crystal_pedestal_block"), CRYSTAL_PEDESTAL);
		Registry.register(Registry.ITEM,  FMCIdentifier.contentID("crystal_pedestal_block"), new BlockItem(CRYSTAL_PEDESTAL, new FabricItemSettings().group(FMCItems.FMC_BLOCKS)));
		// #<block_register>
	}

	@Environment(EnvType.CLIENT)
	public static void initClient()
	{
		BlockEntityRendererRegistry.register(HOME_CRYSTAL_ENTITY, (BlockEntityRendererFactory.Context rendererDispatcherIn) -> {return new HomeCrystalRenderer();});
	}
}