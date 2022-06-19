package lafolie.fmc.core.util;

import net.minecraft.util.Identifier;

public abstract class FMCIdentifier
{
	private static String modid = "final-minecraft";
	private static String common = "c";
	private static String packetSuffix = "pkt";

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
	 * @return final-minecraft:[name]pkt
	 */
	public static final Identifier packetID(String name)
	{
		return new Identifier(modid, name.concat(packetSuffix));
	}
}
