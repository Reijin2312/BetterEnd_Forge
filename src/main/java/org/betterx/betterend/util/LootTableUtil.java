package org.betterx.betterend.util;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.registry.EndBiomes;
import org.betterx.betterend.registry.EndItems;
import org.betterx.betterend.registry.EndTemplates;

import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;

public class LootTableUtil {
    public static final ResourceLocation VILLAGE_LOOT = BetterEnd.makeID("chests/end_village_loot");
    public static final ResourceLocation VILLAGE_TEMPLATE_LOOT = BetterEnd.makeID("chests/end_village_template_loot");
    public static final ResourceLocation VILLAGE_BONUS_LOOT = BetterEnd.makeID("chests/end_village_bonus_loot");
    public static final ResourceLocation COMMON = BetterEnd.makeID("chests/common");
    public static final ResourceLocation FOGGY_MUSHROOMLAND = BetterEnd.makeID("chests/foggy_mushroomland");
    public static final ResourceLocation CHORUS_FOREST = BetterEnd.makeID("chests/chorus_forest");
    public static final ResourceLocation SHADOW_FOREST = BetterEnd.makeID("chests/shadow_forest");
    public static final ResourceLocation LANTERN_WOODS = BetterEnd.makeID("chests/lantern_woods");
    public static final ResourceLocation UMBRELLA_JUNGLE = BetterEnd.makeID("chests/umbrella_jungle");
    public static final ResourceLocation BIOME_CHEST = BetterEnd.makeID("chests/biome");
    public static final ResourceLocation FISHING_FISH = BetterEnd.makeID("gameplay/fishing/fish");
    public static final ResourceLocation FISHING_TREASURE = BetterEnd.makeID("gameplay/fishing/treasure");
    public static final ResourceLocation FISHING_JUNK = BetterEnd.makeID("gameplay/fishing/junk");


    public static final LootItemCondition.Builder IN_END
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setDimension(Level.END));
    public static final LootItemCondition.Builder IN_FOGGY_MUSHROOMLAND
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(EndBiomes.FOGGY_MUSHROOMLAND));
    public static final LootItemCondition.Builder IN_CHORUS_FOREST
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(EndBiomes.CHORUS_FOREST));
    public static final LootItemCondition.Builder IN_AMBER_LAND
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(EndBiomes.AMBER_LAND));
    public static final LootItemCondition.Builder IN_GLOWING_GRASSLANDS
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(EndBiomes.GLOWING_GRASSLANDS));
    public static final LootItemCondition.Builder IN_LANTERN_WOODS
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(EndBiomes.LANTERN_WOODS));
    public static final LootItemCondition.Builder IN_MEGALAKE
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(EndBiomes.MEGALAKE));
    public static final LootItemCondition.Builder IN_MEGALAKE_GROVE
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(EndBiomes.MEGALAKE_GROVE));
    public static final LootItemCondition.Builder IN_NEON_OASIS
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(EndBiomes.NEON_OASIS));
    public static final LootItemCondition.Builder IN_SHADOW_FOREST
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(EndBiomes.SHADOW_FOREST));
    public static final LootItemCondition.Builder IN_SULPHUR_SPRINGS
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(EndBiomes.SULPHUR_SPRINGS));
    public static final LootItemCondition.Builder IN_UMBRELLA_JUNGLE
            = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(EndBiomes.UMBRELLA_JUNGLE));

    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(LootTableUtil::onLootTableLoad);
    }

    private static void onLootTableLoad(LootTableLoadEvent event) {
        final ResourceLocation id = event.getName();
        final var table = event.getTable();

        if (BuiltInLootTables.END_CITY_TREASURE.equals(id)) {
            LootPool.Builder builder = LootPool.lootPool();
            builder.setRolls(ConstantValue.exactly(1));
            builder.when(LootItemRandomChanceCondition.randomChance(0.2f));
            builder.add(LootItem.lootTableItem(Items.GHAST_TEAR));
            table.addPool(builder.build());

            builder = LootPool.lootPool();
            builder.setRolls(UniformGenerator.between(0, 3));
            builder.add(LootItem.lootTableItem(EndItems.MUSIC_DISC_STRANGE_AND_ALIEN));
            builder.add(LootItem.lootTableItem(EndItems.MUSIC_DISC_GRASPING_AT_STARS));
            builder.add(LootItem.lootTableItem(EndItems.MUSIC_DISC_ENDSEEKER));
            builder.add(LootItem.lootTableItem(EndItems.MUSIC_DISC_EO_DRACONA));
            table.addPool(builder.build());

            table.addPool(LootPool
                    .lootPool()
                    .setRolls(UniformGenerator.between(2, 4))
                    .add(EmptyLootItem.emptyItem().setWeight(12))
                    .add(LootItem.lootTableItem(EndTemplates.NETHERITE_UPGRADE).setWeight(3))
                    .add(LootItem.lootTableItem(EndTemplates.HANDLE_ATTACHMENT).setWeight(2))
                    .add(LootItem.lootTableItem(EndTemplates.LEATHER_HANDLE_ATTACHMENT).setWeight(1))
                    .add(LootItem.lootTableItem(EndTemplates.TOOL_ASSEMBLY).setWeight(1))
                    .add(LootItem.lootTableItem(EndTemplates.AETERNIUM_UPGRADE).setWeight(1))
                    .add(LootItem.lootTableItem(EndTemplates.THALLASIUM_UPGRADE).setWeight(2))
                    .add(LootItem.lootTableItem(EndTemplates.TERMINITE_UPGRADE).setWeight(2))
                    .build());
        } else if (BuiltInLootTables.FISHING.equals(id)) {
            table.addPool(LootPool.lootPool().when(IN_END).setRolls(ConstantValue.exactly(1.0F))
                                   .add(LootTableReference.lootTableReference(FISHING_FISH)
                                                          .setWeight(85)
                                                          .setQuality(-1))
                                   .add(LootTableReference.lootTableReference(FISHING_TREASURE)
                                                          .setWeight(5)
                                                          .setQuality(2))
                                   .add(LootTableReference.lootTableReference(FISHING_JUNK)
                                                          .setWeight(10)
                                                          .setQuality(-2))
                                   .build());
        }
    }

    public static ResourceLocation getTable(Holder<Biome> biome) {
        BCLBiome bclBiome = BiomeAPI.getBiome(biome.value());
        if (bclBiome != null) {
            if (bclBiome.equals(EndBiomes.FOGGY_MUSHROOMLAND)) {
                return FOGGY_MUSHROOMLAND;
            } else if (bclBiome.equals(EndBiomes.CHORUS_FOREST)) {
                return CHORUS_FOREST;
            } else if (bclBiome.equals(EndBiomes.SHADOW_FOREST)) {
                return SHADOW_FOREST;
            } else if (bclBiome.equals(EndBiomes.LANTERN_WOODS)) {
                return LANTERN_WOODS;
            } else if (bclBiome.equals(EndBiomes.UMBRELLA_JUNGLE)) {
                return UMBRELLA_JUNGLE;
            }
        }
        return COMMON;
    }

}
