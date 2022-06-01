package lafolie.fmc.core.mixin;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;
import lafolie.fmc.core.elements.ElementalObject;
import lafolie.fmc.core.internal.Components;
import lafolie.fmc.core.internal.elements.ElementalStats;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

@Mixin(Entity.class)
public class EntityMixin implements ElementalObject
{

	private ElementalStats getComponent()
	{
		return Components.ELEMENTAL_STATS.get(this);
	}

	@Override
	public void addBaseElementalAspect(ElementalAspect element, ElementalAttribute attribute, int amount)
	{
		getComponent().addElement(element, attribute, (byte)1);
	}

	@Override
	public void addElementalAspect(ElementalAspect element, ElementalAttribute attribute)
	{
		
		
	}

	@Override
	public void removeElementalAspect(ElementalAspect element, ElementalAttribute attribute)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasElementalAspect(ElementalAspect element, ElementalAttribute attribute)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getElementalAttribute(ElementalAspect element, ElementalAttribute attribute)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getElementalAffinity(ElementalAspect element)
	{
		// TODO Auto-generated method stub
		return 0;
	}




}
