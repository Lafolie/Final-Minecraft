package lafolie.fmc.core.mixin;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import lafolie.fmc.core.util.AlBhed;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.client.util.Session;

@Mixin(SplashTextResourceSupplier.class)
public abstract class SplashTextResourceSupplierMixin
{
	@Final
	@Shadow
    private static Random RANDOM;

	@Final
	@Shadow
    private List<String> splashTexts;

	@Final
	@Shadow
    private Session session;

	@Inject(at = @At("TAIL"), method = "get()Ljava/lang/String;", cancellable = true)
	private void get(CallbackInfoReturnable<String> info)
	{
		Calendar calendar = Calendar.getInstance();
		if(session != null && (calendar.get(Calendar.MINUTE) == 10 || RANDOM.nextInt(splashTexts.size()) == 10))
		{
			info.setReturnValue(AlBhed.toAlBhed((String)info.getReturnValue()));
		}
	}
}
