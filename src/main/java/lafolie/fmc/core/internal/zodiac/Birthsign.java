package lafolie.fmc.core.internal.zodiac;

import java.util.UUID;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import lafolie.fmc.core.FinalMinecraft;
import lafolie.fmc.core.chrono.DateTime;
import lafolie.fmc.core.elements.ElementalAspect;
import lafolie.fmc.core.entity.AnniversaryEntity;
import lafolie.fmc.core.internal.Components;
import lafolie.fmc.core.util.Maths;
import lafolie.fmc.core.zodiac.ZodiacSign;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;


public class Birthsign implements ComponentV3, AutoSyncedComponent
{
	private static final String ZODIAC_KEY = "Zodiac";
	private static final String ELEMENT_KEY = "Element";
	private ZodiacSign sign;
	private ElementalAspect element;
	private final LivingEntity provider;

	private boolean hasInit = false;

	public Birthsign(Object provider)
	{
		this.provider = (LivingEntity)provider;
		sign = ZodiacSign.LIBRA;
		element = ElementalAspect.DARK;
	}

	public ZodiacSign getZodiacSign()
	{
		return sign;
	}

	public ElementalAspect getElementalAspect()
	{
		return element;
	}

	@Override
	public void readFromNbt(NbtCompound tag)
	{
		if(tag.contains(ZODIAC_KEY))
		{
			sign = ZodiacSign.from(tag.getByte(ZODIAC_KEY));
			element = ElementalAspect.fromOrdinal(tag.getByte(ELEMENT_KEY));
			hasInit = true;
			// FinalMinecraft.log.info("Read birthsign {}", element);
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag)
	{
		if(hasInit)
		{
			// FinalMinecraft.log.info("Write Birthsign {}", element);
			tag.putByte(ZODIAC_KEY, (byte)sign.ordinal());
			tag.putByte(ELEMENT_KEY, (byte)element.ordinal());
		}
	}
	
	public void init()
	{
		if(hasInit)
		{
			// return;
		}

		// FinalMinecraft.log.info("init birthsign");
		hasInit = true;
		if(provider.isBaby())
		{
			initFromDateTime();
		}
		else if(provider instanceof PlayerEntity)
		{
			initFromBirthday();
		}
		else
		{
			initFromUUID();
		}
		Components.BIRTHSIGN.sync(provider);
	}

	private void initFromUUID()
	{
		UUID id = provider.getUuid();
		byte hash = Maths.hashUUIDByte(id);

		element = ElementalAspect.fromOrdinal(((hash & 0x0F) % 6) + 1);
		sign = ZodiacSign.from((hash >>> 4 & 0x0F) % 12);
		// FinalMinecraft.log.info("init UUID: {}", id);
		// FinalMinecraft.log.info("\t hash: {}", hash);
		// FinalMinecraft.log.info("\t element: {}", element);
		// FinalMinecraft.log.info("\t sign: {}", sign);
	}

	private void initFromDateTime()
	{
		DateTime dt = new DateTime(provider.world);
		sign = dt.getZodiacSign();
		element = dt.getElementalAspect();
	}

	private void initFromBirthday()
	{
		DateTime dt = ((AnniversaryEntity)provider).getBirthday();
		sign = dt.getZodiacSign();
		element = dt.getElementalAspect();
		FinalMinecraft.log.info("\t element: {}", element);
		FinalMinecraft.log.info("\t sign: {}", sign);
	}
}
