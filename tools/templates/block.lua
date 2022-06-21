local format = string.format
local upper, lower = string.upper, string.lower

function f(config, name, package, isSimple)
	local result = {}
	-- Misc ---------------------------------------------------
	if package ~= "" then
		package = "." .. package
	end

	local words = {}
	for str in string.gmatch(name, "%u+%l*") do
		table.insert(words, str)
	end

	local constantName = upper(table.concat(words, "_"))
	local assetName = lower(constantName)
	local displayName = table.concat(words, " ")

	result.isSimple = isSimple
	result.package = format("%s.block%s", config.basePackage, package)
	result.importPackage = format("import %s.%sBlock;", result.package, name)
	result.classDir = format("%s/block/%s", config.basePath, package)
	result.classPath = format("%s%sBlock.java", result.classDir, name)

	-- Assets -------------------------------------------------
	result.texturePath = format("%s/textures/block/%s_block.png", config.assetPath, assetName)
	result.blockModelPath = format("%s/models/block/%s_block.json", config.assetPath, assetName)
	result.blockItemModelPath = format("%s/models/item/%s_block.json", config.assetPath, assetName)
	result.blockstatePath = format("%s/blockstates/%s_block.json", config.assetPath, assetName)
	result.lootTablePath = format("%s/loot_tables/blocks/%s_block.json", config.dataPath, assetName)
	result.blockstateJson = format([[
{
	"variants": {
		"": { "model": "%s:block/%s_block" }
	}
}
	]], config.modid, assetName)

	result.blockModelJson = format([[
{
	"parent": "block/cube_all",
	"textures": {
		"all": "modid:block/%s_block"
	}
}
	]], config.modid, assetName)

	result.blockItemModelJson = format([[
{
	"parent": "%s:block/%s_block"
}
	]], config.modid, assetName)

	result.lootTableJson = format([[
{
	"type": "minecraft:block",
	"pools": [
		{
		"rolls": 1,
		"entries": [
			{
			"type": "minecraft:item",
			"name": "%s:%s_block"
			}
		],
		"conditions": [
			{
			"condition": "minecraft:survives_explosion"
			}
		]
		}
	]
}
	]], config.modid, assetName)
	result.lang = format('"block.%s.%s": "%s",', config.modid, assetName, displayName)

	-- Java ---------------------------------------------------
	result.jInstanceSimple = format("public static final Block %s = new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f));", constantName)
	result.jInstance = format("public static final %sBlock %s = new %sBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f));", name, constantName, name)
	result.jRegisterBlock = format('Registry.register(Registry.BLOCK, FMCIdentifier.contentID("%s_block"), %s);', assetName, constantName)
	result.jRegisterItem = format('Registry.register(Registry.ITEM, FMCIdentifier.contentID("%s_block"), new BlockItem(%s, new FabricItemSettings().group(FMCItems.FMC_ITEMS)));', assetName, constantName)
	result.jItemGroup = format("stacks.add(new ItemStack(%s));", constantName) --TODO: see if this is required
	result.jClass = format([[
package %s;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class %sBlock extends Block
{
	public %sBlock(Settings settings)
	{
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockHitResult hit) {
		// TODO Lua-generated method stub ;)
		return ActionResult.SUCCESS;
	}
}
]], result.package, name, name)

	return result
end

return f