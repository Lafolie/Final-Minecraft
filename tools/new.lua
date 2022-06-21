--[[
	New
	---
	A tool for generating new content in the FMC project.
	Run with -h for more info
]]
local path = select(1, ...):match "(.+%.).-"
local config = require(path .. "config")
local util = require(path .. "util")
local json = require(path .."lib.json.json-beautify")

local insert, remove, concat = table.insert, table.remove, table.concat
local match, format = string.match, string.format
local commands = {}

-- Vars -----------------------------------------------------------------------
function mkTag(str)
	return format(config.tagTemplate, str)
end

local itemsPath  = "src/main/java/lafolie/fmc/core/FMCItems.java"
local blocksPath = "src/main/java/lafolie/fmc/core/FMCBlocks.java"
local langPath   = "src/main/resources/assets/final-minecraft/lang/en_us.json"

local item_import_packageTag = mkTag("item_import_package")
local item_instanceTag = mkTag("item_instance")
local item_groupTag = mkTag("item_group")
local item_registerTag = mkTag("item_register")

local block_import_packageTag = mkTag("block_import_package")
local block_instanceTag = mkTag("block_instance")
local block_registerTag = mkTag("block_register")

local langTag = mkTag("items")

local itemsFile, itemsSplitPoints = util.splitFile(itemsPath, item_import_packageTag, item_instanceTag, item_groupTag, item_registerTag)
local blocksFile, blocksSplitPoints = util.splitFile(blocksPath, block_import_packageTag, block_instanceTag, block_registerTag)
local langFile, langSplitPoints = util.splitFile(langPath, langTag)

-- Util -----------------------------------------------------------------------
local itemTemplate = require(path .. "templates.item")
local blockTemplate = require(path .. "templates.block")

function createItem(item)
	print("Making item:", item.importPackage)
	-- Assets -------------------------------------------------
	util.copyFile(config.texture, item.texturePath)
	util.writeString(item.modelPath, item.modelJson)
	insert(langSplitPoints[langTag], item.lang)

	-- Java ---------------------------------------------------
	insert(itemsSplitPoints[item_groupTag], item.jItemGroup)
	insert(itemsSplitPoints[item_registerTag], item.jRegister)
	
	if item.isSimple then
		insert(itemsSplitPoints[item_instanceTag], item.jInstanceSimple)
		return
	end
	
	insert(itemsSplitPoints[item_import_packageTag], item.importPackage)
	insert(itemsSplitPoints[item_instanceTag], item.jInstance)
	os.execute(format("mkdir %s", item.classDir))
	util.writeString(item.classPath, item.jClass)
end

function createBlock(block)

end

-- Commands -------------------------------------------------------------------
commands["-help"] = function(args)
print [=[
	Usage: new [option1 name [isSimple] [package]] [option2 [args]] ...
	This program adds new content to the FMC project.
	Java and JSON files will be generated, as well as a placeholder texture and
	a line will be added to the en_US lang file.
	The word "Block" or "Item" will be appended to the given name, so there
	is no need to include when specifying the name.
	
	Available arguments:
		-h -help       Show this help screen
		-i -item       Adds a new item to the project with the given name
		-b -block      Adds a new block & blockitem to the project with the given name
]=]
end

commands["-item"] = function(args)
	local name = args[1]
	local isSimple = args[2]
	local package = args[3] or ""

	if name and package then
		return "items", itemTemplate(config, name, package, isSimple)
	else
		print "No name given to -item"
		print("\tName:", name)
		print("\tSimple:", isSimple and "yes" or "no")
		print("\tPackage:", package)
		print "No changes were made"
		os.exit()
	end
end

commands["-block"] = function(args)
	local name = args[1]
	local isSimple = args[2]
	local package = args[3] or ""

	if name and package then
		return "blocks", blockTemplate(config, name, package, isSimple)
	else
		print "No name given to -block"
		print("\tName:", name)
		print("\tSimple:", isSimple and "yes" or "no")
		print("\tPackage:", package)
		print "No changes were made"
		os.exit()
	end
end

--shorthands
commands["-h"] = commands["-help"]
commands["-i"] = commands["-item"]
commands["-b"] = commands["-block"]

-- Args -----------------------------------------------------------------------
local function processArgs(...)
	local args = {...}
	local list = {}
	
	local n = 0
	local function consume()
		n = n + 1
		return args[n]
	end

	--process commands with variable numbers of arguments
	local entry
	while n < #args do
		local arg = consume(args)
		local cmd = commands[arg]

		if cmd then
			insert(list, entry)
			entry = setmetatable({}, {__call = cmd})
		else
			if entry then
				insert(entry, arg)
			end
		end
	end

	--make sure to include the last one!
	insert(list, entry)

	if not (#list > 0) then
		print "Unknown command. See -h"
		os.exit()
	end

	return list
end

-- Main -----------------------------------------------------------------------

local function main(...)
	local commandList = processArgs(...)
	local objects =
	{
		items = {},
		blocks = {}
	}

	--generate changelist
	for _, cmd in ipairs(commandList) do
		local kind, result = cmd()
		if result then
			insert(objects[kind], result)
		end
	end

	--add the new objects
	for _, item in ipairs(objects.items) do
		createItem(item)
	end

	for _, block in ipairs(objects.blocks) do
		createBlock(block)
	end

	--merge split files & write
	local writeLang

	if #objects.items > 0 then
		local mergedItems = util.mergeSplitFile(itemsFile, itemsSplitPoints)
		util.writeStringSimple(itemsPath, mergedItems)
		writeLang = true
	end

	if #objects.blocks > 0 then
		local mergedBlocks = util.mergeSplitFile(blocksFile, blocksSplitPoints)
		util.writeStringSimple(blocksPath, mergedBlocks)
		writeLang = true
	end

	if writeLang then
		local mergedLang = util.mergeSplitFile(langFile, langSplitPoints)
		util.writeStringSimple(langPath, mergedLang)
	end
end

return main