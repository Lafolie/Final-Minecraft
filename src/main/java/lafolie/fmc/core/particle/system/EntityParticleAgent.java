package lafolie.fmc.core.particle.system;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class EntityParticleAgent extends ParticleAgent
{
	public final Entity owner;

	protected EntityParticleAgent(Entity owner, ParticleSystem system)
	{
		super(system);
		this.owner = owner;
	}

	@Override
	public Vec3d getPosition()
	{
		return owner.getPos();
	}

}
