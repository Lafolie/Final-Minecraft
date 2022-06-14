package lafolie.fmc.core.internal;

import lafolie.fmc.core.particles.TextBillboardParticle;
import lafolie.fmc.core.particles.TextBillboardParticle.Factory;
import lafolie.fmc.core.util.FMCIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public abstract class Particles
{
	public static final DefaultParticleType TEXT = FabricParticleTypes.simple();

	public static void init()
	{
		Registry.register(Registry.PARTICLE_TYPE, FMCIdentifier.contentID("text"), TEXT);
	}

	@Environment(EnvType.CLIENT)
	public static void initClient()
	{
		ParticleFactoryRegistry.getInstance().register(TEXT, TextBillboardParticle.Factory::new);
	}
}
