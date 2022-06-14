package lafolie.fmc.core.particles;


import lafolie.fmc.core.FinalMinecraft;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class TextBillboardParticle extends BillboardParticle
{

	private OrderedText text;// = OrderedText.styledForwardsVisitedString("999", Style.EMPTY);

    protected TextBillboardParticle(ClientWorld clientWorld, double x, double y, double z, double number, double color)
	{
		super(clientWorld, x, y, z);
		// init();
		collidesWithWorld = false;
		gravityStrength = 1f;
		velocityY = 0.25;
		maxAge = 30;
		FinalMinecraft.log.info("Number: {}", number);
		text = OrderedText.styledBackwardsVisitedString(String.format("%1.0f", number), Style.EMPTY);
	}


	@Override
	protected float getMinU()
	{
		return 0;
	}

	@Override
	protected float getMaxU()
	{
		return 1;
	}

	@Override
	protected float getMinV()
	{
		return 0;
	}

	@Override
	protected float getMaxV()
	{
		return 1;
	}

	@Override
	public ParticleTextureSheet getType()
	{
		return ParticleTextureSheet.CUSTOM;
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta)
	{

		TextRenderer renderer = MinecraftClient.getInstance().textRenderer;

		Vec3d vec3d = camera.getPos();
		float x = (float)(MathHelper.lerp((double)tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float y = (float)(MathHelper.lerp((double)tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float z = (float)(MathHelper.lerp((double)tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

		if(alpha == 0 || scale == 0)
		{
			return;
		}

		float offset = renderer.getWidth(text) / 2;

		int color = 0xFFFFFFFF;
		int outlineColor = 0x00000000;

		MatrixStack matStack = new MatrixStack();
		matStack.push();
		matStack.translate(x, y, z);
		matStack.multiply(camera.getRotation());
		matStack.scale(-0.05f, -0.05f, -1f);

		// vertConProv = VertexConsumerProvider.immediate(layerBuffers, fallbackBuffer)
		// renderer.drawWithShadow(matStack, text, x, y, color);

        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
		renderer.drawWithOutline(text, -offset, 0, color, outlineColor, matStack.peek().getPositionMatrix(), immediate, LightmapTextureManager.MAX_LIGHT_COORDINATE);
		immediate.draw();
		
		matStack.pop();
	}

	@Environment(value=EnvType.CLIENT)
	public static class Factory
	implements ParticleFactory<DefaultParticleType>
	{
		// private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider sp)
		{
			// spriteProvider = sp;
		}

		@Override
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i)
		{
			return new TextBillboardParticle(clientWorld, d, e, f, g, h);
		}
	}
}
