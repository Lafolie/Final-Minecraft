package lafolie.fmc.core.particle.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

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
	private final int numEmitters;

	/**
	 * playBursts loop and can last indefinitely. One per agent
	 */
	private final Map<ParticleAgent, Burst> playBursts = new WeakHashMap<>();

	/**
	 * bursts are 1-shot effects, can have many per agent
	 */
	private final Multimap<ParticleAgent, Burst> bursts = HashMultimap.create();

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
		public float loopTime = 0;
		public final Map<String, Double> namedParams;

		protected Burst(Vec3f offset, boolean loop, Map<String, Double> params)
		{
			this.offset = offset;
			this.loop = loop;
			namedParams = new HashMap<>(params);

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
		tickAll(world, playBursts.entrySet().iterator());
		tickAll(world, bursts.entries().iterator());

		removeTime -= playBursts.size() + bursts.size() == 0 ? 1 : 0;
		return removeTime;
	}

	private void tickAll(ClientWorld world, Iterator<Map.Entry<ParticleAgent, Burst>> iter)
	{
		while(iter.hasNext())
		{
			Map.Entry<ParticleAgent, Burst> entry = iter.next();
			ParticleAgent agent = entry.getKey();
			Burst burst = entry.getValue();
			tickBurst(world, agent, burst);
			if(burst.time >= burst.loopTime && !burst.loop)
			{
				iter.remove();
			}
		}
	}

	private void tickBurst(ClientWorld world, ParticleAgent agent, Burst burst)
	{
		burst.time += 1;

		for(int n = 0; n < numEmitters; n++)
		{
			float adjustedTime = burst.time - burst.delays[n];
			if(adjustedTime > 0 && adjustedTime % burst.rates[n] < 1)
			{
				ParticleEmitter emitter = emitters.get(n);
				burst.rates[n] = emitter.emissionRate.get();
				Vec3f offset = emitter.initialLocation.get();
				offset.add(burst.offset);
				for(int i = 0; i < emitter.emissionCount.get(); i++)
				{
					Vec3d position = agent.getPosition().add(offset.getX(), offset.getY(), offset.getZ());
					emitter.emit(world, position, burst.namedParams);
				}
			}
		}
	}

	private void activateSystem()
	{
		removeTime = 20;
		ParticleSystemTicker.addParticleSystem(this);
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
		if(!playBursts.containsKey(agent))
		{
			playBursts.put(agent, new Burst(offset, true, agent.getNamedParams()));
			activateSystem();
		}
	}

	public void stop(ParticleAgent agent)
	{
		playBursts.remove(agent);
	}

	public void burst(ParticleAgent agent, Vec3f offset)
	{
		bursts.put(agent, new Burst(offset, false, agent.getNamedParams()));
		activateSystem();
	}

	public void clearAll()
	{
		playBursts.clear();
		bursts.clear();
	}
}
