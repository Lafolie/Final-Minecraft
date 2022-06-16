package lafolie.fmc.core.mixin;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;
import lafolie.fmc.core.elements.ElementalObject;
import lafolie.fmc.core.internal.Components;
import lafolie.fmc.core.internal.elements.ElementalEntityTags;
import lafolie.fmc.core.internal.elements.ElementalStatsComponent;
import lafolie.fmc.core.internal.elements.InnateElemental;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

@Mixin(Entity.class)
public abstract class EntityMixin implements ElementalObject, InnateElemental
{
	@Shadow
    public abstract EntityType<?> getType();

	// @Inject(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	// private void damage(DamageSource source, float amount, CallbackInfoReturnable info)
	// {
	// 	FinalMinecraft.log.info("I was damaged");
	// 	getComponent();
	// }

	@Override
	public Map<ElementalAspect, Integer> getInnateElements(ElementalAttribute attribute)
	{
		Map<ElementalAspect, Integer> result = new EnumMap<>(ElementalAspect.class);
		int amount = attribute == ElementalAttribute.WEAKNESS || attribute == ElementalAttribute.RESISTANCE ? FinalMinecraft.getConfig().defaultEnemyWeakResist : 1;

		// getRegistryEntry is deprecated, but there doesn't seem to be another way to get it?
		List<ElementalAspect> innateElements = ElementalEntityTags.getEntityElementalTags(getType().getRegistryEntry(), attribute);
		for(ElementalAspect  element : innateElements)
		{
			result.put(element, amount);
		}

		return result;
	}

	@Override
	public ElementalStatsComponent getComponent()
	{
		ElementalStatsComponent component = Components.ELEMENTAL_STATS.get(this);
		
		if(!component.hasInitInnate())
		{
			component.setHasInitInnate();
			InnateElemental innate = (InnateElemental)this;
			for(ElementalAttribute attribute : ElementalAttribute.values())
			{
				// weakness and resistance are a single concept until added to the component
				if(attribute == ElementalAttribute.WEAKNESS)
				{
					continue;
				}
				else if(attribute == ElementalAttribute.RESISTANCE)
				{
					for(Map.Entry<ElementalAspect, Integer> entry : innate.getInnateElements(attribute).entrySet())
					{
						addInnateElementalResistance(entry.getKey(), entry.getValue());
					}
				}
				else
				{
					for(Map.Entry<ElementalAspect, Integer> entry : innate.getInnateElements(attribute).entrySet())
					{
						addElementalAspectRaw(entry.getKey(), attribute, entry.getValue());
					}
				}
			}
			// addInnate* requires manual sync
			component.trySync();
		}
		return component;
	}
}
