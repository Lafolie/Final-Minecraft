package lafolie.fmc.core.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "FinalMinecraftCore")
public class FMCConfig implements ConfigData
{
	// General
	public boolean enableElements = true;
	public boolean combatEnableBirthsign = true;
	public float defaultWeakResistAmount = 0.1f;
	public int defaultEnemyWeakResist = 6;
}
