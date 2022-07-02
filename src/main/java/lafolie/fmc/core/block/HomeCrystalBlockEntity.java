package lafolie.fmc.core.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mojang.authlib.minecraft.client.MinecraftClient;

import lafolie.fmc.core.FMCBlocks;
import lafolie.fmc.core.FMCItems;
import lafolie.fmc.core.FMCTags;
import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.screen.HomeCrystalScreen;
import lafolie.fmc.core.screen.HomeCrystalScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
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
	private static final String BATTERY_KEY = "battery";
	private static final String BATTERY_MAX_KEY = "battery_max";
	private static final String PEDESTAL_KEY = "pedestal";
	private static final String COUNTDOWN_KEY = "countdown";
	private static final String INVENTORY_KEY = "inventory";

	private static final int CHARGE_RATE = 5;
	private static final int CHARGE_PER_HOUR = 3600;
	public static final int MAX_CHARGE = CHARGE_PER_HOUR * 24;
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
	private int battery = 0;
	private int batteryMax = 1;
	public static final Map<Item, Integer> FUEL = new HashMap<>();
	private SimpleInventory sharedInventory = null;
	private double animSpeedTime = 0d;

	private final PropertyDelegate propertyDelegate = new PropertyDelegate()
	{
		@Override
		public int get(int index)
		{
			switch(index)
			{
				case 0:
					return charge;

				case 1:
					return battery;

				case 2:
					return batteryMax;
				
				case 3:
					return hasPedestal ? 1 : 0;
				
				case 4:
					return explosionCountdown;
			}
			return 0;
		}

		@Override
		public void set(int index, int value)
		{
			switch(index)
			{
				case 0:
					charge = value;
					break;

				case 1:
					battery = value;
					break;

				case 2:
					batteryMax = value;
					break;

				case 3:
					hasPedestal = value == 1;
					break;

				case 4:
					explosionCountdown = value;
					break;
			}
		}

		@Override
		public int size()
		{
			return 5;
		}
	};
	static
	{
		FUEL.put(FMCItems.CRYSTAL_SHARD, 60);
		addFuel(FMCTags.CRYSTAL_ITEMS, 600);
		addFuel(FMCTags.CRYSTAL_BLOCK_ITEMS, 6000);
	}

	private static void addFuel(TagKey<Item> tag, int amount)
	{
		for(RegistryEntry<Item> entry : Registry.ITEM.iterateEntries(tag))
		{
			FUEL.put(entry.value(), amount);
		}
	}

	public static List<Item> getFuelList()
	{
		List<Item> list = new ArrayList<>();
		list.addAll(FUEL.keySet());
		return list;
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

	public boolean incTickCounter()
	{
		tickCounter = (tickCounter + 1) % 20;
		return tickCounter == 0;
	}

	public static void tick(World world, BlockPos pos, BlockState state, HomeCrystalBlockEntity entity)
	{
		// incur a tick once a second
		if(!world.isClient && entity.incTickCounter())
		{
			entity.consumeCharge();
			if(entity.shouldExplode())
			{
				entity.breakCrystal(world);
			}
			else if(entity.charge > 0)
			{
				List<PlayerEntity> players = world.getPlayers(TARGET_PREDICATE, null, entity.effectArea);
				for(PlayerEntity player : players)
				{
					// restore MP
				}
			}
			entity.markDirty();
		}
	}

	private int getChargeDrain()
	{
		return hasPedestal ? 1 : 288;
	}

	private boolean shouldExplode()
	{
		// FinalMinecraft.LOG.info("Explosion check: {}/{} {}", explosionCountdown, CHARGE_PER_HOUR, charge);
		return explosionCountdown >= CHARGE_PER_HOUR;
	}

	private void updateCharge(int charge)
	{
		/*
		 * Allow a slight overcharge. This prevents rapid
		 * battery consumption when the max charge falls 1 point
		 * below max and the battery is drained
		 */
		this.charge = MathHelper.clamp(charge, 0, MAX_CHARGE + CHARGE_RATE);
		if(battery > 0)
		{
			explosionCountdown = 0;
		}
		else if(this.charge == 0)
		{
			explosionCountdown += getChargeDrain();
		}

	}

	private void consumeCharge()
	{
		if(charge < MAX_CHARGE && battery > 0 && hasPedestal)
		{
			battery = Math.max(0, battery - 1);
			updateCharge(charge + CHARGE_RATE);
		}
		else if(battery == 0)
		{
			if(!consumeFuel())
			{
				updateCharge(charge - getChargeDrain());
			}
		}
	}

	private boolean consumeFuel()
	{
		ItemStack stack = sharedInventory.getStack(0);
		if(!stack.isEmpty())
		{
			batteryMax = FUEL.get(stack.getItem());
			battery = batteryMax;
			stack.decrement(1);
			return true;
		}
		return false;
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
	// Animatable
	// ------------------------------------------------------------------------

	private double getAnimationSpeed(double delta)
	{
		double direction = charge == 0 || !hasPedestal ? 1d : -1d;
		animSpeedTime = MathHelper.clamp(animSpeedTime + direction * delta, 0d, 1d);
		FinalMinecraft.LOG.info("ANim speed: {} dt: {}", MathHelper.lerp(animSpeedTime, 1d, 20d), delta);
		
		return MathHelper.lerp(animSpeedTime, 1d, 20d);
	}

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
	{
		AnimationController<?> controller = event.getController();
		if(controller.getCurrentAnimation() == null)
		{
			controller.setAnimation(new AnimationBuilder().addAnimation("animation.final-minecraft:home_crystal.rot", true));
		}
		controller.setAnimationSpeed(getAnimationSpeed(event.getPartialTick()));
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
				nbt.putInt(BATTERY_KEY, battery);
				nbt.putInt(BATTERY_MAX_KEY, batteryMax);
				nbt.putInt(COUNTDOWN_KEY, explosionCountdown);
				nbt.putBoolean(PEDESTAL_KEY, hasPedestal);
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
				battery = nbt.getInt(BATTERY_KEY);
				batteryMax = nbt.getInt(BATTERY_MAX_KEY);
				explosionCountdown = nbt.getInt(COUNTDOWN_KEY);
				hasPedestal = nbt.getBoolean(PEDESTAL_KEY);
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
		return new HomeCrystalScreenHandler(syncID, playerInventory, this, propertyDelegate);
	}

	@Override
	public Text getDisplayName()
	{
		return new TranslatableText(getCachedState().getBlock().getTranslationKey());
	}
}
