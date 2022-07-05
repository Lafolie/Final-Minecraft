package lafolie.fmc.core.particle.emitter.property;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Vec3f;

/**
 * Emitter property configured via YAML.
 * Returns a random Vec3f value in the specified cuboid
 */
@Environment(EnvType.CLIENT)
public class UniformEmitterVector implements EmitterVector
{
	private final EmitterFloat x;
	private final EmitterFloat y;
	private final EmitterFloat z;
	private final Vec3f vec = new Vec3f(0f, 0f, 0f);

	public UniformEmitterVector(float minX, float maxX, float minY, float maxY, float minZ, float maxZ)
	{
		x = new UniformEmitterFloat(minX, maxX);
		y = new UniformEmitterFloat(minY, maxY);
		z = new UniformEmitterFloat(minZ, maxZ);
	}

	@Override
	public Vec3f get()
	{
		vec.set(x.get(), y.get(), z.get());
		return vec;
	}
}
