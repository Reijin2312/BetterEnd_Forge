package org.betterx.betterend.util;

import org.betterx.bclib.items.DebugDataItem;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.registry.EndItems;
import org.betterx.betterend.world.structures.village.VillagePools;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class DebugHelpers {
    public static void generateDebugItems() {
        BetterEnd.LOGGER.warning("Generating Debug Helpers");

        EndItems.registerEndItem(
                "debug/village_loot",
                DebugDataItem.forLootTable(LootTableUtil.VILLAGE_LOOT, Items.IRON_INGOT)
        );

        EndItems.registerEndItem(
                "debug/village_bonus",
                DebugDataItem.forLootTable(LootTableUtil.VILLAGE_BONUS_LOOT, Items.DIAMOND)
        );

        EndItems.registerEndItem(
                "debug/village_template",
                DebugDataItem.forLootTable(LootTableUtil.VILLAGE_TEMPLATE_LOOT, Items.GOLD_INGOT)
        );

        EndItems.registerEndItem(
                "debug/biome_loot",
                DebugDataItem.forLootTable(LootTableUtil.BIOME_CHEST, Items.OAK_LEAVES)
        );

        EndItems.registerEndItem(
                "debug/jigsaw_entrance",
                DebugDataItem.forHouseEntranceJigSaw(BetterEnd.MOD_ID, null, Items.OAK_DOOR)
        );

        EndItems.registerEndItem(
                "debug/jigsaw_street_entrance",
                DebugDataItem.forHouseEntranceJigSaw(BetterEnd.MOD_ID, VillagePools.HOUSES_KEY, Items.IRON_DOOR)
        );

        EndItems.registerEndItem(
                "debug/jigsaw_street",
                DebugDataItem.forSteetJigSaw(
                        BetterEnd.MOD_ID,
                        VillagePools.STREET_KEY,
                        Items.ENDER_PEARL
                )
        );

        EndItems.registerEndItem(
                "debug/jigsaw_street_deco",
                DebugDataItem.forStreetDecorationJigSaw(
                        BetterEnd.MOD_ID,
                        VillagePools.STREET_DECO_KEY,
                        Items.ENDER_EYE
                )
        );
        EndItems.registerEndItem(
                "debug/jigsaw_street_big_deco",
                DebugDataItem.forDecorationJigSaw(
                        BetterEnd.MOD_ID,
                        VillagePools.DECORATIONS_KEY,
                        Items.SLIME_BALL
                )
        );

        EndItems.registerEndItem(
                "debug/jigsaw_big_deco",
                DebugDataItem.forDecorationJigSaw(
                        BetterEnd.MOD_ID,
                        null,
                        Items.TURTLE_HELMET
                )
        );

        EndItems.registerEndItem(
                "debug/jigsaw_deco",
                DebugDataItem.forStreetDecorationJigSaw(
                        BetterEnd.MOD_ID,
                        null,
                        Items.LANTERN
                )
        );

        EndItems.registerEndItem(
                "debug/fill_base_void",
                new DebugDataItem((player, entity, useOnContext) -> DebugDataItem.fillStructureEntityBounds(
                        useOnContext, entity,
                        state -> state.is(Blocks.END_STONE),
                        Blocks.STRUCTURE_VOID.defaultBlockState(),
                        false
                ), false, BuiltInRegistries.ITEM.getKey(Items.WATER_BUCKET))
        );

        Item item = EndItems.registerEndItem(
                "debug/fill_air",
                new DebugDataItem((player, entity, useOnContext) -> DebugDataItem.fillStructureEntityBounds(
                        useOnContext, entity,
                        state -> state.isAir() || state.is(Blocks.STRUCTURE_VOID),
                        Blocks.STRUCTURE_VOID.defaultBlockState(),
                        true
                ), false, BuiltInRegistries.ITEM.getKey(Items.BUCKET))
        );
    }

}
