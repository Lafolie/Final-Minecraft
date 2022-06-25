package lafolie.fmc.core.block;

import java.util.ArrayList;
import java.util.List;

import lafolie.fmc.core.FMCBlocks;
import lafolie.fmc.core.FinalMinecraft;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
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
			return true;
		}

		return false;
	}

	// @Override
	// public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
	// 		BlockHitResult hit) {
	// 	// TODO Lua-generated method stub ;)
	// 	return ActionResult.SUCCESS;
	// }
}
