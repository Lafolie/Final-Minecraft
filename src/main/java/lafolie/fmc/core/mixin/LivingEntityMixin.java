package lafolie.fmc.core.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lafolie.fmc.core.Mod;
import lafolie.fmc.core.elements.Element;
import lafolie.fmc.core.elements.ElementalEntity;
import lafolie.fmc.core.elements.WeakResistTable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
	implements ElementalEntity
{
	private WeakResistTable weakResist;

	@Override
	public void InitElementalEntity()
	{
		if(weakResist == null)
		{
			weakResist = new WeakResistTable();
		}
	}

	@Override
	public void InitElementalEntity(List<Pair<Element, Float>> baseWeakResist)
	{
		if(weakResist == null)
		{
			weakResist = new WeakResistTable(baseWeakResist);
		}
	}

	@Override
	public WeakResistTable GetWeakResistTable()
	{
		return weakResist;
	}

	@Inject(at = @At("TAIL"), method = "<init>")
	public void Constructor(EntityType<? extends LivingEntity> entityType, World world, CallbackInfo info)
	{
		
	}
}
