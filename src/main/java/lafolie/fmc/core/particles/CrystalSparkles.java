package lafolie.fmc.core.particles;


import lafolie.fmc.core.util.Maths;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class CrystalSparkles extends BillboardParticle
{
	private Vec2f uv;
	private Vec3f scale = Vec3f.POSITIVE_Z;
	private Vec3f prevScale;
	private boolean rescale;

	public CrystalSparkles(ClientWorld clientWorld, double x, double y, double z, double spr)
	{
		super(clientWorld, x, y, z);
		uv = Maths.indexToUV(spr, 8);
		maxAge = 60;
		velocityY = 0.2;

		if(spr == 0)
		{
			rescale = true;
			scale.add(4.5f, 0.5f, 0f);
		}
		else if(spr == 3)
		{
			velocityMultiplier = 0;
			maxAge = 10;
			scale.add(1f, 10f, 0f);
		}
		else
		{
			scale.add(1f, 1f, 0f);
		}

		prevScale = scale;
	}

	@Override
	public void tick()
	{
		super.tick();
		if(!dead)
		{
			float life = age / maxAge;
			if(rescale)
			{
				scale.set((1f - life) * 4f + 0.5f, life * 4f + 0.5f, 1f);
			}
			setAlpha(life);
		}
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		Quaternion quaternion;
		Vec3d vec3d = camera.getPos();
		float f = (float)(MathHelper.lerp((double)tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float)(MathHelper.lerp((double)tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float)(MathHelper.lerp((double)tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
		if (this.angle == 0.0f) {
			quaternion = camera.getRotation();
		} else {
			quaternion = new Quaternion(camera.getRotation());
			float i = MathHelper.lerp(tickDelta, this.prevAngle, this.angle);
			quaternion.hamiltonProduct(Vec3f.POSITIVE_Z.getRadialQuaternion(i));
		}
		Vec3f vec3f = new Vec3f(-1.0f, -1.0f, 0.0f);
		vec3f.rotate(quaternion);
		Vec3f[] vec3fs = new Vec3f[]{new Vec3f(-1.0f, -1.0f, 0.0f), new Vec3f(-1.0f, 1.0f, 0.0f), new Vec3f(1.0f, 1.0f, 0.0f), new Vec3f(1.0f, -1.0f, 0.0f)};
		float j = this.getSize(tickDelta);
		for (int k = 0; k < 4; ++k) {
			Vec3f vec3f2 = vec3fs[k];
			vec3f2.rotate(quaternion);
			vec3f2.scale(j);
			vec3f2.add(f, g, h);
		}
		float l = this.getMinU();
		float m = this.getMaxU();
		float n = this.getMinV();
		float o = this.getMaxV();
		int p = this.getBrightness(tickDelta);
		vertexConsumer.vertex(vec3fs[0].getX(), vec3fs[0].getY(), vec3fs[0].getZ()).texture(m, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
		vertexConsumer.vertex(vec3fs[1].getX(), vec3fs[1].getY(), vec3fs[1].getZ()).texture(m, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
		vertexConsumer.vertex(vec3fs[2].getX(), vec3fs[2].getY(), vec3fs[2].getZ()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
		vertexConsumer.vertex(vec3fs[3].getX(), vec3fs[3].getY(), vec3fs[3].getZ()).texture(l, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
	}

	// protected CrystalSparkles(ClientWorld clientWorld, double d, double e, double f)
	// {
	// 	super(clientWorld, d, e, f);
	// }

	@Override
	protected float getMinU()
	{
		return uv.x;
	}

	@Override
	protected float getMaxU()
	{
		return uv.x + 0.5f;
	}

	@Override
	protected float getMinV()
	{
		return uv.y;
	}

	@Override
	protected float getMaxV()
	{
		return uv.y + 0.5f;
	}

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
			return new CrystalSparkles(clientWorld, d, e, f, g);
		}
	}
}
