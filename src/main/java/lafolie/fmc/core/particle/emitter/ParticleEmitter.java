package lafolie.fmc.core.particle.emitter;

import java.util.List;
import java.util.Map;

import lafolie.fmc.core.Particles;
import lafolie.fmc.core.particle.emitter.property.ConstantEmitterFloat;
import lafolie.fmc.core.particle.emitter.property.ConstantEmitterVector;
import lafolie.fmc.core.particle.emitter.property.EmitterFloat;
import lafolie.fmc.core.particle.emitter.property.EmitterVector;
import lafolie.fmc.core.particle.emitter.property.UniformBoxEmitterVector;
import lafolie.fmc.core.particle.emitter.property.UniformEmitterFloat;
import lafolie.fmc.core.particle.emitter.property.UniformEmitterVector;
import lafolie.fmc.core.util.FMCIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class ParticleEmitter
{
	public final ParticleEffect particle;
	public final boolean alwaysSpawn = false;
	public final double defaultA = 0d;
	public final double defaultB = 0d;
	public final double defaultC = 0d;

	/**
	 * Ticks to delay before first emission
	 */
	private final EmitterFloat delay;

	/**
	 * Ticks per emission
	 */
	private final EmitterFloat emissionRate;

	/**
	 * Particles per emission
	 */
	private final EmitterFloat emissionCount;

	/**
	 * Initial location relative to the agent
	 */
	private final EmitterVector initialLocation;

	@SuppressWarnings("unchecked")
	public ParticleEmitter(Object settings)
	{
		Map<String, Object> settingsMap = (Map<String, Object>)settings;
		particle = (ParticleEffect)Particles.getParticleType((String)settingsMap.get("particle"));

		delay = parseFloat(settingsMap, "delay", 0f);
		emissionRate = parseFloat(settingsMap, "rate", 1f);
		emissionCount = parseFloat(settingsMap, "count", 1f);
		initialLocation = parseVector(settingsMap, "position");
	}

	@SuppressWarnings("unchecked")
	private EmitterFloat parseFloat(Map<String, Object> settingsMap, String name, float i)
	{
		if(settingsMap.containsKey(name))
		{
			Map<String, Object> map = (Map<String, Object>)settingsMap.get(name);
			if(map.containsKey("constant"))
			{
				return new ConstantEmitterFloat(((Double)map.get("constant")).floatValue());
			}

			if(map.containsKey("uniform"))
			{
				List<Double> list = (List<Double>)map.get("uniform");
				return new UniformEmitterFloat(list.get(0).floatValue(), list.get(1).floatValue());
			}
		}
		return new ConstantEmitterFloat(i);
	}

	@SuppressWarnings("unchecked")
	private EmitterVector parseVector(Map<String, Object> settingsMap, String name)
	{
		if(settingsMap.containsKey(name))
		{
			Map<String, Object> map = (Map<String, Object>)settingsMap.get(name);
			if(map.containsKey("constant"))
			{
				Map<String, Double> vec = (Map<String, Double>)map.get("constant");
				return new ConstantEmitterVector(vec.get("x").floatValue(), vec.get("y").floatValue(), vec.get("z").floatValue());
			}

			if(map.containsKey("uniform"))
			{
				Map<String, List<Double>> vec = (Map<String, List<Double>>)map.get("constant");
				return new UniformEmitterVector(vec.get("x").get(0).floatValue(), vec.get("x").get(1).floatValue(), vec.get("y").get(0).floatValue(), vec.get("y").get(1).floatValue(), vec.get("z").get(0).floatValue(), vec.get("z").get(1).floatValue());
			}

			if(map.containsKey("box"))
			{
				Map<String, List<Double>> vec = (Map<String, List<Double>>)map.get("box");
				return new UniformBoxEmitterVector(vec.get("x").get(0).floatValue(), vec.get("x").get(1).floatValue(), vec.get("y").get(0).floatValue(), vec.get("y").get(1).floatValue(), vec.get("z").get(0).floatValue(), vec.get("z").get(1).floatValue());
			}
		}
		return new ConstantEmitterVector(0f, 0f, 0f);
	}

	public void emit(World world, Vec3d position)
	{
		emit(world, position, defaultA, defaultB, defaultC);
	}

	public void emit(World world, Vec3d position, double... args)
	{
		double a = args.length > 0 ? args[0] : defaultA;
		double b = args.length > 1 ? args[1] : defaultB;
		double c = args.length > 2 ? args[2] : defaultC;
		emit(world, position, a, b, c);
	}

	public void emit(World world, Vec3d position, double a, double b, double c)
	{
		world.addParticle(particle, alwaysSpawn, position.x, position.y, position.z, a, b, c);
	}
}
