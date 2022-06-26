package lafolie.fmc.core.block;

import java.util.Optional;

import lafolie.fmc.core.FMCBlocks;
import lafolie.fmc.core.FinalMinecraft;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;

public class HomeCrystalBlock extends Block implements BlockEntityProvider
{
	public static final BooleanProperty IS_DUMMY = BooleanProperty.of("is_dummy");

	public HomeCrystalBlock(Settings settings)
	{
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(IS_DUMMY, false));
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder)
	{
		// super.appendProperties(builder);
		builder.add(IS_DUMMY);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		// return state.get(IS_DUMMY).booleanValue() ? BlockRenderType.INVISIBLE : BlockRenderType.ENTITYBLOCK_ANIMATED;
		return state.get(IS_DUMMY).booleanValue() ? BlockRenderType.MODEL : BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockHitResult hit)
	{
		// TODO Lua-generated method stub ;)
		return ActionResult.SUCCESS;
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new HomeCrystalBlockEntity(pos, state);
	}

	// @Override
	// public void onBroken(WorldAccess world, BlockPos pos, BlockState state)
	// {
	// 	super.onBroken(world, pos, state);
	// 	breakAll((World)world, pos);
	// }

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player)
	{
		super.onBreak(world, pos, state, player);
		breakAll(world, pos);
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved)
	{
		breakAll(world, pos);
		FinalMinecraft.LOG.info("Explosion call");
	}

	// @Override
	// public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion)
	// {
	// 	breakAll(world, pos);
	// }

	private void breakAll(World world, BlockPos pos)
	{
		if(!world.isClient)
		{
			Optional<HomeCrystalBlockEntity> entity = world.getBlockEntity(pos, FMCBlocks.HOME_CRYSTAL_ENTITY);
			if(entity.isPresent())
			{
				FinalMinecraft.LOG.info("Destroying all");
				HomeCrystalBlockEntity masterCrystal = (HomeCrystalBlockEntity)((MultiBlockEntity)entity.get()).getMasterBlockEntity(world);
				if(masterCrystal != null && !masterCrystal.isExploding())
				{
					masterCrystal.breakCrystal(world, pos);
				}
			}
			world.removeBlockEntity(pos);
		}
	}
}
