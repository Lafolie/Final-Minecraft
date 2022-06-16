package lafolie.fmc.core.internal;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;

import lafolie.fmc.core.internal.elements.ElementalStats;
import lafolie.fmc.core.internal.elements.ElementalStats_Item;
import lafolie.fmc.core.util.FMCIdentifier;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class Components implements
	EntityComponentInitializer,
	ItemComponentInitializer
{
	public static final Identifier ELEMENTAL_STATS_ID = FMCIdentifier.contentID("elemental_stats");//new Identifier("final-minecraft", "elemental_stats");
	public static final Identifier ELEMENTAL_STATS_ITEM_ID = FMCIdentifier.contentID("elemental_stats_item");//new Identifier("final-minecraft", "elemental_stats_item");

	public static final ComponentKey<ElementalStats> ELEMENTAL_STATS = ComponentRegistry.getOrCreate(ELEMENTAL_STATS_ID, ElementalStats.class);
	public static final ComponentKey<ElementalStats_Item> ELEMENTAL_STATS_ITEM = ComponentRegistry.getOrCreate(ELEMENTAL_STATS_ITEM_ID, ElementalStats_Item.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry)
	{
		// Predicate<Class<? extends Entity>> isNonPlayer = c -> c.isAssignableFrom(LivingEntity.class) && !c.isAssignableFrom(PlayerEntity.class);

		// registry.registerFor(isNonPlayer, ELEMENTAL_STATS, ElementalStats::new);
		registry.registerFor(LivingEntity.class, ELEMENTAL_STATS, ElementalStats::new);
		registry.registerForPlayers(ELEMENTAL_STATS, ElementalStats::new, RespawnCopyStrategy.NEVER_COPY);

		
	}

	@Override
	public void registerItemComponentFactories(ItemComponentFactoryRegistry registry)
	{
		//AGGHHHH THIS TOOK SO LONG TO GET RIGHT!
		registry.register(i -> true, ELEMENTAL_STATS_ITEM, ElementalStats_Item::new);
	}
	
}
