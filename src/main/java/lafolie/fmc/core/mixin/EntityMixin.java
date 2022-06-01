package lafolie.fmc.core.mixin;

import org.spongepowered.asm.mixin.Mixin;

import lafolie.fmc.core.elements.ElementalObject;
import lafolie.fmc.core.internal.Components;
import lafolie.fmc.core.internal.elements.ElementalStatsComponent;

import net.minecraft.entity.Entity;

@Mixin(Entity.class)
public class EntityMixin implements ElementalObject
{
	@Override
	public ElementalStatsComponent getComponent()
	{
		return Components.ELEMENTAL_STATS.get(this);
	}
}
