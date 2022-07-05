package lafolie.fmc.core.particle.system;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.client.world.ClientWorld;

public final class ParticleSystemTicker
{
	private static Set<ParticleSystem> systems = new HashSet<>();

	private ParticleSystemTicker() {}

	public static void tick(ClientWorld world)
	{
		for(Iterator<ParticleSystem> iter = systems.iterator(); iter.hasNext();)
		{
			ParticleSystem system = iter.next();
			if(system.tick(world) == 0)
			{
				iter.remove();
			}
		}
	}

	public static void addParticleSystem(ParticleSystem system)
	{
		systems.add(system);
	}

	public static void removeParticleSystem(ParticleSystem system)
	{
		systems.remove(system);
	}
}
