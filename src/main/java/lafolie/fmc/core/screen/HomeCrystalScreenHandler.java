package lafolie.fmc.core.screen;

import lafolie.fmc.core.FMCScreens;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class HomeCrystalScreenHandler extends FMCScreenHandler
{
	private final Inventory inventory;

	// client constructor
	public HomeCrystalScreenHandler(int syncID, PlayerInventory playerInventory)
	{
		this(syncID, playerInventory, new SimpleInventory(1));
	}

	public HomeCrystalScreenHandler(int syncID, PlayerInventory playerInventory, Inventory inventory)
	{
		super(FMCScreens.HOME_CRYSTAL_SCREEN_HANDLER, syncID);
		checkSize(inventory, 1);
		this.inventory = inventory;
		addSlot(new Slot(inventory, 0, 27, 29));
		createPlayerInventorySlots(playerInventory);
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
}
