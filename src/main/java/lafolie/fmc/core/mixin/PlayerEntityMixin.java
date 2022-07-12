package lafolie.fmc.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.Particles;
import lafolie.fmc.core.entity.AnniversaryEntity;
import lafolie.fmc.core.entity.DamageNumbers;
import lafolie.fmc.core.particle.system.ParticleAgent;
import lafolie.fmc.core.particle.system.ParticleAgentProvider;
import lafolie.fmc.core.zodiac.BirthsignEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@EnvironmentInterface(value = EnvType.CLIENT, itf = ParticleAgentProvider.class)
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity
	implements DamageNumbers, BirthsignEntity, AnniversaryEntity, ParticleAgentProvider
{
	public PlayerEntityMixin(EntityType<?> type, World world)
	{
		super(type, world);
	}

	@Inject(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setHealth(F)V", shift = At.Shift.AFTER))
	private void applyDamage(DamageSource source, float amount, CallbackInfo info)
	{
		sendHealthModifiedPacket(amount, getLastAttributeUsed());
		PlayerEntity p1 = ((PlayerEntity)(Object)this);
		FinalMinecraft.LOG.info("MY UUID: {}", p1.getUuidAsString(), PlayerEntity.getUuidFromProfile(p1.getGameProfile()));
		FinalMinecraft.LOG.info("\tPROFILE UUID: {}", PlayerEntity.getUuidFromProfile(p1.getGameProfile()));
	}

	@Environment(EnvType.CLIENT)
	private ParticleAgent test;

	@Environment(EnvType.CLIENT)
	@Override
	public void autoPlayParticleAgents()
	{
		test.play();	
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void autoStopParticleAgents()
	{
		test.stop();
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void initParticleAgents() {
		test = Particles.createAgent("crystal_sparkles", this);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void stopParticleAgents()
	{
		test.stop();	
	}

	// @Inject(at = @At("HEAD"), method = "equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V")
	// public void equipStack(EquipmentSlot slot, ItemStack stack, CallbackInfo info)
	// {
	// 	FinalMinecraft.log.info("equipStack {} {}", slot.toString(), stack.toString());
	// }

}
