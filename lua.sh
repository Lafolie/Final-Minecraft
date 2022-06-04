#!/bin/sh

cd lua
file=$1
shift 1
lua5.2 "$file.lua" "$@"

tagsDir="src/main/resources/data/c"
outputDir="lua/output"
cd ..
# move genTags files
if [ $file == "genTags" ]; then
	echo "Copying files..."
	cp -r "$outputDir/blocks" "$tagsDir"
	cp -r "$outputDir/items" "$tagsDir"
	cp -r "$outputDir/fluids" "$tagsDir"
fi