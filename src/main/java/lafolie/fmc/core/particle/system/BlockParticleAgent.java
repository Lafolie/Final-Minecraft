package lafolie.fmc.core.particle.system;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class BlockParticleAgent extends ParticleAgent
{
	public final BlockPos blockPos;
	public final Vec3d position;

	public BlockParticleAgent(BlockPos pos, ParticleSystem system)
	{
		super(system);
		blockPos = pos;
		position = Vec3d.ofCenter(pos);
	}

	@Override
	public Vec3d getPosition()
	{
		return position;
	}
}
