local util = require "util"
local json = require "lib.json.json-beautify"

local insert = table.insert

-- Vars -----------------------------------------------------------------------
local args = {...}
local genList = {}
local justHelp
local outputFiles = {}
local uniqueTags = {}
local tagCollisions = 0
local tagMismatches = 0

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

local jsonConfig = {indent = "\t"}

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

local function getOutputFile(name, includeNone)
	local file = outputFiles[name]
	if not file then
		file = mkAspectTables(includeNone)
		outputFiles[name] = file
	end
	return file
end

local function getUniqueList(name)
	local list = uniqueTags[name]
	if not list then
		list = {}
		uniqueTags[name] = list
	end
	return list
end

local function ensureUnique(listName, tag, value, source)
	list = getUniqueList(listName)
	if list[tag] then
		tagCollisions = tagCollisions + 1
		print(string.format("Found duplicate %s tag '%s', skipping", listName, tag))
		if list[tag].value ~= value then
			tagMismatches = tagMismatches + 1	
			print(string.format([[
WARNING: Duplicate tag elements do not match!
	Existing element: %s (from %s)
	Given element:    %s (from %s)
	]], list[tag].value, list[tag].source, value, source))
		end
		return
	end

	list[tag] = {value = value, source = source}
	return true
end

local function exportAspectTables(tables, dir)
	local path = string.format("output/%s/%%s_elemental.json", dir)
	for element, tbl in pairs(tables) do
		if #tbl.values >0 then
			local p = string.format(path, element)
			local str = json.beautify(tbl, jsonConfig)
			util.writeString(p, str)
		end
	end
end

-- Commands -------------------------------------------------------------------
local commands = {}

commands["-help"] = function(path)
	print [=[
Usage: genTags [option1 [arg]] [option2 [arg]] ...
This program transforms CSV data into tag JSON usable by minecraft. 
Outputs to 'output/[type] where type matches the argument types (blocks, items, etc).
Output dirs must already exist!

Available arguments:
	-h help		Show this help screen
	-a all		Generate all tags from CSVs. Takes an optional arg path (dir), using the default filenames. Defaults to 'input/'
	-b blocks	Generate block tags from CSV. Takes optional arg path, defaults to 'input/blocks.csv'
	-i items	Generate item tags from CSV. Takes optional arg path, defaults to 'input/items.csv'
	-e entities	Generate entity tags from CSV. Takes optional arg path, defaults to 'input/entities.csv'
	-f fluids   Generate fluid tags from CSV. Takes optional arg path, defaults to 'input/fluids.csv'
	
	Examples:
		Generate everything:
			genTags -a
		With a custom dir 'input2':
			genTags -a input2
		
		Generate blocks only:
			genTags -b
		With a custom filename:
			genTags -b myblocks.csv
		]=]
end

commands["-blocks"] = function(path)
	path = path or "input/blocks.csv"
	--blocks.csv contains both block IDs and block item IDs, so we
	--need to use 2 table sets
	local blocks = getOutputFile "blocks"
	local items = getOutputFile "items"
	local data = util.readCSV(path, true)

	for _, line in ipairs(data) do
		local element = line["Element"]
		local blockID = line["Block ID"]
		local itemID = line["Item ID"]
		if string.lower(itemID) == "identical" then
			itemID = blockID
		end
		local override = line["Item Element Override"]
		-- print(line["Name"], override)

		--skip element n (NONE)
		if element ~= "n" then
			--convert shorthand to full name
			local fullElement = elementShorthands[element]
			-- print("----", fullElement, blocks[fullElement])
			if ensureUnique("blocks", blockID, fullElement, path .. ":blocks") then
				insert(blocks[fullElement].values, "minecraft:" .. blockID)
			end

			-- can't use a ternary here
			if override then
				fullElement = elementShorthands[override]
			end

			--double check for 'none' because of potential override value
			if itemID ~= "None" and fullElement ~= "none" then
				if ensureUnique("items", itemID, fullElement, path .. ":items") then
					insert(items[fullElement].values, "minecraft:" .. itemID)
				end
			end
		end
	end
end

commands["-items"] = function(path)
	path = path or "input/items.csv"
	local items = getOutputFile "items"
	local data = util.readCSV(path, true)

	for _, line in ipairs(data) do
		local element = line["Element"]
		local id = line["ID"]
		if element ~= "n" then
			local fullElement = elementShorthands[element]
			if ensureUnique("items", id, fullElement, path) then
				insert(items[fullElement].values, "minecraft:" .. id)
			end
		end
	end

end

commands["-entities"] = function(path)
	print(path or "entities")
end

commands["-fluids"] = function(path)
	path = path or "input/fluids.csv"
	local fluids = getOutputFile "fluids"
	local data = util.readCSV(path, true)

	for _, line in ipairs(data) do
		local element = line["Element"]
		local id = line["ID"]
		if element ~= "n" then
			local fullElement = elementShorthands[element]
			if ensureUnique("fluids", id, fullElement, path) then
				insert(fluids[fullElement].values, "minecraft:" .. id)
			end
		end
	end
end


commands["-all"] = function()
	commands["-blocks"]()
	commands["-items"]()
	commands["-entities"]()
	commands["-fluids"]()
end

--shorthands
commands["-h"] = commands["-help"]
commands["-a"] = commands["-all"]
commands["-b"] = commands["-blocks"]
commands["-i"] = commands["-items"]
commands["-e"] = commands["-entities"]
commands["-f"] = commands["-fluids"]

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
			print(string.format("Unknown option %s", arg))
			os.exit()
		end
	end
end

-- Go! ------------------------------------------------------------------------
processArgs(args, genList)

print "-- Generating tags... ---------------------------------------------------------"
for k, cmd in pairs(genList) do
	cmd.f(cmd.arg)
end
print "-- Generation successful ------------------------------------------------------"

for name, file in pairs(outputFiles) do
	exportAspectTables(file, name)
end

print ("Num duplicates:", tostring(tagCollisions))
print ("Num conflicts:", tostring(tagMismatches), tagMismatches > 1 and "!!!" or "OK")
