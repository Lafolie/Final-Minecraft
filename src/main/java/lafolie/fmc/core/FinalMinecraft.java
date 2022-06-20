package lafolie.fmc.core;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lafolie.fmc.core.chrono.DateTime;
import lafolie.fmc.core.config.FMCConfig;
import lafolie.fmc.core.entity.AnniversaryEntity;
import lafolie.fmc.core.internal.Particles;
import lafolie.fmc.core.internal.elements.ElementalEntityTags;
import lafolie.fmc.core.internal.elements.ElementalItemTags;
import lafolie.fmc.core.util.AlBhed;
import lafolie.fmc.core.util.ServerStatus;
import lafolie.fmc.core.zodiac.BirthsignEntity;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class FinalMinecraft implements ModInitializer
{
	public static final byte VERSION_MAJOR = 0;
	public static final byte VERSION_MINOR = 1;
	public static final byte VERSION_REVISION = 0;
	public static final String VERSION_CODENAME = "Biggs"; //convention = FFVI characters in order of appearance

	public static final Logger log = LoggerFactory.getLogger("FMC Core");

	private static FMCConfig config;

	private static ServerStatus serverStatus = ServerStatus.INIT;

	public static String getVersionString()
	{
		return String.format("%d.%d.%d \"%s\"", VERSION_MAJOR, VERSION_MINOR, VERSION_REVISION, VERSION_CODENAME);
	}

	@Override
	public void onInitialize()
	{
		log.info("Loaded FMC Core version {}", getVersionString());
		log.info(AlBhed.toAlBhed("Welcome to Final Minecraft!"));
		initConfig();
		initContent();

		// RegistryEntryAddedCallback.event(Registry.BLOCK).register((rawId, id, block) -> {log.info("hello");});
		ServerLifecycleEvents.SERVER_STARTING.register(server -> onServerStarting(server));
		ServerLifecycleEvents.SERVER_STARTED.register(server -> onServerStarted(server));
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> onServerStopping(server));
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> onServerStopped(server));
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, res, succ) -> onEndDataPackReload(server, res, succ));

		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> onEntitySpawned(entity, world));
	}

	// ------------------------------------------------------------------------
	// Entity Callbacks
	// ------------------------------------------------------------------------

	private void onEntitySpawned(Entity entity, ServerWorld world)
	{
		if(entity instanceof PlayerEntity)
		{
			((AnniversaryEntity)entity).tryInit(world);
			log.info("My birthday is {}", ((AnniversaryEntity)entity).getBirthday().getDateString());
			log.info("My joinday is {}", ((AnniversaryEntity)entity).getJoinday().getDateString());
			log.info("Today is {}", (new DateTime(world).getDateString()));

		}

		if(entity instanceof BirthsignEntity)
		{
			((BirthsignEntity)entity).init();
		}
	}

	// ------------------------------------------------------------------------
	// Server Callbacks
	// ------------------------------------------------------------------------

	private void onServerStarting(MinecraftServer server)
	{
		serverStatus = ServerStatus.STARTING;
	}
	private void onServerStarted(MinecraftServer server)

	{
		serverStatus = ServerStatus.STARTED;
		ElementalItemTags.clearCache();
		ElementalEntityTags.clearCache();
	}

	private void onServerStopping(MinecraftServer server)
	{
		serverStatus = ServerStatus.STOPPING;
	}

	private void onServerStopped(MinecraftServer server)
	{
		serverStatus = ServerStatus.STOPPED;
		ElementalItemTags.clearCache();
		ElementalEntityTags.clearCache();
	}

	private void onEndDataPackReload(MinecraftServer server, ResourceManager resourceManager, boolean success)
	{
		ElementalItemTags.clearCache();
		ElementalEntityTags.clearCache();
	}

	// ------------------------------------------------------------------------
	// INITS
	// ------------------------------------------------------------------------

	private void initConfig()
	{
		AutoConfig.register(FMCConfig.class, JanksonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(FMCConfig.class).getConfig();
	}

	public static FMCConfig getConfig()
	{
		return config;
	}

	private void initContent()
	{
		Particles.init();
	}

	// @Override
	// public void onEntryAdded(int rawId, net.minecraft.util.Identifier id, Item object)
	// {
	// 	System.out.print(id.toString());
	// 	log.info(id.toString());
		
	// }
}
