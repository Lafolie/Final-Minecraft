package lafolie.fmc.core.block;

import lafolie.fmc.core.FMCBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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
	public int getRenderDistance()
	{
		return 128;
	}

	@Override
	public RenderLayer getRenderType(HomeCrystalBlockEntity animatable, float partialTicks, MatrixStack stack,
			VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			Identifier textureLocation)
	{
		// return RenderLayer.getEntityCutoutNoCull(HomeCrystalModel.TEXTURE_ID);
		return RenderLayer.getEntityTranslucent(HomeCrystalModel.TEXTURE_ID);
	}

	@Override
	public void render(BlockEntity tile, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn,
			int combinedLightIn, int combinedOverlayIn) {
		
		BlockState state = tile.getWorld().getBlockState(tile.getPos());
		if(state.getBlock() == FMCBlocks.HOME_CRYSTAL && !state.get(HomeCrystalBlock.IS_DUMMY))
		{
			super.render(tile, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		}
	}
}
