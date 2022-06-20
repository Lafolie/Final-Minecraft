package lafolie.fmc.core.internal.chrono;

import java.util.HashMap;
import java.util.Map;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import lafolie.fmc.core.chrono.DateTime;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class Anniversaries implements ComponentV3, AutoSyncedComponent
{
	private static final String LIST_KEY = "entries";
	private static final String LONG_KEY = "l";
	private static final String STRING_KEY = "s";

	private final Entity provider;
	private final Map<String, Long> dates = new HashMap<>();

	public Anniversaries(Entity provider)
	{
		this.provider = provider;
	}

	public boolean containsKey(String key)
	{
		return dates.containsKey(key);
	}

	public DateTime get(String key)
	{
		return new DateTime(dates.getOrDefault(key, 0L));
	}

	public long getRaw(String key)
	{
		return dates.getOrDefault(key, 0L);
	}

	public void set(String key, DateTime date)
	{
		dates.put(key, date.getTicks());
	}

	public void setRaw(String key, long ticks)
	{
		dates.put(key, ticks);
	}

	@Override
	public void readFromNbt(NbtCompound tag)
	{
		if(tag.contains(LIST_KEY))
		{
			NbtList list = tag.getList(LIST_KEY, NbtType.COMPOUND);
			for(int n = 0; n < list.size(); n++)
			{
				NbtCompound nbt = list.getCompound(n);
				dates.put(nbt.getString(STRING_KEY), nbt.getLong(LONG_KEY));
			}
		}	
	}

	@Override
	public void writeToNbt(NbtCompound tag)
	{
		NbtList list = new NbtList();
		for(Map.Entry<String, Long> date : dates.entrySet())
		{
			NbtCompound nbt = new NbtCompound();
			nbt.putString(STRING_KEY, date.getKey());
			nbt.putLong(LONG_KEY, date.getValue());
			list.add(nbt);
		}
		tag.put(LIST_KEY, list);
	}	
}
