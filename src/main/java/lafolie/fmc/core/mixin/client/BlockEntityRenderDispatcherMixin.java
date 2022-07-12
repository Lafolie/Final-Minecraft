package lafolie.fmc.core.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lafolie.fmc.core.particle.system.ParticleAgentProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin
{
	@Inject(at = @At(value = "RETURN", ordinal = 2), method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V")
	private void stopAutoPlayParticles(BlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo info)
	{
		if(blockEntity instanceof ParticleAgentProvider)
		{
			((ParticleAgentProvider)blockEntity).autoStopParticleAgents();
		}
	}

	@Inject(at = @At(value = "TAIL"), method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V")
	private void startAutoPlayParticles(BlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo info)
	{
		if(blockEntity instanceof ParticleAgentProvider)
		{
			((ParticleAgentProvider)blockEntity).autoPlayParticleAgents();
		}
	}
}
