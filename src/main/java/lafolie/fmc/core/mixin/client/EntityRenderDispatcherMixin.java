package lafolie.fmc.core.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import lafolie.fmc.core.particle.system.ParticleAgentProvider;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin
{
	@Inject(at = @At("TAIL"), method = "shouldRender(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/render/Frustum;DDD)Z", locals = LocalCapture.CAPTURE_FAILHARD)
	public <E extends Entity> void handleParticleAgents(E entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable info, EntityRenderer<E> renderer)
	{
		if(entity instanceof ParticleAgentProvider)
		{
			if(renderer.shouldRender(entity, frustum, x, y, z))
			{
				((ParticleAgentProvider)entity).autoPlayParticleAgents();
			}
			else
			{
				((ParticleAgentProvider)entity).autoStopParticleAgents();
			}
		}
	}
	
}
