package lafolie.fmc.core;

import lafolie.fmc.core.screen.HomeCrystalScreen;
import lafolie.fmc.core.screen.HomeCrystalScreenHandler;
import lafolie.fmc.core.util.FMCIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;

public final class FMCScreens
{
	// public static final ScreenHandlerType<HomeCrystalScreenHandler> HOME_CRYSTAL_SCREEN_HANDLER = ;
	public static final ScreenHandlerType<HomeCrystalScreenHandler> HOME_CRYSTAL_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, FMCIdentifier.contentID("home_crystal_block"), new ScreenHandlerType<>(HomeCrystalScreenHandler::new));

	public static void init()
	{
	}
	
	@Environment(EnvType.CLIENT)
	public static void initClient()
	{
		FinalMinecraft.LOG.info("Blah");

		HandledScreens.register(HOME_CRYSTAL_SCREEN_HANDLER, HomeCrystalScreen::new);
	}
}
