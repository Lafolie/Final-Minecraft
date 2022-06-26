package lafolie.fmc.core.block;

import java.util.ArrayList;
import java.util.List;

import lafolie.fmc.core.FMCBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.BlockEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.explosion.Explosion;
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
	private BlockPos masterCrystalPos;
	private static final String POS_KEY = "masterPos";

	public HomeCrystalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	public HomeCrystalBlockEntity(BlockPos pos, BlockState state)
	{
		this(FMCBlocks.HOME_CRYSTAL_ENTITY, pos, state);
	}

	public HomeCrystalBlockEntity(BlockPos pos, BlockState state, BlockPos masterPos)
	{
		this(pos, state);
		masterCrystalPos = masterPos;
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

	public void breakCrystal(World world, BlockPos originalPos)
	{
		BlockPos origin = getPos();
		for(int y = -1; y <= 2; y++)
		{
			for(int x = -1; x <= 1; x++)
			{
				for(int z = -1; z <= 1; z++)
				{
					BlockPos pos = origin.add(x, y, z);
					destroyDummy(world, pos, originalPos);
					
				}
			}
		}

		destroyDummy(world, origin.up(2), originalPos);
		destroyDummy(world, origin.down(3), originalPos);

		world.createExplosion(null, origin.getX(), origin.getY(), origin.getZ(), 8, Explosion.DestructionType.BREAK);
	}

	private void destroyDummy(World world, BlockPos pos, BlockPos originalPos)
	{
		BlockState state = world.getBlockState(pos);
		if(state.getBlock() == FMCBlocks.HOME_CRYSTAL)
		{
			if(!pos.equals(originalPos))
			{
				world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, HomeCrystalBlock.getRawIdFromState(state));
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
			}
		}
	}

	// ------------------------------------------------------------------------
	// NBT
	// ------------------------------------------------------------------------

	@Override
	protected void writeNbt(NbtCompound nbt)
	{
		super.writeNbt(nbt);
		if(masterCrystalPos != null)
		{
			nbt.putIntArray(POS_KEY, new int[] {masterCrystalPos.getX(), masterCrystalPos.getY(), masterCrystalPos.getZ()});
		}
	}

	@Override
	public void readNbt(NbtCompound nbt)
	{
		super.readNbt(nbt);
		if(nbt.contains(POS_KEY))
		{
			int[] arr = nbt.getIntArray(POS_KEY);
			masterCrystalPos = new BlockPos(arr[0], arr[1], arr[2]);
		}
	}

	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket()
	{
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt()
	{
		return createNbt();
	}
}
