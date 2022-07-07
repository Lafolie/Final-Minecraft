package lafolie.fmc.core.particle.system;

import java.util.HashMap;
import java.util.Map;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public abstract class ParticleAgent
{
	private final ParticleSystem system;
	private final Map<String, Double> namedParameters = new HashMap<>();

	public ParticleAgent(ParticleSystem system)
	{
		this.system = system;
	}

	public void setNamedParameter(String name, double i)
	{
		namedParameters.put(name, i);
	}

	public double getNamedParameter(String name)
	{
		return namedParameters.get(name);
	}

	/*
	 * Starts the particle system
	 */
	public void play()
	{
		system.play(this, new Vec3f(0f, 0f, 0f));
	}

	/**
	 * Starts the particle system with an offset
	 * @param offset spawn offset
	 */
	public void play(Vec3f offset)
	{
		system.play(this, offset);
	}

	/*
	 * Stops the particle system
	 */
	public void stop()
	{
		system.stop(this);
	}

	/**
	* Play the particle system without looping
	* (1-shot effect)
	*/
	public void burst()
	{
		system.burst(this, new Vec3f(0f, 0f, 0f));
	}
	
	/**
	 * Play the particle system without looping
	 * with an offset
	 * (1-shot effect)
	 * @param offset spawn offset
	 */
	public void burst(Vec3f offset)
	{
		system.burst(this, offset);
	}

	/**
	 * Called internally to get the position of the
	 * agent owner
	 * @return position of the owner
	 */
	protected Vec3d getPosition()
	{
		return null;
	}

	/**
	 * Called internally to get the named parameters
	 * @return named parameters
	 */
	protected Map<String, Double> getNamedParams()
	{
		return namedParameters;
	}
}
