#!/bin/sh

# cd lua
file=$1
shift 1
# lua5.2 "luaSrc/$file.lua" "$@"
lua5.2 "$@" -e "require 'luaSrc.$file'"

tagsDir="src/main/resources/data/c/tags"
outputDir="lua/output"
# cd ..
# move genTags files
if [ $file == "genTags" ]; then
	echo "Copying files..."
	cp -r "$outputDir/blocks" "$tagsDir"
	cp -r "$outputDir/items" "$tagsDir"
	cp -r "$outputDir/fluids" "$tagsDir"
	cp -r "$outputDir/entity_types" $tagsDir
fi