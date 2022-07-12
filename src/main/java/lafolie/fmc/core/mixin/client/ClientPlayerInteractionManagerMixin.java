package lafolie.fmc.core.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.block.MultiBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin
{
	@Final
	@Shadow
	private MinecraftClient client;

	@ModifyVariable(at = @At("HEAD"), method = "attackBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z")
	private BlockPos attackMultiblock(BlockPos pos)
	{
		return findMultiblockMaster(pos);
	}

	@ModifyVariable(at = @At("HEAD"), method = "updateBlockBreakingProgress(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z")
	private BlockPos updateMultiblockBreakingProgress(BlockPos pos)
	{
		return findMultiblockMaster(pos);
	}

	private BlockPos findMultiblockMaster(BlockPos pos)
	{
		BlockEntity entity = client.world.getBlockEntity(pos);
		if(entity != null && entity instanceof MultiBlockEntity)
		{
			pos = ((MultiBlockEntity)entity).getMasterBlockEntityPos();
		}
		return pos;
	}
}
