package lafolie.fmc.core.mixin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.FinalMinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T>
{
	protected LivingEntityRendererMixin(Context ctx) {
		super(ctx);
		// Auto-generated constructor stub
	}

	int x = 0;

	@Inject(at = @At("HEAD"), method = "getOverlay(Lnet/minecraft/entity/LivingEntity;F)I", cancellable = true)
	private static void getOverlay(LivingEntity entity, float whiteOverlayProgress, CallbackInfoReturnable<Integer> info)
	{
		if(entity.hurtTime > 0 || entity.deathTime > 0)
		{
			// int color = OverlayTexture.packUv(OverlayTexture.getU(whiteOverlayProgress), 3);
			// MathHelper.multiplyColors(color & 0xFF000000, 0, 1f, 1f);
			int x = MathHelper.floor(15 - whiteOverlayProgress * 15);
			int color = FinalMinecraftClient.getDamageLUT().getImage().getColor(x, 7);
			color = 0;
			info.setReturnValue(color);
		}
	}
}
