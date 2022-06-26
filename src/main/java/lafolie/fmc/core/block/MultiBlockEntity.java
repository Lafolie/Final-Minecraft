package lafolie.fmc.core.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface MultiBlockEntity
{
	public BlockPos getMasterBlockEntityPos();
	public BlockEntity getMasterBlockEntity(World world);
}
