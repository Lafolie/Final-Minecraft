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

local itemsPath = "src/main/java/lafolie/fmc/core/Items.java"
local langPath = "src/main/resources/assets/final-minecraft/lang/en_us.json"

local item_import_packageTag = mkTag("item_import_package")
local item_instanceTag = mkTag("item_instance")
local item_groupTag = mkTag("item_group")
local item_registerTag = mkTag("item_register")
local langTag = mkTag("items")

local itemsFile, itemsSplitPoints = util.splitFile(itemsPath, item_import_packageTag, item_instanceTag, item_groupTag, item_registerTag)
local langFile, langSplitPoints = util.splitFile(langPath, langTag)

-- Util -----------------------------------------------------------------------
local addItem = require(path .. "item.addItem")

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

-- Commands -------------------------------------------------------------------
commands["-help"] = function(args)
print [=[
	Help text here.
]=]
end

commands["-add"] = function(args)
	local name = args[1]
	local isSimple = args[2]
	local package = args[3] or ""

	if name and package then
		return "add", addItem(config, name, package, isSimple)
	else
		print "No name given to -add"
		print(name, package)
		print "No changes were made"
		os.exit()
	end
end

--shorthands
commands["-h"] = commands["-help"]
commands["-a"] = commands["-add"]

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
	local toAdd = {}

	--generate changelist
	for _, cmd in ipairs(commandList) do
		local kind, result = cmd()
		if result then
			if kind == "add" then
				insert(toAdd, result)
			end
		end
	end

	--add the new items
	for _, item in ipairs(toAdd) do
		createItem(item)
	end

	--merge split files
	local mergedItems = util.mergeSplitFile(itemsFile, itemsSplitPoints)
	local mergedLang = util.mergeSplitFile(langFile, langSplitPoints)
	
	--writeout the merged files
	util.writeStringSimple(itemsPath, mergedItems)
	util.writeStringSimple(langPath, mergedLang)
end

return main