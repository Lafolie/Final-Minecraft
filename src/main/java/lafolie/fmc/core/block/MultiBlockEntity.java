package lafolie.fmc.core.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Basic interface for dealing with MultiBlockEntities. Only
 * used for Home Crystals for now, but if needed for other things
 * this should be expanded.
 */
public interface MultiBlockEntity
{
	public BlockPos getMasterBlockEntityPos();
	public BlockEntity getMasterBlockEntity(World world);
}
