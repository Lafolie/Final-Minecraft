package lafolie.fmc.core.particle.emitter.property;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Emitter property configured via YAML.
 * Returns a random float value in the specified range
 */
@Environment(EnvType.CLIENT)
public class UniformEmitterFloat implements EmitterFloat
{
	private static Random rnd = new Random((long)Math.random());
	private final float min;
	private final float max;

	public UniformEmitterFloat(float min, float max)
	{
		this.min = min;
		this.max = max;
	}

	@Override
	public float get()
	{
		return rnd.nextFloat(min, max);
	}

	public float getExtreme()
	{
		return rnd.nextBoolean() ? min : max;
	}
}
