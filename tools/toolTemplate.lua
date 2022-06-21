local path = select(1, ...):match "(.+%.).-"
local config = require(path .. "config")
local util = require(path .. "util")
local json = require(path .."lib.json.json-beautify")

local insert, remove = table.insert, table.remove
local match, format = string.match, string.format
local commands = {}

-- Vars -----------------------------------------------------------------------

-- Util -----------------------------------------------------------------------
function mkTag(str)
	return format(config.tagTemplate, str)
end

-- Commands -------------------------------------------------------------------
commands["-help"] = function(args)
print [=[
	Help text here.
]=]
end

--shorthands
commands["-h"] = commands["-help"]

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
	for _, cmd in ipairs(commandList) do
		cmd()
	end

end

return main