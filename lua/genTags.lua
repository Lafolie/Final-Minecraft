local util = require "util"

local args = {...}
local genList = {}

-- Commands -------------------------------------------------------------------
local commands = {}

commands["-help"] = function(path)
	print[=[
Usage: genTags [option1 [arg]] [option2 [arg]] ...
This program transforms CSV data into tag JSON usable by minecraft.
Available arguments:
	-h help		Show this help screen
	-b blocks	Generate block tags from CSV. Takes option path as arg, defaults to 'input/blocks.csv'
	-i items	Generate item tags from CSV. Takes option path as arg, defaults to 'input/items.csv'
	-e entities	Generate block tags from CSV. Takes option path as arg, defaults to 'input/entities.csv']=]
end


commands["-blocks"] = function(path)
	print(path or "blocks")
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

			table.insert(list, {f = cmd, arg = cmdArg})
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