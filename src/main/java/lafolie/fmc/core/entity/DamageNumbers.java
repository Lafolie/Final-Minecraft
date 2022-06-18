package lafolie.fmc.core.entity;

import lafolie.fmc.core.elements.ElementalAttribute;
import lafolie.fmc.core.internal.Particles;
import lafolie.fmc.core.internal.network.HealthModifiedPacket;
import lafolie.fmc.core.internal.network.HealthModifiedPacket.EventType;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;

public interface DamageNumbers
{
	/**
	 * Get the last ElementalAttribute used in the damage algorithm
	 * @return ElementalAttribute
	 */
	public ElementalAttribute getLastAttributeUsed();

	/**
	 * Called by semdHealthModifiedPacket to determine which players to send
	 * the packet to.
	 * @param player the player to consider
	 * @return whether to send the packet or not
	 */
	public default boolean shouldSendPacket(ServerPlayerEntity player)
	{
		return true;
	}

	/**
	 * Creates and sends a packet to spawn damage number particles on the client
	 * @param amount the amount of damage to display
	 * @param attribute affects what colour to render the particle with
	 * @return the packet that was sent
	 */
	// @Environment(EnvType.SERVER)
	public default void sendHealthModifiedPacket(float amount, ElementalAttribute attribute)
	{
		// FinalMinecraft.log.info("Sending packet");
		Entity self = (Entity)this;

		HealthModifiedPacket.EventType type = EventType.NORMAL;
		if(attribute != null)
		{
			switch (attribute) 
			{
				case RESISTANCE:
				case IMMUNITY:
					type = EventType.RESIST;
					break;

				case ABSORBTION:
				case REVIVE:
					type = EventType.HEAL;
					break;

				case WEAKNESS:
				case FATAL:
					type = EventType.EFFECTIVE;
					break;
			
				default:
					break;
			}
		}
		HealthModifiedPacket packet = new HealthModifiedPacket(self.getId(), type, amount);
		PacketByteBuf buf = PacketByteBufs.create();
		for(ServerPlayerEntity player : PlayerLookup.tracking(self))
		{
			if(shouldSendPacket(player))
			{
				ServerPlayNetworking.send(player, HealthModifiedPacket.ID, packet.write(buf));
			}
		}
		if(self instanceof ServerPlayerEntity)
		{
			ServerPlayNetworking.send((ServerPlayerEntity)self, HealthModifiedPacket.ID, packet.write(buf));
		}
	}

	// @Environment(EnvType.CLIENT)
	public default void spawnDamageNumbers(MinecraftClient client, float amount, HealthModifiedPacket.EventType type)
	{
		Entity ent = (Entity)this;
		double color;

		switch (type) 
		{
			case NORMAL:
				color = ColorHelper.Argb.getArgb(0, 255, 255, 255);
				break;

			case HEAL:
				color = ColorHelper.Argb.getArgb(0, 143, 206, 59);
				break;

			case RESIST:
				color = ColorHelper.Argb.getArgb(0, 64, 64, 72);
				break;

			case EFFECTIVE:
				color = ColorHelper.Argb.getArgb(0, 219, 171, 28);
				break;

			case POISON:
				color = ColorHelper.Argb.getArgb(0, 162, 28, 219);
				break;
		
			default:
				color = ColorHelper.Argb.getArgb(0, 255, 255, 255);
				break;
		}

		Vec3d position = new Vec3d(ent.getX(), ent.getBodyY(1d), ent.getZ());
		Entity cam = client.getCameraEntity();
		double offsetBy = (Entity)client.player == this ? 0.5d : -0.5d;
		Vec3d adjustedPos = Vec3d.fromPolar(cam.getPitch(), cam.getYaw()).multiply(ent.getBoundingBox().getAverageSideLength() * offsetBy);
		adjustedPos = position.add(adjustedPos);
		client.world.addParticle(Particles.TEXT, true, adjustedPos.x, adjustedPos.y, adjustedPos.z, amount, color, 0d);
	}
}
