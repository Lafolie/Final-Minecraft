package lafolie.fmc.core;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.FabricTagBuilder;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.block.Block;
import net.minecraft.block.IceBlock;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lafolie.fmc.core.config.FMCConfig;
import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.internal.Particles;
import lafolie.fmc.core.internal.elements.ElementalEntityTags;
import lafolie.fmc.core.internal.elements.ElementalItemTags;
import lafolie.fmc.core.util.ServerStatus;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class FinalMinecraft implements ModInitializer//, RegistryEntryAddedCallback<Item>
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
		initConfig();
		initContent();

		// RegistryEntryAddedCallback.event(Registry.BLOCK).register((rawId, id, block) -> {log.info("hello");});
		ServerLifecycleEvents.SERVER_STARTING.register(server -> onServerStarting(server));
		ServerLifecycleEvents.SERVER_STARTED.register(server -> onServerStarted(server));
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> onServerStopping(server));
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> onServerStopped(server));
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, res, succ) -> onEndDataPackReload(server, res, succ));
		// for(ElementalItemTags.TAGS.get(ElementalAspect.ICE).isOf))
	}

	private void initContent()
	{
		Particles.init();
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

	

	// @Override
	// public void onEntryAdded(int rawId, net.minecraft.util.Identifier id, Item object)
	// {
	// 	System.out.print(id.toString());
	// 	log.info(id.toString());
		
	// }
}
