package lafolie.fmc.core.mixin;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.chrono.DateTime;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World
{
	protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef,
			RegistryEntry<DimensionType> registryEntry, Supplier<Profiler> profiler, boolean isClient,
			boolean debugWorld, long seed) {
		super(properties, registryRef, registryEntry, profiler, isClient, debugWorld, seed);
		//Auto-generated constructor stub
	}

	private DateTime dt = new DateTime();

	@Inject(at = @At("TAIL"), method = "tickTime()V")
	private void afterTickTime(CallbackInfo info)
	{
		dt.setTime(getTimeOfDay());
		FinalMinecraft.log.info("Time {}", dt.getTicks());
		FinalMinecraft.log.info("\t{}", dt.getDateString());
		FinalMinecraft.log.info("\t{}", dt.getTimeString(false));
	}
}
