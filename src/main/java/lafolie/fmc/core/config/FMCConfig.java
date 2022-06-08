package lafolie.fmc.core.config;

import java.util.ArrayList;

import lafolie.fmc.core.elements.ElementalAspect;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.util.Pair;

@Config(name = "FinalMinecraftCore")
public class FMCConfig implements ConfigData
{
	// General
	public boolean enableElements = true;
	public float defaultWeakResistAmount = 0.2f;
}
