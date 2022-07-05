package lafolie.fmc.core.particle.emitter;

import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Final;

import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.Particles;
import lafolie.fmc.core.particle.emitter.property.ConstantEmitterFloat;
import lafolie.fmc.core.particle.emitter.property.ConstantEmitterVector;
import lafolie.fmc.core.particle.emitter.property.EmitterFloat;
import lafolie.fmc.core.particle.emitter.property.EmitterVector;
import lafolie.fmc.core.particle.emitter.property.UniformBoxEmitterVector;
import lafolie.fmc.core.particle.emitter.property.UniformEmitterFloat;
import lafolie.fmc.core.particle.emitter.property.UniformEmitterVector;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class ParticleEmitter
{
	public final ParticleEffect particle;
	public final boolean alwaysSpawn = false;

	/**
	 * Generic particle spawn parameter
	 */
	public final EmitterFloat paramA;

	/**
	 * Generic particle spawn parameter
	 */
	public final EmitterFloat paramB;

	/**
	 * Generic particle spawn parameter
	 */
	public final EmitterFloat paramC;

	/**
	 * Ticks to delay before first emission
	 */
	public final EmitterFloat delay;

	/**
	 * Ticks per emission
	 */
	public final EmitterFloat emissionRate;

	/**
	 * Particles per emission
	 */
	public final EmitterFloat emissionCount;

	/**
	 * Initial location relative to the agent
	 */
	public final EmitterVector initialLocation;

	@SuppressWarnings("unchecked")
	public ParticleEmitter(Object settings)
	{
		Map<String, Object> settingsMap = (Map<String, Object>)settings;
		particle = (ParticleEffect)Particles.getParticleType((String)settingsMap.get("particle"));

		delay = parseFloat(settingsMap, "delay", 0f);
		emissionRate = parseFloat(settingsMap, "rate", 1f);
		emissionCount = parseFloat(settingsMap, "count", 1f);
		initialLocation = parseVector(settingsMap, "position");
		paramA = parseFloat(settingsMap, "paramA", 0f);
		paramB = parseFloat(settingsMap, "paramB", 0f);
		paramC = parseFloat(settingsMap, "paramC", 0f);
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

	public void emit(World world, Vec3d position, Vec3d params)
	{
		double a = params.x >= 0 ? params.x : paramA.get();
		double b = params.y >= 0 ? params.y : paramB.get();
		double c = params.z >= 0 ? params.z : paramC.get();
		FinalMinecraft.LOG.info("Spawing particle! {} {} {} {}", position, a, b, c);
		world.addParticle(particle, alwaysSpawn, position.x, position.y, position.z, a, b, c);
	}
}
