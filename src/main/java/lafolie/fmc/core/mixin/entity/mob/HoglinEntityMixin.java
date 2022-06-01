package lafolie.fmc.core.mixin.entity.mob;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lafolie.fmc.core.Mod;
import lafolie.fmc.core.config.FMCConfig;
import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalEntity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

@Mixin(HoglinEntity.class)
public abstract class HoglinEntityMixin
{
	@Inject(at = @At("TAIL"), method = "<init>")
	public void Constructor(EntityType<? extends HoglinEntity> entityType, World world, CallbackInfo info)
	{
		ElementalEntity self = (ElementalEntity)this;

		FMCConfig config =  Mod.getConfig();
		ArrayList<Pair<ElementalAspect, Float>> baseElements = config.hoglinWeakResist.getPairList();

		self.initElementalEntity(baseElements);
	}
}
