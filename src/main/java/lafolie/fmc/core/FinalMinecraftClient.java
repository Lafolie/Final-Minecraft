package lafolie.fmc.core;

import java.util.Vector;

import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalObject;
import lafolie.fmc.core.elements.Utils;
import lafolie.fmc.core.internal.Particles;
import lafolie.fmc.core.internal.network.HealthModifiedPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;

public class FinalMinecraftClient implements ClientModInitializer
{

	@Override
	public void onInitializeClient()
	{
		InitContent();
		registerNetworkReceivers();

		ItemTooltipCallback.EVENT.register((stack, context, lines) -> 
		{
			//read NBT and stuff
			ElementalObject elStack = (ElementalObject)(Object)stack;
			
			for(ElementalAspect element : ElementalAspect.values())
			{
				int total = elStack.getWeakResistAffinity(element);
				if(total > 0)
				{
					lines.add(
						new TranslatableText("fmc.core.element.tooltip.elemental",
						new TranslatableText(Utils.ElementLangKeys.get(element))));
						lines.add(new LiteralText(String.format("Total: %s", total))); //ALSO REMOVE LATER
				}

			}
		});
	}
	
	private void InitContent()
	{
		Particles.initClient();
	}

	private void registerNetworkReceivers()
	{
		// HealthModifiedPacket
		ClientPlayNetworking.registerGlobalReceiver(HealthModifiedPacket.ID, (client, handler, buf, responseSender) ->
		{
			// FinalMinecraft.log.info("Receiving packet");

			HealthModifiedPacket packet = new HealthModifiedPacket(buf);
			client.execute(() ->
			{
				Entity ent = client.world.getEntityById(packet.entityID);
				double color;
				switch (packet.type) 
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
				Vec3d adjustedPos = Vec3d.fromPolar(cam.getPitch(), cam.getYaw()).negate().multiply(ent.getBoundingBox().getAverageSideLength() * 0.5d);
				adjustedPos = position.add(adjustedPos);
				client.world.addParticle(Particles.TEXT, true, adjustedPos.x, adjustedPos.y, adjustedPos.z, packet.amount, color, 0);
			});
		});
	}
}
