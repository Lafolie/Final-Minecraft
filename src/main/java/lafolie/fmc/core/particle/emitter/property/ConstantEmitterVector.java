package lafolie.fmc.core.particle.emitter.property;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Vec3f;

/**
 * Emitter property configured via YAML.
 * Returns a constant Vec3f value
 */
@Environment(EnvType.CLIENT)
public class ConstantEmitterVector implements EmitterVector
{
	private final Vec3f vec;
	
	public ConstantEmitterVector(float x, float y, float z)
	{
		vec = new Vec3f(x, y, z);
	}

	@Override
	public Vec3f get()
	{
		return vec;
	}
}
