package lafolie.fmc.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.yaml.snakeyaml.Yaml;

import lafolie.fmc.core.particle.CrystalSparkles;
import lafolie.fmc.core.particle.TextBillboardParticle;
import lafolie.fmc.core.particle.system.ParticleAgent;
import lafolie.fmc.core.particle.system.ParticleSystem;
import lafolie.fmc.core.util.FMCIdentifier;
import lafolie.fmc.core.util.IO;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public final class Particles
{
	private static String PARTICLES_PATH = "assets/final-minecraft/particleSystems/";

	@Environment(EnvType.CLIENT)
	private static Map<String, ParticleSystem> PARTICLE_SYSTEMS = new HashMap<>();

	public static final DefaultParticleType TEXT = FabricParticleTypes.simple(true);
	public static final DefaultParticleType CRYSTAL_SPARKLES = FabricParticleTypes.simple();
	
	private static final HashMap<String, Identifier> particleIDs = new HashMap<>();

	public static void init()
	{
		register("text", TEXT);
		register("crystal_sparkles", CRYSTAL_SPARKLES);
	}

	private static void register(String name, DefaultParticleType type)
	{
		Identifier id = FMCIdentifier.contentID(name);
		Registry.register(Registry.PARTICLE_TYPE, id, type);
		particleIDs.put(name, id);
	}

	public static ParticleType<?> getParticleType(String name)
	{
		return Registry.PARTICLE_TYPE.get(particleIDs.get(name));
	}

	@Environment(EnvType.CLIENT)
	public static void initClient()
	{
		// ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register((atlastTexture, registry) -> 
		// {
		// 	registry.register(FMCIdentifier.contentID("particle/crystal_sparkles"));
		// });

		ParticleFactoryRegistry.getInstance().register(TEXT, TextBillboardParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(CRYSTAL_SPARKLES, CrystalSparkles.Factory::new);
	}

	@Environment(EnvType.CLIENT)
	public ParticleAgent createAgent(String name, Entity entity)
	{
		return PARTICLE_SYSTEMS.get(name).createAgent(entity);
	}

	@Environment(EnvType.CLIENT)
	public ParticleAgent createAgent(String name, BlockPos pos)
	{
		return PARTICLE_SYSTEMS.get(name).createAgent(pos);
	}

	@Environment(EnvType.CLIENT)
	public static void loadParticleSystems()
	{
		Path dirPath = FinalMinecraft.MOD.findPath(PARTICLES_PATH).get();
		List<Path> files = null;
		try
		{
			files = Files.walk(dirPath).filter(p -> p.toString().endsWith(".yaml")).collect(Collectors.toList());
		} catch (IOException e)
		{
			FinalMinecraft.LOG.error("Could not locate particleSystems");
			e.printStackTrace();
		}

		assert files != null : "Erorr loading particle systems";

		for(Path path : files)
		{
			List<Object> settings = IO.readYaml(path);
			String name = FilenameUtils.removeExtension(path.getFileName().toString());
			PARTICLE_SYSTEMS.put(name, new ParticleSystem(settings));
		}

	}
}
