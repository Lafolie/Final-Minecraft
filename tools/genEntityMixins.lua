local passives = 
{
	mixin = "entity.passive",
	packageDir = "lafolie/fmc/core/mixin/entity/passive",
	mcPackage = "net.minecraft.entity.passive",
	"Axolotl",
	"Bat",
	"Bee",
	"Cat",
	"Chicken",
	"Cod",
	"Cow",
	"Dolphin",
	"Donkey",
	"Fox",
	"GlowSquid",
	"Goat",
	"Horse",
	"IronGolem",
	"Llama",
	"Mooshroom",
	"Mule",
	"Ocelot",
	"Panda",
	"Parrot",
	"Pig",
	"PolarBear",
	"Rabbit",
	"Salmon",
	"Sheep",
	"Squid",
	"Strider",
	"TropicalFish",
	"Turtle",
	"Villager",
	"WanderingTrader",
	"Wolf"
}

local mobs = 
{
	mixin = "entity.mob",
	packageDir = "lafolie/fmc/core/mixin/entity/mob",
	mcPackage = "net.minecraft.entity.mob",
	"Blaze",
	"CaveSpider",
	"Creeper",
	"Drowned",
	"ElderGuardian",
	"Enderman",
	"Endermite",
	"Evoker",
	"Ghast",
	"Guardian",
	"Hoglin",
	"Husk",
	"Illager",
	"Illusioner",
	"MagmaCube",
	"Phantom",
	"Piglin",
	"PiglinBrute",
	"Pillager",
	"Ravager",
	"Shulker",
	"Silverfish",
	"Skeleton",
	"SkeletonHorse",
	"Slime",
	"SpellcastingIllager",
	"Spider",
	"Stray",
	"Vex",
	"Vindicator",
	"Witch",
	"WitherSkeleton",
	"Zoglin",
	"Zombie",
	"ZombieHorse",
	"ZombieVillager",
	"ZombifiedPiglin"
}

local wither = 
{
	mixin = "entity.boss",
	packageDir = "lafolie/fmc/core/mixin/entity/boss",
	mcPackage = "net.minecraft.entity.boss",
	"Wither"
}

local enderDragon = 
{
	mixin = "entity.boss",
	packageDir = "lafolie/fmc/core/mixin/entity/boss",
	mcPackage = "net.minecraft.entity.boss.dragon",
	"EnderDragon"
}

local template = 
[[package lafolie.fmc.core.mixin.%s;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lafolie.fmc.core.Mod;
import lafolie.fmc.core.config.FMCConfig;
import lafolie.fmc.core.elements.Element;
import lafolie.fmc.core.elements.ElementalEntity;

import net.minecraft.entity.EntityType;
import %s.%sEntity;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

@Mixin(%sEntity.class)
public abstract class %sEntityMixin
{
	@Inject(at = @At("TAIL"), method = "<init>")
	public void Constructor(EntityType<? extends %sEntity> entityType, World world, CallbackInfo info)
	{
		ElementalEntity self = (ElementalEntity)this;

		FMCConfig config =  Mod.getConfig();
		ArrayList<Pair<Element, Float>> baseElements = config.%sWeakResist.GetPairList();

		self.InitElementalEntity(baseElements);
	}
}
]]

local configTemplate =
[[@ConfigEntry.Category("Elements")
@ConfigEntry.Gui.CollapsibleObject
public ElementConfig %sWeakResist = new ElementConfig();
]]

local configSource = {}

local mixins = {}
local lang = {}

local elements = 
{
	"none",
	"fire",
	"ice",
	"lightning",
	"wind",
	"water",
	"earth",
	"poison",
	"holy",
	"dark",
	"gravity"
}

local elementsPretty = 
{
	"None",
	"Fire",
	"Ice",
	"Lightning",
	"Wind",
	"Water",
	"Earth",
	"Poison",
	"Holy",
	"Dark",
	"Gravity"
}


function generateSource(mixin, package, name, lowName)
	return template:format(mixin, package, name, name, name, name, lowName), configTemplate:format(lowName)
end

function generateAll(tbl)
	local path = string.format("src/main/java/%s", tbl.packageDir)
	for k, ent in ipairs(tbl) do
		local lowName = string.lower(ent):sub(1,1) .. ent:sub(2)
		local source, config = generateSource(tbl.mixin, tbl.mcPackage, ent, lowName)
		table.insert(configSource, config)
		local fname = string.format("%s/%sEntityMixin.java", path, ent)
		print(fname)
		local file = io.open(fname, "w")
		io.output(file)
		io.write(source)
		io.close(file)
		print(string.format("Created source file: %s", fname))

		local mixin = string.format("\"%s.%sEntityMixin\",", tbl.mixin, ent)
		table.insert(mixins, mixin)

		local langStr = string.format("\"text.autoconfig.FinalMinecraftCore.option.%sWeakResist\":\t\"%s\",", lowName, ent)
		table.insert(lang, langStr)
		for k, element in ipairs(elements) do
			table.insert(lang, string.format("\"text.autoconfig.FinalMinecraftCore.option.%sWeakResist.%s\":\t\"%s\",", lowName, element, elementsPretty[k]))
		end
	end
end

function writeTable(tbl, path)
	local file = io.open(path, "w")
	io.output(file)
	io.write(table.concat(tbl, "\n"))
	io.close()
	print(string.format("Created %s", path))
end

generateAll(mobs)
generateAll(wither)
generateAll(enderDragon)
generateAll(passives)
writeTable(configSource, "lua/output/config.txt")
writeTable(mixins, "lua/output/mixins.txt")
writeTable(lang, "lua/output/lang.txt")