package lafolie.fmc.core.mixin;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;
import lafolie.fmc.core.elements.ElementalObject;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

@Mixin(Entity.class)
public class EntityMixin implements ElementalObject
{

	@Override
	public void AddElementalAspect(ElementalAspect element, ElementalAttribute attribute) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int GetElementalAttribute(ElementalAspect element, ElementalAttribute attribute) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int GetElementalAffinity(ElementalAspect element) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean HasElementalAspect(ElementalAspect element, ElementalAttribute attribute) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void RemoveElementalAspect(ElementalAspect element, ElementalAttribute attribute) {
		// TODO Auto-generated method stub
		
	}

	private void AddElement(ElementalAttribute attribute, ElementalAspect element, byte inAmt)
	{

	}

	@Override
	public void AddBaseElementalAspect(ElementalAspect element, ElementalAttribute attribute) {
		// TODO Auto-generated method stub
		
	}

}
