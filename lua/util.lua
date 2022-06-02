local concat, insert = table.concat, table.insert

local util = {}

function util.writeTable(tbl, path, seperator)
	local file = io.open(path, "w")
	io.output(file)
	io.write(concat(tbl, seperator or "\n"))
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

return util