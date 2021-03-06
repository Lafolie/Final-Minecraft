package lafolie.fmc.core.internal;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import lafolie.fmc.core.internal.chrono.Anniversaries;
import lafolie.fmc.core.internal.element.ElementalStats;
import lafolie.fmc.core.internal.element.ElementalStats_Item;
import lafolie.fmc.core.internal.zodiac.Birthsign;
import lafolie.fmc.core.util.FMCIdentifier;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.Identifier;

public class Components implements
	EntityComponentInitializer,
	ItemComponentInitializer,
	WorldComponentInitializer
{
	public static final Identifier ELEMENTAL_STATS_ID = FMCIdentifier.componentID("elemental_stats");
	public static final Identifier ELEMENTAL_STATS_ITEM_ID = FMCIdentifier.componentID("elemental_stats_item");
	public static final Identifier BIRTHSIGN_ID = FMCIdentifier.componentID("birthsign");
	public static final Identifier ANNIVERSARY_ID = FMCIdentifier.componentID("anniversaries");

	public static final ComponentKey<ElementalStats> ELEMENTAL_STATS = ComponentRegistry.getOrCreate(ELEMENTAL_STATS_ID, ElementalStats.class);
	public static final ComponentKey<ElementalStats_Item> ELEMENTAL_STATS_ITEM = ComponentRegistry.getOrCreate(ELEMENTAL_STATS_ITEM_ID, ElementalStats_Item.class);
	public static final ComponentKey<Birthsign> BIRTHSIGN = ComponentRegistry.getOrCreate(BIRTHSIGN_ID, Birthsign.class);
	public static final ComponentKey<Anniversaries> ANNIVERSARY = ComponentRegistry.getOrCreate(ANNIVERSARY_ID, Anniversaries.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry)
	{
		// Predicate<Class<? extends Entity>> isNonPlayer = c -> c.isAssignableFrom(LivingEntity.class) && !c.isAssignableFrom(PlayerEntity.class);

		// registry.registerFor(isNonPlayer, ELEMENTAL_STATS, ElementalStats::new);
		registry.registerFor(LivingEntity.class, ELEMENTAL_STATS, ElementalStats::new);
		registry.registerForPlayers(ELEMENTAL_STATS, ElementalStats::new, RespawnCopyStrategy.NEVER_COPY);

		registry.registerFor(MobEntity.class, BIRTHSIGN, Birthsign::new);
		registry.registerFor(AnimalEntity.class, BIRTHSIGN, Birthsign::new);
		registry.registerForPlayers(BIRTHSIGN, Birthsign::new, RespawnCopyStrategy.ALWAYS_COPY);
		registry.registerForPlayers(ANNIVERSARY, Anniversaries::new, RespawnCopyStrategy.ALWAYS_COPY);
	}

	@Override
	public void registerItemComponentFactories(ItemComponentFactoryRegistry registry)
	{
		//AGGHHHH THIS TOOK SO LONG TO GET RIGHT!
		registry.register(i -> true, ELEMENTAL_STATS_ITEM, ElementalStats_Item::new);
	}

	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry)
	{
		// registry.register(DATETIME, WorldDate.class, WorldDate::new);	
	}
}
