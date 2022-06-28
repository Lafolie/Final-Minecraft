package lafolie.fmc.core.util;

import java.util.UUID;

import com.eliotlash.mclib.utils.MathHelper;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.util.math.Vec2f;

public final class Maths
{
	public static Vec2f indexToUV(double index, int gridSize)
	{
		return new Vec2f((float)(index % gridSize), (float)Math.floor(index / gridSize));
	}

	public static byte hashUUIDByte(UUID id)
	{
		short s_xor = hashUUIDShort(id);

		byte b_lsb = (byte)s_xor;
		byte b_msb = (byte)(s_xor >>> 8);
		
		return (byte)(b_lsb ^ b_msb);
	}

	public static short hashUUIDShort(UUID id)
	{
		long l_lsb = id.getLeastSignificantBits();
		long l_msb = id.getMostSignificantBits();
		long l_xor = l_lsb ^ l_msb;

		int i_lsb = (int)l_xor;
		int i_msb = (int)(l_xor >>> 32);
		int i_xor = i_lsb ^ i_msb;

		short s_lsb = (short)i_xor;
		short s_msb = (short)(i_xor >>> 16);
		return (short)(s_lsb ^ s_msb);
	}
}
