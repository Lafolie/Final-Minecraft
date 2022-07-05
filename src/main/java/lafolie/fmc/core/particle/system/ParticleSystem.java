package lafolie.fmc.core.particle.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import lafolie.fmc.core.particle.emitter.ParticleEmitter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class ParticleSystem
{
	private final List<ParticleEmitter> emitters = new ArrayList<>();
	private final Map<ParticleAgent, Burst> bursts = new WeakHashMap<>();
	private final int numEmitters;

	/*
	 * Keeps the particle system in the ticker list for a short
	 * time to prevent excessive adds/removals
	 */
	private int removeTime = 0;

	/**
	 * Container for individual bursts
	 */
	private class Burst
	{
		public final Vec3f offset;
		public final boolean loop;
		public float time = 0;
		public final float[] delays;
		public final float[] rates;
		public final Vec3d params;
		public float loopTime = 0;

		protected Burst(Vec3f offset, boolean loop, Vec3d params)
		{
			this.offset = offset;
			this.loop = loop;
			this.params = params;
			delays = new float[numEmitters];
			rates = new float[numEmitters];
			for(int n = 0; n < numEmitters; n++)
			{
				ParticleEmitter emitter = emitters.get(n);
				float delay = emitter.delay.get();
				float rate = emitter.emissionRate.get();
				delays[n] = delay;
				rates[n] = rate;
				rate += delay;
				loopTime = rate > loopTime ? rate : loopTime;
			}
		}
	}

	public ParticleSystem(List<Object> emitterSettings)
	{
		for(Object settings : emitterSettings)
		{
			emitters.add(new ParticleEmitter(settings));
		}
		numEmitters = emitters.size();
	}

	public int tick(ClientWorld world)
	{
		for(Iterator<Map.Entry<ParticleAgent,Burst>> iter = bursts.entrySet().iterator(); iter.hasNext();)
		{
			Map.Entry<ParticleAgent, Burst> entry = iter.next();
			ParticleAgent agent = entry.getKey();
			Burst burst = entry.getValue();
			burst.time += 1;
			float time = burst.time;

			for(int n = 0; n < numEmitters; n++)
			{
				float adjustedTime = time - burst.delays[n];
				if(adjustedTime > 0 && adjustedTime % burst.rates[n] < 1)
				{
					ParticleEmitter emitter = emitters.get(n);
					burst.rates[n] = emitter.emissionRate.get();
					Vec3f offset = emitter.initialLocation.get();
					offset.add(burst.offset);
					for(int i = 0; i < emitter.emissionCount.get(); i++)
					{
						Vec3d position = agent.getPosition().add(offset.getX(), offset.getY(), offset.getZ());
						emitter.emit(world, position, burst.params);
					}
				}
			}

			if(time >= burst.loopTime && !burst.loop)
			{
				iter.remove();
			}
		}

		removeTime -= bursts.size() == 0 ? 1 : 0;
		return removeTime;
	}

	public ParticleAgent createAgent(Entity entity)
	{
		return new EntityParticleAgent(entity, this);
	}

	public ParticleAgent createAgent(BlockPos pos)
	{
		return new BlockParticleAgent(pos, this);
	}

	public void play(ParticleAgent agent, Vec3f offset)
	{
		if(!bursts.containsKey(agent))
		{
			removeTime = 20;
			bursts.put(agent, new Burst(offset, true, new Vec3d(0d, 0d, 0d)));
			ParticleSystemTicker.addParticleSystem(this);
		}
	}
}
