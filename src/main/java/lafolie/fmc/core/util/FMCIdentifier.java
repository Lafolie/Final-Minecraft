package lafolie.fmc.core.util;

import net.minecraft.util.Identifier;

public abstract class FMCIdentifier
{
	private static String modid = "final-minecraft";
	private static String common = "c";
	private static String packetSuffix = "_pkt";
	private static String componentSuffix = "_cmp";
	private static String animPrefix = "animations/";
	private static String geoPrefix = "geo/";

	/**
	 * ID for general content
	 * @param name
	 * @return final-minecraft:[name]
	 */
	public static final Identifier contentID(String name)
	{
		return new Identifier(modid, name);
	}

	/**
	 * ID for common tag format
	 * @param name
	 * @return c:[name]
	 */
	public static final Identifier commonTagID(String name)
	{
		return new Identifier(common, name);
	}

	/**
	 * ID for network packets
	 * @param name
	 * @return final-minecraft:[name]_pkt
	 */
	public static final Identifier packetID(String name)
	{
		return new Identifier(modid, name.concat(packetSuffix));
	}

	/**
	 * ID for cardinal components
	 * @param name
	 * @return final-minecraft:[name]_cmp
	 */
	public static final Identifier componentID(String name)
	{
		return new Identifier(modid, name.concat(componentSuffix));
	}

	/**
	 * ID for GeckoLib animations
	 * @param name
	 * @return final-minecraft:animation/[name]
	 */
	public static final Identifier animationID(String name)
	{
		return new Identifier(modid, animPrefix + name);
	}

	/**
	 * ID for GeckoLib models
	 * @param name
	 * @return final-minecraft:geo/[name]
	 */
	public static final Identifier geometryID(String name)
	{
		return new Identifier(modid, geoPrefix + name);
	}
}
