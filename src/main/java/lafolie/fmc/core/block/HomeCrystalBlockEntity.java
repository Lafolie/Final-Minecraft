package lafolie.fmc.core.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lafolie.fmc.core.FMCBlocks;
import lafolie.fmc.core.screen.HomeCrystalScreen;
import lafolie.fmc.core.screen.HomeCrystalScreenHandler;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class HomeCrystalBlockEntity extends BlockEntity 
	implements Inventory, IAnimatable, MultiBlockEntity, NamedScreenHandlerFactory
{
	private static final String POS_KEY = "masterPos";
	private static final String CHARGE_KEY = "charge";
	private static final String INVENTORY_KEY = "inventory";

	private static final int CHARGE_PER_HOUR = 72000;
	private static final int MAX_CHARGE = CHARGE_PER_HOUR * 24;
	private static final int JOB_CHARGE = CHARGE_PER_HOUR * 12;
	private static final double EFFECT_SIZE = 2048d;
	private static final TargetPredicate TARGET_PREDICATE = TargetPredicate.createNonAttackable();

	public final Box effectArea;
	
	private HomeCrystalBlockEntity masterCrystal;
	private AnimationFactory animFactory = new AnimationFactory(this);
	private BlockPos masterCrystalPos;
	private boolean hasPedestal = true;
	private int charge = 0;
	private int explosionCountdown = 0;
	private boolean exploding = false;
	private int tickCounter = 0;
	private ChargeStatus chargeStatus = ChargeStatus.LOW;
	private int battery = 0;
	private SimpleInventory sharedInventory = null;

	public static enum ChargeStatus
	{
		EMPTY,
		LOW,
		NOMINAL,
		CHARGING;
	}

	public HomeCrystalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);

		boolean isDummy = state.get(HomeCrystalBlock.IS_DUMMY);
		effectArea = isDummy ? null : Box.of(Vec3d.of(pos), EFFECT_SIZE, EFFECT_SIZE, EFFECT_SIZE);
		if(sharedInventory == null)
		{
			if(isDummy)
			{
				BlockEntity master = getMasterBlockEntity(world);
				if(master != null)
				{
					sharedInventory = ((HomeCrystalBlockEntity)master).getSharedInventory();
				}
			}
			else
			{
				sharedInventory = new SimpleInventory(1);
			}
		}

		if(!state.get(HomeCrystalBlock.IS_DUMMY))
		{
			charge = CHARGE_PER_HOUR;
		}
	}

	public void setHasPedestal(boolean hasPedestal)
	{
		this.hasPedestal = hasPedestal;
	}

	public HomeCrystalBlockEntity(BlockPos pos, BlockState state)
	{
		this(FMCBlocks.HOME_CRYSTAL_ENTITY, pos, state);
	}

	public HomeCrystalBlockEntity(BlockPos pos, BlockState state, BlockPos masterPos, SimpleInventory sharedInventory)
	{
		this(pos, state);
		masterCrystalPos = masterPos;
	}

	public static void tick(World world, BlockPos pos, BlockState state, HomeCrystalBlockEntity entity)
	{
		// incur a tick once a second
		if(entity.incTickCounter())
		{
			entity.consumeCharge();
			if(entity.shouldExplode())
			{
				entity.breakCrystal(world);
			}
			else
			{
				List<PlayerEntity> players = world.getPlayers(TARGET_PREDICATE, null, entity.effectArea);
				for(PlayerEntity player : players)
				{
					// restore MP
				}
				entity.markDirty();
			}
		}
	}

	public boolean shouldExplode()
	{
		return explosionCountdown >= CHARGE_PER_HOUR;
	}

	private void updateCharge(int charge)
	{
		this.charge = MathHelper.clamp(charge, 0, MAX_CHARGE);
		if(battery > 0)
		{
			chargeStatus = ChargeStatus.CHARGING;
			explosionCountdown = 0;
		}
		else
		{
			if(charge == 0)
			{
				chargeStatus = ChargeStatus.EMPTY;
				explosionCountdown += 1;
			}
			else if(charge < CHARGE_PER_HOUR)
			{
				chargeStatus = ChargeStatus.LOW;
			}
			else
			{
				chargeStatus = ChargeStatus.NOMINAL;
			}

		}

	}

	public void consumeCharge()
	{
		if(battery > 0)
		{
			battery -= 1;
			updateCharge(charge + 1);
		}
		else
		{
			updateCharge(charge - 1);
		}
	}

	public void setBattery(int battery)
	{
		this.battery = battery;
	}

	public boolean incTickCounter()
	{
		tickCounter = (tickCounter + 1) % 20;
		return tickCounter == 0;
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

	public void breakCrystal(World world)
	{
		exploding = true;
		BlockPos origin = getPos();
		ItemScatterer.spawn(world, origin, this);

		for(int y = -2; y <= 1; y++)
		{
			for(int x = -1; x <= 1; x++)
			{
				for(int z = -1; z <= 1; z++)
				{
					BlockPos pos = origin.add(x, y, z);
					destroyDummy(world, pos);
				}
			}
		}

		destroyDummy(world, origin.up(2));
		destroyDummy(world, origin.down(3));

		world.createExplosion(null, origin.getX(), origin.getY(), origin.getZ(), 8, Explosion.DestructionType.BREAK);
	}

	private void destroyDummy(World world, BlockPos pos)
	{
		BlockState state = world.getBlockState(pos);
		if(state.getBlock() == FMCBlocks.HOME_CRYSTAL)
		{
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
			world.removeBlockEntity(pos);
			world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, HomeCrystalBlock.getRawIdFromState(state));
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
			
			// only store these for the master
			if(masterCrystalPos.equals(getPos()))
			{
				nbt.putInt(CHARGE_KEY, charge);
				nbt.put(INVENTORY_KEY, sharedInventory.toNbtList());
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

			// only retrieve these for the master
			if(masterCrystalPos.equals(getPos()))
			{
				charge = nbt.getInt(CHARGE_KEY);
				sharedInventory.readNbtList(nbt.getList(INVENTORY_KEY, NbtType.COMPOUND));
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

	// ------------------------------------------------------------------------
	// Inventory
	// ------------------------------------------------------------------------
	
	protected SimpleInventory getSharedInventory()
	{
		return sharedInventory;
	}

	@Override
	public void clear()
	{
		sharedInventory.clear();
	}

	@Override
	public int size()
	{
		return sharedInventory.size();
	}

	@Override
	public boolean isEmpty()
	{
		return sharedInventory.isEmpty();
	}

	@Override
	public ItemStack getStack(int slot)
	{
		return sharedInventory.getStack(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount)
	{
		return sharedInventory.removeStack(slot, amount);
	}

	@Override
	public ItemStack removeStack(int slot)
	{
		return sharedInventory.removeStack(slot);
	}

	@Override
	public void setStack(int slot, ItemStack stack)
	{
		sharedInventory.setStack(slot, stack);
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player)
	{
		return sharedInventory.canPlayerUse(player);
	}

	// ------------------------------------------------------------------------
	// ScreenHandler
	// ------------------------------------------------------------------------

	@Override
	public ScreenHandler createMenu(int syncID, PlayerInventory playerInventory, PlayerEntity player)
	{
		return new HomeCrystalScreenHandler(syncID, playerInventory, this);
	}

	@Override
	public Text getDisplayName()
	{
		return new TranslatableText(getCachedState().getBlock().getTranslationKey());
	}
}
