package lafolie.fmc.core.particle.system;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public abstract class ParticleAgent
{
	private final ParticleSystem system;

	public ParticleAgent(ParticleSystem system)
	{
		this.system = system;
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
	public void play(Vec3d offset)
	{
		
	}

		/**
		 * Starts the particle system
		 * @param args particle system parameters
		 */
		public void play(double... params)
		{
	
		}

	/**
	 * Starts the particle system with an offset
	 * @param offset spawn offset
	 * @param params particle system parameters
	 */
	public void play(Vec3d offset, double... params)
	{
		
	}

	/*
	 * Stops the particle system
	 */
	public void stop()
	{

	}


	
	/*
	* Play the particle system without looping
	*/
	public void burst()
	{
		burst(0d, 0d, 0d);
	}
	
	/**
	 * Play the particle system without looping
	 * with an offset
	 * @param offset spawn offset
	 */
	public void burst(Vec3d offset)
	{
		burst(offset, 0d, 0d, 0d);
	}

	
	/**
	 * Play the particle system without looping
	 * with an offset
	 * @param params particle system parameters
	 */
	public void burst(double... params)
	{
		
	}

	/**
	 * Play the particle system without looping
	 * with an offset
	 * @param offset spawn offset
	 * @param params particle system parameters
	 */
	public void burst(Vec3d offset, double... params)
	{
		Vec3d pos = getPosition().add(offset);
	}
	
	/**
	 * Called internally to get the position of the
	 * agent owner
	 * @return position of the owner
	 */
	public Vec3d getPosition()
	{
		return null;
	}
}
