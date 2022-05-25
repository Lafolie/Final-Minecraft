package lafolie.fmc.core;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lafolie.fmc.core.config.FMCConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class Mod implements ModInitializer
{
	public static final byte VERSION_MAJOR = 0;
	public static final byte VERSION_MINOR = 1;
	public static final byte VERSION_REVISION = 0;
	public static final String VERSION_CODENAME = "Biggs"; //convention = FFVI characters in order of appearance

	public static final Logger log = LoggerFactory.getLogger("FMC Core");

	private static FMCConfig config;

	public static String getVersionString()
	{
		return String.format("%d.%d.%d \"%s\"", VERSION_MAJOR, VERSION_MINOR, VERSION_REVISION, VERSION_CODENAME);
	}

	@Override
	public void onInitialize()
	{
		log.info("Loaded FMC Core version %s", getVersionString());
		InitConfig();
	}

	private void InitConfig()
	{
		AutoConfig.register(FMCConfig.class, JanksonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(FMCConfig.class).getConfig();
	}

	public static FMCConfig getConfig()
	{
		return config;
	}
}
