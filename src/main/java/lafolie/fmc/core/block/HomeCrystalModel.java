package lafolie.fmc.core.block;

import lafolie.fmc.core.util.FMCIdentifier;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HomeCrystalModel extends AnimatedGeoModel<HomeCrystalBlockEntity>
{

	// referenced by the renderer
	public final static Identifier TEXTURE_ID = FMCIdentifier.contentID("textures/geo/home_crystal_model.png");

	@Override
	public Identifier getAnimationFileLocation(HomeCrystalBlockEntity animatable)
	{
		return FMCIdentifier.animationID("block/home_crystal.anim.json");
	}

	@Override
	public Identifier getModelLocation(HomeCrystalBlockEntity object)
	{
		return FMCIdentifier.geometryID("home_crystal_model.geo.json");
	}

	@Override
	public Identifier getTextureLocation(HomeCrystalBlockEntity object)
	{
		return TEXTURE_ID;
	}
	
}
