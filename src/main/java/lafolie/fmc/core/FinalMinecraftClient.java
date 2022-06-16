package lafolie.fmc.core;

import java.util.Vector;

import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalObject;
import lafolie.fmc.core.elements.Utils;
import lafolie.fmc.core.entity.DamageNumbers;
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
						lines.add(new LiteralText(String.format("Total: %s", total))); // REMOVE LATER
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
				DamageNumbers dmgEntity = (DamageNumbers)client.world.getEntityById(packet.entityID);
				dmgEntity.spawnDamageNumbers(client, packet.amount, packet.type);
			});
		});
	}
}
