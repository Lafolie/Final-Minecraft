// package lafolie.fmc.core.mixin;

// import java.util.List;

// import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// import lafolie.fmc.core.Mod;
// import lafolie.fmc.core.elements.ElementalAspect;
// import lafolie.fmc.core.elements.ElementalAttribute;
// import lafolie.fmc.core.elements.ElementalEntity;
// import lafolie.fmc.core.elements.ElementalObject;
// import lafolie.fmc.core.elements.WeakResistTable;
// import net.minecraft.entity.LivingEntity;
// import net.minecraft.util.Pair;
// import net.minecraft.entity.EntityType;
// import net.minecraft.world.World;

// @Mixin(LivingEntity.class)
// public abstract class LivingEntityMixin
// 	implements ElementalObject
// {
	
// 	@Inject(at = @At("TAIL"), method = "<init>")
// 	public void Constructor(EntityType<? extends LivingEntity> entityType, World world, CallbackInfo info)
// 	{
		
// 	}

// 	@Override
// 	public void addElementalAspect(ElementalAspect element, ElementalAttribute attribute) {
// 		// TODO Auto-generated method stub
		
// 	}

// 	@Override
// 	public int getElementalAttribute(ElementalAspect element, ElementalAttribute attribute) {
// 		// TODO Auto-generated method stub
// 		return 0;
// 	}

// 	@Override
// 	public int getElementalAffinity(ElementalAspect element) {
// 		// TODO Auto-generated method stub
// 		return 0;
// 	}

// 	@Override
// 	public boolean hasElementalAspect(ElementalAspect element, ElementalAttribute attribute) {
// 		// TODO Auto-generated method stub
// 		return false;
// 	}

// 	@Override
// 	public void removeElementalAspect(ElementalAspect element, ElementalAttribute attribute) {
// 		// TODO Auto-generated method stub
		
// 	}
// }
