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
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class ParticleSystem
{
	private List<ParticleEmitter> emitters = new ArrayList<>();
	private Map<ParticleAgent, AgentListing> agents = new WeakHashMap<>();

	private class AgentListing
	{
		public final Vec3d offset;
		public final boolean loop;
		public float time = 0;

		public AgentListing(Vec3d offset, boolean loop)
		{
			this.offset = offset;
			this.loop = loop;
		}
	}

	public ParticleSystem(List<Object> emitterSettings)
	{
		for(Object settings : emitterSettings)
		{
			emitters.add(new ParticleEmitter(settings));
		}
	}

	public int tick(ClientWorld world)
	{
		for(Iterator<Map.Entry<ParticleAgent,AgentListing>> iter = agents.entrySet().iterator(); iter.hasNext();)
		{
			Map.Entry<ParticleAgent, AgentListing> entry = iter.next();
			ParticleAgent agent = entry.getKey();
			AgentListing listing = entry.getValue();
			listing.time += 1;
			if(true)
			{
				iter.remove();
			}
		}

		return agents.size();
	}

	public ParticleAgent createAgent(Entity entity)
	{
		return new EntityParticleAgent(entity, this);
	}

	public ParticleAgent createAgent(BlockPos pos)
	{
		return new BlockParticleAgent(pos, this);
	}

	public void play(ParticleAgent agent, Vec3d offset)
	{
		if(!agents.containsKey(agent))
		{
			agents.put(agent, new AgentListing(offset, true));
			ParticleSystemTicker.addParticleSystem(this);
		}
	}
}
