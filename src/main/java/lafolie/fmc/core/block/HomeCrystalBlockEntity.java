package lafolie.fmc.core.block;

import lafolie.fmc.core.FMCBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class HomeCrystalBlockEntity extends BlockEntity implements IAnimatable
{
	private AnimationFactory animFactory = new AnimationFactory(this);
	public boolean isDummy = false;

	public HomeCrystalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	public HomeCrystalBlockEntity(BlockPos pos, BlockState state)
	{
		this(FMCBlocks.HOME_CRYSTAL_ENTITY, pos, state);
	}

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
	{
		event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.final-minecraft:home_crystal.rot", true));
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(new AnimationController<HomeCrystalBlockEntity>(this, "controller", 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory()
	{
		return this.animFactory;
	}

	
}
