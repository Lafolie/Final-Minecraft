package lafolie.fmc.core.particles;


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

	private OrderedText text = OrderedText.styledForwardsVisitedString("999", Style.EMPTY);
	// private static TextRenderer renderer = MinecraftClient.getInstance().textRenderer;

	protected TextBillboardParticle(ClientWorld clientWorld, double d, double e, double f)
	{
		super(clientWorld, d, e, f);
		init();
	}

    protected TextBillboardParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i)
	{
		super(clientWorld, d, e, f, g, h, i);
		init();
	}

	private void init()
	{
		collidesWithWorld = false;
		gravityStrength = 1;
		velocityY = 5;
		maxAge = 120;
		// this.
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

		// Quaternion quaternion;
		Vec3d vec3d = camera.getPos();
		float x = (float)(MathHelper.lerp((double)tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float y = (float)(MathHelper.lerp((double)tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float z = (float)(MathHelper.lerp((double)tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

		if(alpha == 0 || scale == 0)
		{
			return;
		}

		int color = 0xFFFFFFFF;
		int outlineColor = 0x00000000;

		// Quaternion quat = camera.getRotation();
		
		MatrixStack matStack = new MatrixStack();
		matStack.push();
		matStack.translate(x, y, z);
		matStack.multiply(camera.getRotation());

		VertexConsumerProvider vertConProv = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());

		renderer.drawWithOutline(text, x, y, color, outlineColor, matStack.peek().getPositionMatrix(), vertConProv, Integer.MAX_VALUE);

		vertexConsumer.vertex(0, 0, 0);
		vertexConsumer.vertex(0, 0, 0);
		vertexConsumer.vertex(0, 0, 0);
		vertexConsumer.vertex(0, 0, 0);
// public void vertex(float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ)

		
        // if (this.angle == 0.0f) {
        //     quaternion = camera.getRotation();
        // } else {
        //     quaternion = new Quaternion(camera.getRotation());
        //     float i = MathHelper.lerp(tickDelta, this.prevAngle, this.angle);
        //     quaternion.hamiltonProduct(Vec3f.POSITIVE_Z.getRadialQuaternion(i));
        // }
        // Vec3f vec3f = new Vec3f(-1.0f, -1.0f, 0.0f);
        // vec3f.rotate(quaternion);
        // Vec3f[] vec3fs = new Vec3f[]{new Vec3f(-1.0f, -1.0f, 0.0f), new Vec3f(-1.0f, 1.0f, 0.0f), new Vec3f(1.0f, 1.0f, 0.0f), new Vec3f(1.0f, -1.0f, 0.0f)};
        // float j = this.getSize(tickDelta);
        // for (int k = 0; k < 4; ++k) {
        //     Vec3f vec3f2 = vec3fs[k];
        //     vec3f2.rotate(quaternion);
        //     vec3f2.scale(j);
        //     vec3f2.add(f, g, h);
        // }
        // float l = this.getMinU();
        // float m = this.getMaxU();
        // float n = this.getMinV();
        // float o = this.getMaxV();
        // int p = this.getBrightness(tickDelta);
        // vertexConsumer.vertex(vec3fs[0].getX(), vec3fs[0].getY(), vec3fs[0].getZ()).texture(m, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
        // vertexConsumer.vertex(vec3fs[1].getX(), vec3fs[1].getY(), vec3fs[1].getZ()).texture(m, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
        // vertexConsumer.vertex(vec3fs[2].getX(), vec3fs[2].getY(), vec3fs[2].getZ()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
        // vertexConsumer.vertex(vec3fs[3].getX(), vec3fs[3].getY(), vec3fs[3].getZ()).texture(l, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
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
			return new TextBillboardParticle(clientWorld, d, e, f);
		}
	}
}
