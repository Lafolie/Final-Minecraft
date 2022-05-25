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
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

@Mixin(SquidEntity.class)
public abstract class SquidEntityMixin
{
	@Inject(at = @At("TAIL"), method = "<init>")
	public void Constructor(EntityType<? extends SquidEntity> entityType, World world, CallbackInfo info)
	{
		ElementalEntity self = (ElementalEntity)this;

		FMCConfig config =  Mod.getConfig();
		ArrayList<Pair<Element, Float>> baseElements = config.squidWeakResist.GetPairList();

		self.InitElementalEntity(baseElements);
	}
}
