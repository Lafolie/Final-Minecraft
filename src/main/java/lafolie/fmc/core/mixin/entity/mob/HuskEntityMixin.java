package lafolie.fmc.core.mixin.entity.mob;

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
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

@Mixin(HuskEntity.class)
public abstract class HuskEntityMixin
{
	@Inject(at = @At("TAIL"), method = "<init>")
	public void Constructor(EntityType<? extends HuskEntity> entityType, World world, CallbackInfo info)
	{
		ElementalEntity self = (ElementalEntity)this;

		FMCConfig config =  FinalMinecraft.getConfig();
		ArrayList<Pair<ElementalAspect, Float>> baseElements = config.huskWeakResist.getPairList();

		self.initElementalEntity(baseElements);
	}
}
