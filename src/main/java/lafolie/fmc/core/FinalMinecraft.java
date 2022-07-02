package lafolie.fmc.core;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import java.util.Locale;
import java.util.logging.LogManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.util.internal.logging.Log4J2LoggerFactory;
import lafolie.fmc.core.chrono.DateTime;
import lafolie.fmc.core.config.FMCConfig;
import lafolie.fmc.core.entity.AnniversaryEntity;
import lafolie.fmc.core.internal.elements.ElementalEntityTags;
import lafolie.fmc.core.internal.elements.ElementalItemTags;
import lafolie.fmc.core.util.AlBhed;
import lafolie.fmc.core.util.ServerStatus;
import lafolie.fmc.core.zodiac.BirthsignEntity;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public final class FinalMinecraft implements ModInitializer
{
	public static final byte VERSION_MAJOR = 0;
	public static final byte VERSION_MINOR = 1;
	public static final byte VERSION_REVISION = 0;
	public static final String VERSION_CODENAME = "Biggs"; //convention = FFVI characters in order of appearance

	public static final Logger LOG = LoggerFactory.getLogger("Final Minecraft");

	private static FMCConfig config;

	private static ServerStatus serverStatus = ServerStatus.INIT;

	public static String getVersionString()
	{
		return String.format(Locale.ROOT, "%d.%d.%d \"%s\"", VERSION_MAJOR, VERSION_MINOR, VERSION_REVISION, VERSION_CODENAME);
	}

	@Override
	public void onInitialize()
	{
		LOG.info("Loaded FMC Core version {}", getVersionString());
		LOG.info(AlBhed.toAlBhed("Welcome to Final Minecraft!"));
		initConfig();
		initContent();

		
		// RegistryEntryAddedCallback.event(Registry.BLOCK).register((rawId, id, block) -> {log.info("hello");});
		ServerLifecycleEvents.SERVER_STARTING.register(FinalMinecraft::onServerStarting);
		ServerLifecycleEvents.SERVER_STARTED.register(FinalMinecraft::onServerStarted);
		ServerLifecycleEvents.SERVER_STOPPING.register(FinalMinecraft::onServerStopping);
		ServerLifecycleEvents.SERVER_STOPPED.register(FinalMinecraft::onServerStopped);
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(FinalMinecraft::onEndDataPackReload);

		ServerEntityEvents.ENTITY_LOAD.register(FinalMinecraft::onEntitySpawned);
	}

	// ------------------------------------------------------------------------
	// Entity Callbacks
	// ------------------------------------------------------------------------

	private static void onEntitySpawned(Entity entity, ServerWorld world)
	{
		if(entity instanceof PlayerEntity)
		{
			((AnniversaryEntity)entity).tryInit(world);
			LOG.info("My birthday is {}", ((AnniversaryEntity)entity).getBirthday().getDateString());
			LOG.info("My joinday is {}", ((AnniversaryEntity)entity).getJoinday().getDateString());
			LOG.info("Today is {}", (new DateTime(world).getDateString()));

		}

		if(entity instanceof BirthsignEntity)
		{
			((BirthsignEntity)entity).init();
		}
	}

	// ------------------------------------------------------------------------
	// Server Callbacks
	// ------------------------------------------------------------------------

	private static void onServerStarting(MinecraftServer server)
	{
		serverStatus = ServerStatus.STARTING;
	}
	private static void onServerStarted(MinecraftServer server)

	{
		serverStatus = ServerStatus.STARTED;
		ElementalItemTags.clearCache();
		ElementalEntityTags.clearCache();
	}

	private static void onServerStopping(MinecraftServer server)
	{
		serverStatus = ServerStatus.STOPPING;
	}

	private static void onServerStopped(MinecraftServer server)
	{
		serverStatus = ServerStatus.STOPPED;
		ElementalItemTags.clearCache();
		ElementalEntityTags.clearCache();
	}

	private static void onEndDataPackReload(MinecraftServer server, ResourceManager resourceManager, boolean success)
	{
		ElementalItemTags.clearCache();
		ElementalEntityTags.clearCache();
	}

	// ------------------------------------------------------------------------
	// INITS
	// ------------------------------------------------------------------------

	private static void initConfig()
	{
		AutoConfig.register(FMCConfig.class, JanksonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(FMCConfig.class).getConfig();
	}

	public static FMCConfig getConfig()
	{
		return config;
	}

	private static void initContent()
	{
		FMCScreens.init();
		FMCItems.init();
		FMCBlocks.init();
		Particles.init();
	}

	// @Override
	// public void onEntryAdded(int rawId, net.minecraft.util.Identifier id, Item object)
	// {
	// 	System.out.print(id.toString());
	// 	log.info(id.toString());
		
	// }
}
