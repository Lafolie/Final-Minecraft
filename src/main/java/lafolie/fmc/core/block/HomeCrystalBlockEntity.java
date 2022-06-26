package lafolie.fmc.core.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Final;

import lafolie.fmc.core.FMCBlocks;
import lafolie.fmc.core.FinalMinecraft;
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

public class HomeCrystalBlockEntity extends BlockEntity implements IAnimatable, MultiBlockEntity
{
	private static final String POS_KEY = "masterPos";
	private static final String CHARGE_KEY = "charge";
	private static final int MAX_CHARGE = 9999;
	private static final int JOB_CHARGE = 5000;

	private AnimationFactory animFactory = new AnimationFactory(this);
	private BlockPos masterCrystalPos;
	private int charge = 1000;
	private boolean exploding = false;


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

	@Override
	public BlockPos getMasterBlockEntityPos()
	{
		return masterCrystalPos != null ? masterCrystalPos : getPos();
	}

	@Override
	public BlockEntity getMasterBlockEntity(World world)
	{
		if(masterCrystalPos != null)
		{
			Optional<HomeCrystalBlockEntity> entity = world.getBlockEntity(masterCrystalPos, FMCBlocks.HOME_CRYSTAL_ENTITY);
			if(entity.isPresent())
			{
				return entity.get();
			}

		}
		return null;
	}

	public boolean isExploding()
	{
		return exploding;
	}

	public void breakCrystal(World world, BlockPos originalPos)
	{
		exploding = true;
		BlockPos origin = getPos();
		// FinalMinecraft.LOG.info("Destroying crystal. Master: {}", origin);
		// int count = 0;
		for(int y = -2; y <= 1; y++)
		{
			for(int x = -1; x <= 1; x++)
			{
				for(int z = -1; z <= 1; z++)
				{
					BlockPos pos = origin.add(x, y, z);
					destroyDummy(world, pos);
					// count ++;
				}
			}
		}

		destroyDummy(world, origin.up(2));
		destroyDummy(world, origin.down(3));
		// count +=2;

		// FinalMinecraft.LOG.info("Destroyed {} blocks", count);
		world.createExplosion(null, origin.getX(), origin.getY(), origin.getZ(), 8, Explosion.DestructionType.BREAK);
	}

	private void destroyDummy(World world, BlockPos pos)
	{
		BlockState state = world.getBlockState(pos);
		if(state.getBlock() == FMCBlocks.HOME_CRYSTAL)
		{
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
			// world.removeBlock(pos, false);
			world.removeBlockEntity(pos);
			world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, HomeCrystalBlock.getRawIdFromState(state));
			// else
			// {
			// 	FinalMinecraft.LOG.info("Skipping block: {} original: {}", pos, originalPos);
			// }
		}
		// else
		// {
		// 	FinalMinecraft.LOG.info("Did not find crystal: {}", pos);
		// }
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
			
			// only store charge for the master
			if(masterCrystalPos.equals(getPos()))
			{
				nbt.putInt(CHARGE_KEY, charge);
			}
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

			if(masterCrystalPos.equals(getPos()))
			{
				charge = nbt.getInt(CHARGE_KEY);
			}
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
