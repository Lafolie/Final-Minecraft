package lafolie.fmc.core;

import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalAttribute;
import lafolie.fmc.core.elements.ElementalObject;
import lafolie.fmc.core.elements.Utils;
import lafolie.fmc.core.internal.Particles;
import lafolie.fmc.core.internal.network.HealthModifiedPacket;
import lafolie.fmc.core.mixin.ItemStackMixin;
import lafolie.fmc.core.particles.TextBillboardParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.Registry;

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
				

				client.world.addParticle(Particles.TEXT, true, ent.getX(), ent.getBodyY(1), ent.getZ(), packet.amount, 0, 0);
			});
		});
	}
}
