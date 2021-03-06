package lafolie.fmc.core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import lafolie.fmc.core.element.ElementalAspect;
import lafolie.fmc.core.element.ElementalObject;
import lafolie.fmc.core.entity.DamageNumbers;
import lafolie.fmc.core.internal.network.HealthModifiedPacket;
import lafolie.fmc.core.particle.system.ParticleAgentProvider;
import lafolie.fmc.core.particle.system.ParticleSystemTicker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public final class FinalMinecraftClient implements ClientModInitializer
{
	private static NativeImageBackedTexture flashLUT;

	@Override
	public void onInitializeClient()
	{
		FMCBlocks.initClient();

		InitContent();
		registerNetworkReceivers();

		ClientLifecycleEvents.CLIENT_STARTED.register(FinalMinecraftClient::onClientStarted);
		ClientLifecycleEvents.CLIENT_STOPPING.register(FinalMinecraftClient::onClientStopping);
		ClientTickEvents.END_WORLD_TICK.register(FinalMinecraftClient::onEndWorldTick);
		ClientPlayConnectionEvents.DISCONNECT.register(FinalMinecraftClient::onClientDisconnect);

		ClientBlockEntityEvents.BLOCK_ENTITY_LOAD.register(FinalMinecraftClient::onBlockEntityLoaded);
		ClientBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register(FinalMinecraftClient::onBlockEntityUnloaded);

		ClientEntityEvents.ENTITY_LOAD.register(FinalMinecraftClient::onEntityLoaded);
		ClientEntityEvents.ENTITY_UNLOAD.register(FinalMinecraftClient::onEntityUnLoaded);

		//TODO: remove lambda, make function
		ItemTooltipCallback.EVENT.register((stack, context, lines) -> 
		{
			//read NBT and stuff
			ElementalObject elStack = (ElementalObject)(Object)stack;
			
			for(ElementalAspect element : ElementalAspect.values())
			{
				int total = elStack.getWeakResistAffinity(element);
				if(total > 0)
				{
					lines.add(
						new TranslatableText("fmc.core.element.tooltip.elemental",
						new TranslatableText(element.getLangKey())));
						lines.add(new LiteralText(String.format("Total: %s", total))); //TODO REMOVE LATER
				}

			}
		});
	}

	private static void onClientStarted(MinecraftClient client)
	{
		Particles.loadParticleSystems();
	}
	
	private static void onClientStopping(MinecraftClient client)
	{
		FinalMinecraft.LOG.info("STOPPING CLIENT");
	}

	private static void onClientDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client)
	{
		ParticleSystemTicker.clearParticleSystems();
	}

	private static void onEndWorldTick(ClientWorld world)
	{
		ParticleSystemTicker.tick(world);
	}

	private static void onEntityLoaded(Entity entity, ClientWorld world)
	{
		if(entity instanceof ParticleAgentProvider)
		{
			((ParticleAgentProvider)entity).initParticleAgents();
		}
	}

	private static void onEntityUnLoaded(Entity entity, ClientWorld world)
	{
		if(entity instanceof ParticleAgentProvider)
		{
			((ParticleAgentProvider)entity).stopParticleAgents();
		}
	}

	private static void onBlockEntityLoaded(BlockEntity blockEntity, ClientWorld world)
	{
		if(blockEntity instanceof ParticleAgentProvider)
		{
			((ParticleAgentProvider)blockEntity).initParticleAgents();
		}
	}

	private static void onBlockEntityUnloaded(BlockEntity blockEntity, ClientWorld world)
	{
		if(blockEntity instanceof ParticleAgentProvider)
		{
			((ParticleAgentProvider)blockEntity).stopParticleAgents();
		}
	}

	public static NativeImageBackedTexture getDamageLUT()
	{
		if(flashLUT == null)
		{
			loadImages();
		}
		return flashLUT;
	}

	private static void InitContent()
	{
		FMCScreens.initClient();
		Particles.initClient();
		initBlocks();
	}

	//TODO: remove this or reattempt the damage flash stuff
	private static void loadImages()
	{
		ModContainer imgSource = FabricLoader.getInstance().getModContainer("final-minecraft").orElseThrow();
		try
		{
			InputStream io = Files.newInputStream(imgSource.findPath("assets/final-minecraft/textures/etc/damageFlash.png").get(), StandardOpenOption.READ);
			NativeImage img = NativeImage.read(io);
			flashLUT = new NativeImageBackedTexture(img);
		}
		catch (IOException e)
		{
			FinalMinecraft.LOG.error("Could not load file damageFlash.png");
			e.fillInStackTrace();
			e.printStackTrace();
			FinalMinecraft.LOG.error("{}", e.getMessage());
		}
	}

	private static void initBlocks()
	{

		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.CRYSTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.FIRE_CRYSTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.ICE_CRYSTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.THUNDER_CRYSTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.WIND_CRYSTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.WATER_CRYSTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.EARTH_CRYSTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.HOLY_CRYSTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.DARK_CRYSTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.POISON_CRYSTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.GRAVITY_CRYSTAL, RenderLayer.getTranslucent());
	}

	private static void registerNetworkReceivers()
	{
		// HealthModifiedPacket
		ClientPlayNetworking.registerGlobalReceiver(HealthModifiedPacket.ID, (client, handler, buf, responseSender) ->
		{
			// FinalMinecraft.log.info("Receiving packet");

			HealthModifiedPacket packet = new HealthModifiedPacket(buf);
			client.execute(() ->
			{
				DamageNumbers dmgEntity = (DamageNumbers)client.world.getEntityById(packet.entityID);
				if(dmgEntity != null)
				{
					dmgEntity.spawnDamageNumbers(client, packet.amount, packet.type);
				}
				else
				{
					FinalMinecraft.LOG.info("dmgEntity was null");
				}
			});
		});
	}
}
