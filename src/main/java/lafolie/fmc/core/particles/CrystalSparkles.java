package lafolie.fmc.core.particles;


import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import lafolie.fmc.core.util.Maths;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class CrystalSparkles extends SpriteBillboardParticle
{
	private final SpriteProvider provider;
	private Vec3f scaleVec = Vec3f.POSITIVE_Z;
	private Vec3f prevScale;
	private boolean rescale;

	public CrystalSparkles(ClientWorld clientWorld, double x, double y, double z, double spr, SpriteProvider provider)
	{
		super(clientWorld, x, y, z);
		this.provider = provider;
		maxAge = 10;
		velocityY = 0.001;

		if(spr == 0d)
		{
			rescale = true;
			scaleVec.add(4.5f, 0.5f, 1f);
		}
		else if(spr == 3d)
		{
			velocityMultiplier = 0;
			maxAge = 10;
			scaleVec.add(1f, 10f, 0f);
		}
		else
		{
			scaleVec.add(1f, 1f, 0f);
		}

		prevScale = scaleVec;
		setSprite(provider.getSprite((int)spr, 3));;
	}

	@Override
	public void tick()
	{
		super.tick();
		if(!dead)
		{
			float life = (float)age / (float)maxAge;
			if(rescale)
			{
				prevScale = scaleVec;
				scaleVec.set((1f - life) * 4f + 0.5f, life * 4f + 0.5f, 1f);
			}
			setAlpha(1f - life);
		}
	}

	// @Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta)
	{
		Quaternion quaternion;
		Vec3d vec3d = camera.getPos();
		float x = (float)(MathHelper.lerp((double)tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float y = (float)(MathHelper.lerp((double)tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float z = (float)(MathHelper.lerp((double)tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
		if (this.angle == 0.0f) {
			quaternion = camera.getRotation();
		} else {
			quaternion = new Quaternion(camera.getRotation());
			float i = MathHelper.lerp(tickDelta, this.prevAngle, this.angle);
			quaternion.hamiltonProduct(Vec3f.POSITIVE_Z.getRadialQuaternion(i));
		}
		float sx = (float)(MathHelper.lerp((double)tickDelta, prevScale.getX(), scaleVec.getX()));
		float sy = (float)(MathHelper.lerp((double)tickDelta, prevScale.getY(), scaleVec.getY()));
		Vec3f vec3f = new Vec3f(-1.0f, -1.0f, 0.0f);
		vec3f.rotate(quaternion);
		Vec3f[] vec3fs = new Vec3f[]{new Vec3f(-1.0f, -1.0f, 0.0f), new Vec3f(-1.0f, 1.0f, 0.0f), new Vec3f(1.0f, 1.0f, 0.0f), new Vec3f(1.0f, -1.0f, 0.0f)};
		float j = this.getSize(tickDelta);
		for (int k = 0; k < 4; ++k) {
			Vec3f vec3f2 = vec3fs[k];
			vec3f2.multiplyComponentwise(sx, sy, 1);
			vec3f2.rotate(quaternion);
			vec3f2.scale(j);
			vec3f2.add(x, y, z);
		}

		float l = this.getMinU();
		float m = this.getMaxU();
		float n = this.getMinV();
		float o = this.getMaxV();
		int p = 0xF000F0;//this.getBrightness(tickDelta);
		vertexConsumer.vertex(vec3fs[0].getX(), vec3fs[0].getY(), vec3fs[0].getZ()).texture(m, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
		vertexConsumer.vertex(vec3fs[1].getX(), vec3fs[1].getY(), vec3fs[1].getZ()).texture(m, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
		vertexConsumer.vertex(vec3fs[2].getX(), vec3fs[2].getY(), vec3fs[2].getZ()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
		vertexConsumer.vertex(vec3fs[3].getX(), vec3fs[3].getY(), vec3fs[3].getZ()).texture(l, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
	}

	// protected CrystalSparkles(ClientWorld clientWorld, double d, double e, double f)
	// {
	// 	super(clientWorld, d, e, f);
	// }

	// @Override
	// protected float getMinU()
	// {
	// 	return uv.x;
	// }

	// @Override
	// protected float getMaxU()
	// {
	// 	return uv.x + 0.5f;
	// }

	// @Override
	// protected float getMinV()
	// {
	// 	return uv.y;
	// }

	// @Override
	// protected float getMaxV()
	// {
	// 	return uv.y + 0.5f;
	// }

	@Override
	public ParticleTextureSheet getType()
	{
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory
	implements ParticleFactory<DefaultParticleType>
	{
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider)
		{
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new CrystalSparkles(clientWorld, d, e, f, g, spriteProvider);
		}
	}
}
