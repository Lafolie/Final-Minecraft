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

	// Entity weak/resist
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig blazeWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig caveSpiderWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig creeperWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig drownedWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig elderGuardianWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig endermanWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig endermiteWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig evokerWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig ghastWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig guardianWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig hoglinWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig huskWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig illagerWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig illusionerWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig magmaCubeWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig phantomWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig piglinWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig piglinBruteWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig pillagerWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig ravagerWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig shulkerWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig silverfishWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig skeletonWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig skeletonHorseWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig slimeWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig spellcastingIllagerWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig spiderWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig strayWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig vexWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig vindicatorWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig witchWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig witherSkeletonWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig zoglinWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig zombieWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig zombieHorseWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig zombieVillagerWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig zombifiedPiglinWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig witherWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig enderDragonWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig axolotlWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig batWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig beeWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig catWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig chickenWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig codWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig cowWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig dolphinWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig donkeyWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig foxWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig glowSquidWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig goatWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig horseWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig ironGolemWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig llamaWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig mooshroomWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig muleWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig ocelotWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig pandaWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig parrotWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig pigWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig polarBearWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig rabbitWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig salmonWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig sheepWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig squidWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig striderWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig tropicalFishWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig turtleWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig villagerWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig wanderingTraderWeakResist = new ElementConfig();
	
	@ConfigEntry.Category("Elements")
	@ConfigEntry.Gui.CollapsibleObject
	public ElementConfig wolfWeakResist = new ElementConfig();
	
	
	
	public static class ElementConfig
	{
		public float none = 0f;
		public float fire = 0f;
		public float ice = 0f;	
		public float lightning = 0f;
		public float wind = 0f;
		public float water = 0f;
		public float poison = 0f;
		public float holy = 0f;
		public float dark = 0f;
		public float gravity = 0f;

		public ArrayList<Pair<ElementalAspect, Float>> getPairList()
		{
			ArrayList<Pair<ElementalAspect, Float>> list = new ArrayList<Pair<ElementalAspect, Float>>();
			list.add(new Pair<ElementalAspect, Float>(ElementalAspect.NONE, none));
			list.add(new Pair<ElementalAspect, Float>(ElementalAspect.FIRE, fire));
			list.add(new Pair<ElementalAspect, Float>(ElementalAspect.ICE, ice));
			list.add(new Pair<ElementalAspect, Float>(ElementalAspect.LIGHTNING, lightning));
			list.add(new Pair<ElementalAspect, Float>(ElementalAspect.WIND, wind));
			list.add(new Pair<ElementalAspect, Float>(ElementalAspect.WATER, water));
			list.add(new Pair<ElementalAspect, Float>(ElementalAspect.POISON, poison));
			list.add(new Pair<ElementalAspect, Float>(ElementalAspect.HOLY, holy));
			list.add(new Pair<ElementalAspect, Float>(ElementalAspect.DARK, dark));
			list.add(new Pair<ElementalAspect, Float>(ElementalAspect.GRAVITY, gravity));

			return list;
		}
	}
}
