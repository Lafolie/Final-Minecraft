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
	private final float x;
	private final float y;
	private final float z;
	private final Vec3f vec;
	
	public ConstantEmitterVector(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		vec = new Vec3f(x, y, z);
	}

	@Override
	public Vec3f get()
	{
		vec.set(x, y, z);
		return vec;
	}
}
