package lafolie.fmc.core.particle.emitter.property;

import java.util.function.Function;

import lafolie.fmc.core.util.Maths;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

/**
 * Emitter property configured via YAML.
 * Returns a random Vec3f value in the specified cuboid,
 * aligned to the axis extremes
 */
@Environment(EnvType.CLIENT)
public class UniformBoxEmitterVector implements EmitterVector
{
	private final UniformEmitterFloat x;
	private final UniformEmitterFloat y;
	private final UniformEmitterFloat z;
	private final UniformFloat[] axes;
	private final Vec3f vec = new Vec3f(0f, 0f, 0f);

	@FunctionalInterface
	private interface UniformFloat
	{
		float get();
	}

	public UniformBoxEmitterVector(float minX, float maxX, float minY, float maxY, float minZ, float maxZ)
	{
		x = new UniformEmitterFloat(minX, maxX);
		y = new UniformEmitterFloat(minY, maxY);
		z = new UniformEmitterFloat(minZ, maxZ);
		axes = new UniformFloat[]
		{
			x::getExtreme,
			y::get,
			z::get,

			x::get,
			y::getExtreme,
			z::get,

			x::get,
			y::get,
			z::getExtreme
		};

	}

	@Override
	public Vec3f get()
	{
		int axis = Maths.random.nextInt(3) * 3;
		vec.set(axes[axis].get(), axes[axis + 1].get(), axes[axis + 2].get());
		return vec;
	}
}
