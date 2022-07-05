package lafolie.fmc.core.particle;

import java.util.ArrayList;
import java.util.List;

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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class TextBillboardParticle extends BillboardParticle
{
	private final List<OrderedText> characters = new ArrayList<>();
	private final float textWidth;
	private final float fadeAge;
	private final int color;

	protected TextBillboardParticle(ClientWorld clientWorld, double x, double y, double z, double number, double color)
	{
		super(clientWorld, x, y, z);

		collidesWithWorld = false;
		gravityStrength = 0f;
		velocityY = 0d;
		maxAge = 40;
		fadeAge = maxAge - 25;
		this.color = MathHelper.floor(color);
		String text = String.format("%.0f", number);
		textWidth =  MinecraftClient.getInstance().textRenderer.getWidth(text);
		int n = 0;
		while(n < text.length())
		{
			characters.add(OrderedText.styledForwardsVisitedString(text.substring(n, n+1), Style.EMPTY));
			n++;
		}

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
	public void tick()
	{
		super.tick();
		if(!dead && age >= maxAge - fadeAge)
		{
			float ageDiff = Math.max(maxAge - age, 0f);
			alpha = ageDiff / fadeAge;
		}
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta)
	{
		TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
		
		Vec3d vec3d = camera.getPos();
		float x = (float)(MathHelper.lerp((double)tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float y = (float)(MathHelper.lerp((double)tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float z = (float)(MathHelper.lerp((double)tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
		float life = MathHelper.lerp(tickDelta, (float)age - 1f, (float)age);

		if(characters.size() < 0 || alpha <= 0f || scale <= 0f)
		{
			return;
		}

		float xOffset = textWidth / -2f;
		int intAlpha = MathHelper.floor(255f * alpha) << 24;
		int intColor = color | intAlpha;
		int outlineColor = intAlpha;

		MatrixStack matStack = new MatrixStack();
		matStack.push();
		matStack.translate(x, y, z);
		matStack.multiply(camera.getRotation());
		matStack.scale(-0.04f, -0.04f, -1f);

		VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
		float n = 0;
		for(OrderedText txt : characters)
		{
			float i = (life - n) / 5f;
			if(i < 0f)
			{
				continue;
			}

			if(i > 0.5f)
			{
				i = 1f - i;
			}

			float yOffset = Math.max(0, MathHelper.lerp(i, 0f, 24f));
			renderer.drawWithOutline(txt, xOffset, -yOffset, intColor, outlineColor, matStack.peek().getPositionMatrix(), immediate, LightmapTextureManager.MAX_LIGHT_COORDINATE);
			xOffset += renderer.getWidth(txt);
			n += 1f;
		}
		immediate.draw();
		
		matStack.pop();
	}

	@Environment(value=EnvType.CLIENT)
	public static class Factory
	implements ParticleFactory<DefaultParticleType>
	{
		public Factory(SpriteProvider sp) { }

		@Override
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i)
		{
			return new TextBillboardParticle(clientWorld, d, e, f, g, h);
		}
	}
}
