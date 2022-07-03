package lafolie.fmc.core.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.blaze3d.systems.RenderSystem;

import lafolie.fmc.core.util.FMCIdentifier;
import lafolie.fmc.core.util.LangTable;
import lafolie.fmc.core.util.RealTime;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.MathHelper;

public class HomeCrystalScreen extends HandledScreen<HomeCrystalScreenHandler>
{
	private static final Identifier TEXTURE = FMCIdentifier.contentID("textures/gui/home_crystal.png");
	private static final LangTable LANG = new LangTable("final-minecraft.charge.");
	HomeCrystalScreenHandler handler;

	static
	{
		LANG.addPrefixed("danger", 0xFF0040);
		LANG.addPrefixed("low", 0xFF8020);
		LANG.addPrefixed("nominal", 0x40a0FF);
		LANG.addPrefixed("charging", 0x20FF80);
		LANG.addPrefixed("explode", 0xFF0040);
		LANG.addPrefixed("mp", 0xF4DB16);
		LANG.addPrefixed("job", 0xF4DB16);
		LANG.addPrefixed("no_pedestal", 0xFF0040);
		LANG.addPrefixed("yes_pedestal", 0xF4DB16);
		LANG.addPrefixed("charged", 0xFFFFFF);
	}

	public HomeCrystalScreen(HomeCrystalScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
		this.handler = (HomeCrystalScreenHandler)handler;
	}
	
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		matrices.push();
		matrices.translate(x, y, 0);

		float chargePercent = handler.getChargePercent();
		float batteryPercent = handler.getBatteryPercent();
		boolean hasPedestal = handler.getHasPedestal();

		drawTexture(matrices, 0, 0, 0, 0, backgroundWidth, backgroundHeight);
		drawArrow(matrices, batteryPercent);
		drawPedestal(matrices, hasPedestal);
		drawCrystal(matrices, chargePercent, getStatusText(chargePercent, batteryPercent, hasPedestal));
		
		matrices.pop();
	}

	private Text getStatusText(float chargePercent, float batteryPercent, boolean hasPedestal)
	{
		if(!hasPedestal || chargePercent < 0.01)
		{
			return LANG.getText("danger");
		}

		if(batteryPercent > 0)
		{
			return LANG.getText("charging");
		}

		if(chargePercent < 0.5)
		{
			return LANG.getText("low");
		}
		return LANG.getText("nominal");
	}

	private void drawCrystal(MatrixStack matrices, float progress, Text statusText)
	{
		int h = MathHelper.floor(61 * progress);
		int y = 61 - h;
		int w = textRenderer.getWidth(statusText);
		drawHorizontalLine(matrices, 88, 99 + w + 2, y + 6, 0xFFFFFFFF);
		drawTexture(matrices, 80, 6 + y, 176, y, 19, h);
		fill(matrices, 100, y + 7, 100 + w + 2, y + 18, 0x80000000);
		drawTextWithShadow(matrices, textRenderer, statusText, 101, y + 8, 0xFFFFFFFF);
	}

	private void drawArrow(MatrixStack matrices, float progress)
	{
		int w = MathHelper.floor(19 * progress);
		int x = 19 - w;
		drawTexture(matrices, 49 + x, 49, 176 + x, 69, w, 12);
	}

	private void drawPedestal(MatrixStack matrices, boolean isPresent)
	{
		int x = isPresent ? 176 : 184;
		drawTexture(matrices, 86, 69, x, 61, 8, 8);
	}

	private void drawCrystalTooltip(MatrixStack matrices, int mouseX, int mouseY)
	{
		if(isPointWithinBounds(80, 6, 19, 61, mouseX, mouseY))
		{
			float charge = handler.getChargePercent();
			int countDown = handler.getExplosionCountdown();
			List<Text> list = new ArrayList<>();
			list.add(LANG.getText("charged", MathHelper.floor(charge * 100)));

			if(countDown > 0)
			{
				int time = (RealTime.SECONDS_PER_HOUR - countDown) / (handler.getHasPedestal() ? 1 : 288);
				list.add(LANG.getText("explode", RealTime.formatSeconds(time)));
			}

			list.add(LANG.getText(handler.getHasPedestal() ? "yes_pedestal" : "no_pedestal"));
			
			if(charge > 0)
			{
				list.add(LANG.getText("mp"));
			}

			if(charge > 0.5)
			{
				list.add(LANG.getText("job"));
			}

			renderTooltip(matrices, list, Optional.empty(), mouseX, mouseY);
		}
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices, mouseX, mouseY);
		drawCrystalTooltip(matrices, mouseX, mouseY);
	}
	
	@Override
	protected void init()
	{
		super.init();
		titleX = 8;//(backgroundWidth - textRenderer.getWidth(title)) / 10;
	}
}
