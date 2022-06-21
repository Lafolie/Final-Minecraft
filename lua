#!/usr/bin/env lua5.2
package.path = package.path .. ";?/init.lua"
require("tools." .. select(1, ...))(select(2, ...))
