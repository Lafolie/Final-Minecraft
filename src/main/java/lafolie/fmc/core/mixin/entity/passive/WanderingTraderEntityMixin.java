package lafolie.fmc.core.mixin.entity.passive;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lafolie.fmc.core.Mod;
import lafolie.fmc.core.config.FMCConfig;
import lafolie.fmc.core.elements.Element;
import lafolie.fmc.core.elements.ElementalEntity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

@Mixin(WanderingTraderEntity.class)
public abstract class WanderingTraderEntityMixin
{
	@Inject(at = @At("TAIL"), method = "<init>")
	public void Constructor(EntityType<? extends WanderingTraderEntity> entityType, World world, CallbackInfo info)
	{
		ElementalEntity self = (ElementalEntity)this;

		FMCConfig config =  Mod.getConfig();
		ArrayList<Pair<Element, Float>> baseElements = config.wanderingTraderWeakResist.GetPairList();

		self.InitElementalEntity(baseElements);
	}
}
