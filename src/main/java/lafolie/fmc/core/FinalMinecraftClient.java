package lafolie.fmc.core;

import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.elements.ElementalObject;
import lafolie.fmc.core.entity.DamageNumbers;
import lafolie.fmc.core.internal.network.HealthModifiedPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

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
						new TranslatableText(element.getLangKey())));
						lines.add(new LiteralText(String.format("Total: %s", total))); //TODO REMOVE LATER
				}

			}
		});
	}
	
	private void InitContent()
	{
		Particles.initClient();
		initBlocks();
	}

	private void initBlocks()
	{
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.CRYSTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.FIRE_CRYSTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.ICE_CRYSTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.THUNDER_CRYSTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.WIND_CRYSTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.WATER_CRYSTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.EARTH_CRYSTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.HOLY_CRYSTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.DARK_CRYSTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.POISON_CRYSTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FMCBlocks.GRAVITY_CRYSTAL, RenderLayer.getTranslucent());

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
				if(dmgEntity != null)
				{
					dmgEntity.spawnDamageNumbers(client, packet.amount, packet.type);
				}
				else
				{
					FinalMinecraft.log.info("dmgEntity was null");
				}
			});
		});
	}
}
