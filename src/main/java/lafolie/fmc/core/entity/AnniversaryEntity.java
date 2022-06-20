package lafolie.fmc.core.entity;

import lafolie.fmc.core.chrono.DateTime;
import lafolie.fmc.core.internal.Components;
import lafolie.fmc.core.internal.chrono.Anniversaries;
import lafolie.fmc.core.util.Maths;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface AnniversaryEntity
{
	private Anniversaries getComponent()
	{
		return Components.ANNIVERSARY.get(this);
	}

	/**
	 * Called internally.
	 * @param world
	 */
	public default void tryInit(World world)
	{
		Anniversaries anvs = getComponent();
		if(!anvs.containsKey("birthday"))
		{
			short birthday = Maths.hashUUIDShort(((Entity)this).getUuid());
			anvs.set("birthday", new DateTime(birthday));
			anvs.setRaw("joinday", world.getTimeOfDay());
		}
	}

	public default DateTime get(String key)
	{
		return getComponent().get(key);
	}

	public default DateTime getBirthday()
	{
		return get("birthday");
	}

	public default DateTime getJoinday()
	{
		return get("joinday");
	}

	public default void set(String key, DateTime date)
	{
		if(!((Entity)this).world.isClient)
		{
			getComponent().set(key, date);
			Components.ANNIVERSARY.sync(this);
		}
	}
}
