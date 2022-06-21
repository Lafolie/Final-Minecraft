local concat, insert, remove = table.concat, table.insert, table.remove

local util = {}

util.elementalAspects = 
{
	none = "none",
	fire = "fire",
	ice = "ice",
	lightning = "lightning",
	wind = "wind",
	water = "water",
	earth = "earth",
	poison = "poison",
	holy = "holy",
	dark = "dark",
	gravity = "gravity"
}

function util.mergeSplitFile(file, splitPoints)
	local numInserts = 0
	for _, splitPoint in ipairs(splitPoints) do
		local i = splitPoint.index + numInserts
		numInserts = numInserts + 1

		local tabs = string.rep("\t", splitPoint.tabs)
		--prepend tabs to splitpoints
		for n, str in ipairs(splitPoint) do
			splitPoint[n] = tabs .. str
		end

		insert(file, i, concat(splitPoint, "\n"))
	end
	return concat(file, "\n")
end

function util.splitFile(path, ...)
	print("Splitting file:", path)
	assert(path, "no path given to splitFile!")
	local tags = {...}
	local result = {}
	local splitPoints = {}

	local tbl = {}
	local tag = remove(tags, 1)
	local numSplits = 1
	for line in io.lines(path) do
		--split the file after a tag
		if tag and line:match(tag) then
			print("\tFound tag:", tag)
			--flatten previous section
			insert(result, concat(tbl, "\n"))
			tbl = {}

			--create a splitPoint
			numSplits = numSplits + 1
			local tabs = string.match(line, "\t*.")
			local splitPoint = {index = numSplits, tabs = #tabs - 1}
			insert(splitPoints, splitPoint) --need this for iPairs
			splitPoints[tag] = splitPoint --this makes it easy to add lines

			--move on to the next tag
			tag = remove(tags, 1)
		end

		--ensure tag is below inserted content
		insert(tbl, line)
	end

	--flatten the final section
	insert(result, concat(tbl, "\n"))

	if #tags > 0 then
		print "[WARNING] Did not find tags:"
		for k, v in ipairs(tags) do
			print("", v)
		end
	end

	return result, splitPoints
end

function util.writeTable(path, tbl, seperator)
	local file = io.open(path, "w")
	io.output(file)
	io.write(concat(tbl, seperator or "\n"))
	io.close()
	print(string.format("Created %s", path))
end

function util.writeString(path, str, ...)
	local file = io.open(path, "w")
	io.output(file)
	io.write(string.format(str, ...))
	io.close()
	print(string.format("Created %s", path))
end

function util.writeStringSimple(path, str)
	local file = io.open(path, "w")
	io.output(file)
	io.write(str)
	io.close()
	print(string.format("Created %s", path))
end

function util.readCSV(path, firstRowIsHeader)
	local tbl = {}
	local header
	
	for line in io.lines(path) do
		local row = {}
		
		if firstRowIsHeader then
			local values = {}
			for str in line:gmatch "([^,%c]+)" do
				insert(values, str)
			end
			
			--populate the header entries
			if not header then
				header = values
			else
				insert(tbl, row)
				for n = 1, #values do
					local id = header[n]
					row[id] = values[n]
				end
			end
		else
			insert(tbl, row)
			for str in line:gmatch "([^,%c]+)" do
				insert(row, str)
			end
		end
	end

	return tbl
end

function util.copyFile(src, dest)
	local file = io.open(src, "r")
	local contents = file:read "*a"
	file:close()

	file = io.open(dest, "w")
	file:write(contents)
	file:close()

	print(string.format("Copied file %s to %s", src, dest))
end


return util