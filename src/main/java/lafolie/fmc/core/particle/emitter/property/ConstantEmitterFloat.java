package lafolie.fmc.core.particle.emitter.property;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Emitter property configured via YAML.
 * Returns a constant float value
 */
@Environment(EnvType.CLIENT)
public class ConstantEmitterFloat implements EmitterFloat
{
	private final float value;

	public ConstantEmitterFloat(float value)
	{
		this.value = value;
	}

	@Override
	public float get()
	{
		return value;
	}
}
