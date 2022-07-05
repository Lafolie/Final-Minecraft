package lafolie.fmc.core.block;

import java.util.ArrayList;
import java.util.List;

import lafolie.fmc.core.FMCBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CrystalBlock extends Block
{
	public CrystalBlock(Settings settings)
	{
		super(settings);
	}

	/**
	 * Check to see if a Home Crystal structure was completed
	 */
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack)
	{
		if(world.isClient)
		{
			return;
		}

		/**
		 * Find a pedestal and tell it to check for a completed crystal build.
		 * We have to use a list in case the player is constructing multiple crystals
		 * adjacent to eachother. If so, just use the first one we hit (similar to
		 * minecart/redstone rules)
		 */
		List<BlockPos> pedestals = findPedestals(world, pos);
		for(BlockPos pedestalPos : pedestals)
		{
			if(FMCBlocks.CRYSTAL_PEDESTAL.detectCrystalShape(world, pedestalPos))
			{
				break;
			}
		}
	}

	/**
	 * Get a list of all the crystal blocks surrounding this one on
	 * the XZ plane
	 * @param world
	 * @param pos
	 * @return
	 */
	public List<BlockPos> getSurroundingCrystalPos(List<BlockPos> list, World world, BlockPos pos, boolean includeMid)
	{
		for(int x = -1; x <= 1; x++)
		{
			for(int z = -1; z <= 1; z++)
			{
				BlockPos checkPos = pos.add(x, 0, z);
				if((!(x == 0 && z == 0) || includeMid) && world.getBlockState(checkPos).getBlock() == FMCBlocks.CRYSTAL)
				{
					list.add(checkPos);
				}
			}
		}
		return list;
	}

	/**
	 * Find any pedestals below this crystal formation
	 * @param world
	 * @param pos
	 * @return
	 */
	private List<BlockPos> findPedestals(World world, BlockPos pos)
	{	
		ArrayList<BlockPos> result = new ArrayList<>();

		// first, check if we placed the top or bottom block
		// and exit early
		BlockPos topCheck = pos.down(6);
		BlockPos bottomCheck = pos.down(1);

		BlockState state = world.getBlockState(topCheck);
		if(state.getBlock() == FMCBlocks.CRYSTAL_PEDESTAL)
		{
			result.add(topCheck);
			return result;
		}

		state = world.getBlockState(bottomCheck);
		if(state.getBlock() == FMCBlocks.CRYSTAL_PEDESTAL)
		{
			result.add(bottomCheck);
			return result;
		}

		// not found, so check surroundings
		List<BlockPos> surroundingCrystals = new ArrayList<>();
		getSurroundingCrystalPos(surroundingCrystals, world, pos, false);

		// if we found less than 3 crystals, this structure must be incomplete
		if(surroundingCrystals.size() < 3)
		{
			return result;
		}

		// start at 2 because we already checked if this was the bottom block
		for(int y = 2; y <= 5; y++)
		{

			int numCrystals = 0;
			for(BlockPos p : surroundingCrystals)
			{
				Block block = world.getBlockState(p.down(y)).getBlock();
				if(block == FMCBlocks.CRYSTAL_PEDESTAL)
				{
					result.add(p.down(y));
				}
				else
				{
					numCrystals += block == FMCBlocks.CRYSTAL ? 1 : 0;
				}
			}

			// if we hit no crystals, no point checking further
			if(numCrystals ==  0)
			{
				break;
			}
		}
		return result;
	}

	// @Override
	// public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
	// 		BlockHitResult hit) {
	// 	// TODO Lua-generated method stub ;)
	// 	return ActionResult.SUCCESS;
	// }
}
