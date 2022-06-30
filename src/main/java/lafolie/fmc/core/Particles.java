package lafolie.fmc.core;

import lafolie.fmc.core.particles.CrystalSparkles;
import lafolie.fmc.core.particles.TextBillboardParticle;
import lafolie.fmc.core.util.FMCIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.registry.Registry;

public abstract class Particles
{
	public static final DefaultParticleType TEXT = FabricParticleTypes.simple(true);
	public static final DefaultParticleType CRYSTAL_SPARKLES = FabricParticleTypes.simple();

	public static void init()
	{
		Registry.register(Registry.PARTICLE_TYPE, FMCIdentifier.contentID("text"), TEXT);
		Registry.register(Registry.PARTICLE_TYPE, FMCIdentifier.contentID("crystal_sparkles"), CRYSTAL_SPARKLES);
	}

	@Environment(EnvType.CLIENT)
	public static void initClient()
	{
		ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register((atlastTexture, registry) -> 
		{
			registry.register(FMCIdentifier.contentID("particle/crystal_sparkles"));
		});

		ParticleFactoryRegistry.getInstance().register(TEXT, TextBillboardParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(CRYSTAL_SPARKLES, CrystalSparkles.Factory::new);
	}
}
