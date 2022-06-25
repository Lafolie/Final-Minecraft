package lafolie.fmc.core.block;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class HomeCrystalRenderer extends GeoBlockRenderer<HomeCrystalBlockEntity>
{
	public HomeCrystalRenderer()
	{
		super(new HomeCrystalModel());
	}
	
	@Override
	public RenderLayer getRenderType(HomeCrystalBlockEntity animatable, float partialTicks, MatrixStack stack,
			VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			Identifier textureLocation)
	{
		return RenderLayer.getEntityTranslucent(HomeCrystalModel.TEXTURE_ID);
	}
}
