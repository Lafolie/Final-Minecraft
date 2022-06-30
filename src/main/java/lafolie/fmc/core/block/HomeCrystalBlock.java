package lafolie.fmc.core.block;

import java.util.Optional;

import com.mojang.datafixers.types.templates.Check.CheckType;

import lafolie.fmc.core.FMCBlocks;
import lafolie.fmc.core.FinalMinecraft;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
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

public class HomeCrystalBlock extends BlockWithEntity
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
		return state.get(IS_DUMMY).booleanValue() ? BlockRenderType.INVISIBLE : BlockRenderType.ENTITYBLOCK_ANIMATED;
		// return state.get(IS_DUMMY).booleanValue() ? BlockRenderType.MODEL : BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		BlockPos masterPos;
		Optional<HomeCrystalBlockEntity> entity = world.getBlockEntity(pos, FMCBlocks.HOME_CRYSTAL_ENTITY);
		if(entity.isPresent())
		{
			masterPos = ((MultiBlockEntity)entity.get()).getMasterBlockEntityPos();
		}
		Block master = world.getBlockState(pos).getBlock();
		return master instanceof HomeCrystalBlock ? ((HomeCrystalBlock)master).useCrystal(state, world, pos, player, hand, hit) : ActionResult.FAIL;
	}

	public ActionResult useCrystal(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
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

	private HomeCrystalBlockEntity getMasterCrystal(World world, BlockPos pos)
	{
		Optional<HomeCrystalBlockEntity> entity = world.getBlockEntity(pos, FMCBlocks.HOME_CRYSTAL_ENTITY);
		if(entity.isPresent())
		{
			return (HomeCrystalBlockEntity)((MultiBlockEntity)entity.get()).getMasterBlockEntity(world);
		}
		return null;
	}

	/**
	 * Ensures that only the master home crystal block will tick
	 */
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type)
	{
		if(!world.isClient && !state.get(IS_DUMMY))
		{
			return HomeCrystalBlock.checkType(type, FMCBlocks.HOME_CRYSTAL_ENTITY, HomeCrystalBlockEntity::tick);
		}

		return null;
	}

	private void breakAll(World world, BlockPos pos)
	{
		if(!world.isClient)
		{
			HomeCrystalBlockEntity masterCrystal = getMasterCrystal(world, pos);
			if(masterCrystal != null && !masterCrystal.isExploding())
			{
				masterCrystal.breakCrystal(world);
			}
		}
	}
}
