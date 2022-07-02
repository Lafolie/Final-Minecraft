package lafolie.fmc.core.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lafolie.fmc.core.FMCBlocks;
import lafolie.fmc.core.FinalMinecraft;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CrystalPedestalBlock extends Block
{
	private static int CRYSTAL_STRUCTURE_SIZE = 38;

	public CrystalPedestalBlock(Settings settings)
	{
		super(settings);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack)
	{
		if(!world.isClient)
		{
			HomeCrystalBlockEntity master = findHomeCrystalAbove(world, pos);
			if(master != null)
			{
				master.setHasPedestal(true);
			}
			else
			{
				detectCrystalShape(world, pos);
			}
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved)
	{
		if(!world.isClient)
		{
			HomeCrystalBlockEntity master = findHomeCrystalAbove(world, pos);
			if(master != null && !master.isExploding())
			{
				master.setHasPedestal(false);
			}
		}
	}

	private HomeCrystalBlockEntity findHomeCrystalAbove(World world, BlockPos pos)
	{
		Optional<HomeCrystalBlockEntity> entity = world.getBlockEntity(pos.up(), FMCBlocks.HOME_CRYSTAL_ENTITY);
		if(entity.isPresent())
		{
			BlockEntity master = entity.get().getMasterBlockEntity(world);
			if(master != null)
			{
				return (HomeCrystalBlockEntity)master;
			}
		}
		return null;
	}

	public boolean detectCrystalShape(World world, BlockPos pos)
	{
		FinalMinecraft.LOG.info("Checking for crystal structure....");
		List<BlockPos> closedList = new ArrayList<>();

		// check bottom block
		if(world.getBlockState(pos.up()).getBlock() == FMCBlocks.CRYSTAL)
		{
			closedList.add(pos.up());
		}
		else
		{
			return false;
		}

		// check top block
		if(world.getBlockState(pos.up(6)).getBlock() == FMCBlocks.CRYSTAL)
		{
			closedList.add(pos.up(6));
		}
		else
		{
			return false;
		}

		// check middle structure cuboid
		for(int y = 2; y <= 5; y++)
		{
			FMCBlocks.CRYSTAL.getSurroundingCrystalPos(closedList, world, pos.up(y), true);
		}

		FinalMinecraft.LOG.info("Structure size: {}", closedList.size());
		if(closedList.size() == CRYSTAL_STRUCTURE_SIZE)
		{
			FinalMinecraft.LOG.info("DETECTED STRUCTURE!!!!");
			buildHomeCrystal(closedList, world, pos.up(4));
			return true;
		}

		return false;
	}

	private void buildHomeCrystal(List<BlockPos> crystalBlocks, World world, BlockPos masterCrystalPos)
	{
		// BlockState state = FMCBlocks.FIRE_CRYSTAL.getDefaultState();
		SimpleInventory sharedInventory = new SimpleInventory(1);
		for(BlockPos pos : crystalBlocks)
		{
			BlockState state = FMCBlocks.HOME_CRYSTAL.getDefaultState().with(HomeCrystalBlock.IS_DUMMY, !pos.equals(masterCrystalPos));
			world.setBlockState(pos, state, Block.NOTIFY_ALL);
			// FinalMinecraft.LOG.info("Replacing {}, isDummy = {} ({})", pos.toShortString(), pos.equals(masterCrystalPos), masterCrystalPos.toShortString());
			world.addBlockEntity(new HomeCrystalBlockEntity(pos, state, masterCrystalPos, sharedInventory));
			world.updateNeighbors(pos, state.getBlock());
			state.updateNeighbors(world, pos, NOTIFY_ALL);
		}
	}
	// @Override
	// public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
	// 		BlockHitResult hit) {
	// 	// TODO Lua-generated method stub ;)
	// 	return ActionResult.SUCCESS;
	// }
}
