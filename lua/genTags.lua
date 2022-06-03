local util = require "util"
local json = require "lib.json"

local insert = table.insert

-- Vars -----------------------------------------------------------------------
local args = {...}
local genList = {}

local elementShorthands = 
{
	n = util.elementalAspects.none,
	f = util.elementalAspects.fire,
	i = util.elementalAspects.ice,
	l = util.elementalAspects.lightning,
	w = util.elementalAspects.wind,
	u = util.elementalAspects.water,
	e = util.elementalAspects.earth,
	p = util.elementalAspects.poison,
	h = util.elementalAspects.holy,
	d = util.elementalAspects.dark,
	g = util.elementalAspects.gravity

}

-- Common Funcs ---------------------------------------------------------------
local function mkAspectTables(includeNone)
	local tables = {}
	for _, element in pairs(util.elementalAspects) do
		if includeNone or element ~= "none" then
			tables[element] =
			{
				replace = false,
				values = {}
			}
		end
	end

	return tables
end

local function exportAspectTables(tables, dir)
	local path = string.format("output/%s/%%s_elemental.json", dir)
	for element, tbl in pairs(tables) do
		local p = string.format(path, element)
		local str = json.encode(tbl)
		util.writeString(p, str)
	end
end

-- Commands -------------------------------------------------------------------
local commands = {}

commands["-help"] = function(path)
	print [=[
Usage: genTags [option1 [arg]] [option2 [arg]] ...
This program transforms CSV data into tag JSON usable by minecraft.
Available arguments:
	-h help		Show this help screen
	-b blocks	Generate block tags from CSV. Takes option path as arg, defaults to 'input/blocks.csv'
	-i items	Generate item tags from CSV. Takes option path as arg, defaults to 'input/items.csv'
	-e entities	Generate block tags from CSV. Takes option path as arg, defaults to 'input/entities.csv']=]
end

commands["-blocks"] = function(path)
	--blocks.csv contains both block IDs and block item IDs, so we
	--need to use 2 table sets
	local blocks = mkAspectTables()
	local items = mkAspectTables()
	local data = util.readCSV(path or "input/blocks.csv", true)

	--LUTs, to ensure IDs are only included once
	local uniqueBlocks = {}
	local uniqueItems = {}

	for _, line in ipairs(data) do
		local element = line["Element"]
		local blockID = line["Block ID"]
		local itemID = line["Item ID"]
		if string.lower(itemID) == "identical" then
			itemID = blockID
		end
		
		--skip element n (NONE)
		if element ~= "n" then
			--convert shorthand to full name
			local fullElement = elementShorthands[element]
			-- print("----", fullElement, blocks[fullElement])
			if not uniqueBlocks[blockID] then
				insert(blocks[fullElement].values, "minecraft:" .. blockID)
				uniqueBlocks[blockID] = true
			end

			if not uniqueItems[itemID] then
				insert(items[fullElement].values, "minecraft:" .. itemID)
				uniqueItems[itemID] = true
			end
		end
	end

	exportAspectTables(blocks, "blocks")
	exportAspectTables(items, "items")
end

commands["-items"] = function(path)
	print(path or "items")
end

commands["-entities"] = function(path)
	print(path or "entities")
end

--shorthands
commands["-h"] = commands["-help"]
commands["-b"] = commands["-blocks"]
commands["-i"] = commands["-items"]
commands["-e"] = commands["-entities"]

-- Args -----------------------------------------------------------------------
local function processArgs(args, list)
	local n = 0

	local function consume()
		n = n + 1
		return args[n]
	end

	while n < #args do
		local arg = consume()
		local cmd = commands[arg]
		if cmd then
			--check for arg
			local nextArg = args[n + 1]
			local cmdArg
			if not commands[nextArg] then
				cmdArg = consume()
			end

			insert(list, {f = cmd, arg = cmdArg})
		else
			print(string.format("Unknown argument option %s", arg))
			os.exit()
		end
	end
end

-- Go! ------------------------------------------------------------------------
processArgs(args, genList)

for k, cmd in pairs(genList) do
	cmd.f(cmd.arg)
end