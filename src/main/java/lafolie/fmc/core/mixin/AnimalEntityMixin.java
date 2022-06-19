package lafolie.fmc.core.mixin;

import org.spongepowered.asm.mixin.Mixin;

import lafolie.fmc.core.zodiac.BirthsignEntity;
import net.minecraft.entity.passive.AnimalEntity;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin implements BirthsignEntity
{
	
}
