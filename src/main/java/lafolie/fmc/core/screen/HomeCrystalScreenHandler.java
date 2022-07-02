package lafolie.fmc.core.screen;

import java.util.ArrayList;
import java.util.List;

import lafolie.fmc.core.FMCScreens;
import lafolie.fmc.core.block.HomeCrystalBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.slot.Slot;

public class HomeCrystalScreenHandler extends FMCScreenHandler
{
	private static final List<Item> ALLOWED_ITEMS;
	private final Inventory inventory;
	private PropertyDelegate propertyDelegate;

	static
	{
		ALLOWED_ITEMS = HomeCrystalBlockEntity.getFuelList();
	}

	// client constructor
	public HomeCrystalScreenHandler(int syncID, PlayerInventory playerInventory)
	{
		this(syncID, playerInventory, new SimpleInventory(1), new ArrayPropertyDelegate(5));
	}

	public HomeCrystalScreenHandler(int syncID, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate)
	{
		super(FMCScreens.HOME_CRYSTAL_SCREEN_HANDLER, syncID);
		checkSize(inventory, 1);
		this.inventory = inventory;
		this.propertyDelegate = propertyDelegate;
		addSlot(new Slot(inventory, 0, 27, 29)
		{
			@Override
			public boolean canInsert(ItemStack stack)
			{
				return ALLOWED_ITEMS.contains(stack.getItem());
			}
		});
		createPlayerInventorySlots(playerInventory);
		addProperties(propertyDelegate);
	}

	@Override
	public boolean canUse(PlayerEntity player)
	{
		return inventory.canPlayerUse(player);
	}
	
	@Override
	public ItemStack transferSlot(PlayerEntity player, int index)
	{
		ItemStack newStack = ItemStack.EMPTY;
		Slot slot = slots.get(index);
		if(slot != null)
		{
			ItemStack original = slot.getStack();
			newStack = original.copy();
			if(index < inventory.size() && !insertItem(original, inventory.size(), slots.size(), true))
			{
				return ItemStack.EMPTY;
			}
			else if(!insertItem(original, 0, inventory.size(), false))
			{
				return ItemStack.EMPTY;
			}

			if(original.isEmpty())
			{
				slot.setStack(ItemStack.EMPTY);
			}
			else
			{
				slot.markDirty();
			}
		}
		return newStack;
	}

	public float getChargePercent()
	{
		return propertyDelegate.get(0) / (float)HomeCrystalBlockEntity.MAX_CHARGE;
	}

	public float getBatteryPercent()
	{
		return propertyDelegate.get(1) / (float)propertyDelegate.get(2);
	}

	public boolean getHasPedestal()
	{
		return propertyDelegate.get(3) == 1;
	}

	public int getExplosionCountdown()
	{
		return propertyDelegate.get(4);
	}
}
