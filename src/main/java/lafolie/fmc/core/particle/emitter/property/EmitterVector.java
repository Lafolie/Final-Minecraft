package lafolie.fmc.core.particle.emitter.property;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Vec3f;

/**
 * A Vec3f property for ParticleEmitters
 */
@Environment(EnvType.CLIENT)
public interface EmitterVector
{
	/**
	 * 
	 * @return Vec3f value
	 */
	public Vec3f get();
}
