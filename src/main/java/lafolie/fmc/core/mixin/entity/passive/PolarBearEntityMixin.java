package lafolie.fmc.core.mixin.entity.passive;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.config.FMCConfig;
import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalEntity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

@Mixin(PolarBearEntity.class)
public abstract class PolarBearEntityMixin
{
	@Inject(at = @At("TAIL"), method = "<init>")
	public void Constructor(EntityType<? extends PolarBearEntity> entityType, World world, CallbackInfo info)
	{
		ElementalEntity self = (ElementalEntity)this;

		FMCConfig config =  FinalMinecraft.getConfig();
		ArrayList<Pair<ElementalAspect, Float>> baseElements = config.polarBearWeakResist.getPairList();

		self.initElementalEntity(baseElements);
	}
}
