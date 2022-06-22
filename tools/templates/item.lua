local format = string.format
local upper, lower = string.upper, string.lower

function f(config, name, package, isSimple)
	local result = {}
	-- Misc ---------------------------------------------------
	if package ~= "" then
		package = "." .. package
	end

	local words = {}
	for str in string.gmatch(name, "%u+[%l%d_%-]*") do
		table.insert(words, str)
	end

	local constantName = upper(table.concat(words, "_"))
	local assetName = lower(constantName)
	local displayName = table.concat(words, " ")

	result.isSimple = isSimple
	result.package = format("%s.item%s", config.basePackage, package)
	result.importPackage = format("import %s.%sItem;", result.package, name)
	result.classDir = format("%s/item/%s", config.basePath, package)
	result.classPath = format("%s%sItem.java", result.classDir, name)

	-- Assets -------------------------------------------------
	result.texturePath = format("%s/textures/item/%s_item.png", config.assetPath, assetName)
	result.modelPath = format("%s/models/item/%s_item.json", config.assetPath, assetName)
	result.modelJson = format([[
{
	"parent": "item/generated",
	"textures": {
		"layer0": "%s:item/%s_item"
	}
}
	]], config.modid, assetName)
	result.lang = format('"item.%s.%s_item": "%s",', config.modid, assetName, displayName)

	-- Java ---------------------------------------------------
	result.jInstanceSimple = format("public static final Item %s = new Item(new FabricItemSettings());", constantName)
	result.jInstance = format("public static final %sItem %s = new %sItem(new FabricItemSettings());", name, constantName, name)
	result.jRegister = format('Registry.register(Registry.ITEM, FMCIdentifier.contentID("%s_item"), %s);', assetName, constantName)
	result.jItemGroup = format("stacks.add(new ItemStack(%s));", constantName)
	result.jClass = format([[
package %s;

import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class %sItem extends Item
{
	public %sItem(Settings settings)
	{
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext tooltipContext)
	{
		tooltip.add(new TranslatableText(""));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		//TODO: Lua-generated method stub :P
		return TypedActionResult.success(player.getStackInHand(hand));
	}
}
]], result.package, name, name)

	return result
end

return f