package lafolie.fmc.core.particle.emitter.property;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * A float property for ParticleEmitters
 */
@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface EmitterFloat
{
	/**
	 * 
	 * @return float value
	 */
	public float get();
}
