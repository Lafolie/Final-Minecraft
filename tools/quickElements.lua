local path = select(1, ...):match "(.+%.).-"
local config = require(path .. "config")
local util = require(path .. "util")
local json = require(path .."lib.json.json-beautify")

local insert, remove = table.insert, table.remove
local match, format = string.match, string.format

local blockFile = util.readFile "src/main/resources/data/final-minecraft/recipes/crystal_block.json"
local itemFile = util.readFile "src/main/resources/data/final-minecraft/recipes/crystal_item_template.json"

for _, element in ipairs(util.elementalAspects) do
	local blockPath = format("src/main/resources/data/final-minecraft/recipes/%s_crystal_block.json", element)
	local itemPath =  format("src/main/resources/data/final-minecraft/recipes/%s_crystal_item.json", element)
	local crystalName = format("final-minecraft:%s_crystal", element)
	local blockStr = blockFile:gsub("final%-minecraft:crystal", crystalName)
	local itemStr = itemFile:gsub("final%-minecraft:e_crystal", crystalName)
	util.writeStringSimple(blockPath, blockStr)
	util.writeStringSimple(itemPath, itemStr)
end