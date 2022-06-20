package lafolie.fmc.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;

import lafolie.fmc.core.chrono.Daypart;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

@Mixin(World.class)
public abstract class WorldMixin
{
	@Shadow
	public abstract long getTimeOfDay();

	// @Inject()
}
