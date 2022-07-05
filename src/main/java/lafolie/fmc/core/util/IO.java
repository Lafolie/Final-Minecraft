package lafolie.fmc.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

import lafolie.fmc.core.FinalMinecraft;

public final class IO
{
	private static Yaml yaml = new Yaml();

	public static List<Object> readYaml(Path path)
	{
		List<Object> result = new ArrayList<>();
		try
		{
			InputStream input = Files.newInputStream(path, StandardOpenOption.READ);
			for(Object obj : yaml.loadAll(input))
			{
				result.add(obj);
			}
		} catch (IOException e) 
		{
			FinalMinecraft.LOG.error("Error loading YAML file '{}'", path);
			e.printStackTrace();
		}
		assert result.size() > 0 : "Error loading YAML file";

		return result;
	}
}
