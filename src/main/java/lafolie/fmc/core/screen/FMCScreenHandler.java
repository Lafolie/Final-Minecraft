package lafolie.fmc.core.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class FMCScreenHandler extends ScreenHandler
{
	public FMCScreenHandler(ScreenHandlerType<?> type, int syncId)
	{
		super(type, syncId);
	}

	@Override
	public boolean canUse(PlayerEntity player)
	{
		return false;
	}
	
	public void createPlayerInventorySlots(PlayerInventory playerInventory)
	{
		int x;
		int y;
		for(y = 0; y < 3; ++y)
		{
			for(x = 0; x < 9; ++x)
			{
				addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}

		for(x = 0; x < 9; ++x)
		{
			addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
		}
	}
}
