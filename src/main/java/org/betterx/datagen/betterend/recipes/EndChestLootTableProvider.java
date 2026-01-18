package org.betterx.datagen.betterend.recipes;

import org.betterx.bclib.complexmaterials.WoodenComplexMaterial;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndItems;
import org.betterx.betterend.registry.EndTemplates;
import org.betterx.betterend.util.LootTableUtil;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class EndChestLootTableProvider extends LootTableProvider {
    public EndChestLootTableProvider(PackOutput output) {
        super(
                output,
                Set.of(),
                List.of(new SubProviderEntry(EndChestLootSubProvider::new, LootContextParamSets.CHEST))
        );
    }

    private static class EndChestLootSubProvider implements LootTableSubProvider {
        private final CompoundTag fireworkRocketTag = new CompoundTag();

        private EndChestLootSubProvider() {
            CompoundTag fireworksTag = new CompoundTag();
            fireworkRocketTag.put("Fireworks", fireworksTag);
            fireworksTag.putByte("Flight", (byte) 3);
        }

        @Override
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
            biConsumer.accept(
                    LootTableUtil.VILLAGE_LOOT,
                    LootTable.lootTable()
                             .withPool(simpleVillageLoot())
                             .withPool(plateUpgradeLootPool())
            );

            biConsumer.accept(
                    LootTableUtil.VILLAGE_TEMPLATE_LOOT,
                    LootTable.lootTable()
                             .withPool(simpleVillageLoot())
                             .withPool(handleLootPool())
                             .withPool(leatherHandleLootPool())
                             .withPool(plateUpgradeLootPool())
                             .withPool(upgradeLootPool())
            );

            biConsumer.accept(
                    LootTableUtil.VILLAGE_BONUS_LOOT,
                    LootTable.lootTable()
                             .withPool(leatherHandleLootPool())
                             .withPool(plateUpgradeLootPool())
                             .withPool(villageBonusLoot())
                             .withPool(elytraLoot(0.2f))
            );

            biConsumer.accept(
                    LootTableUtil.FISHING_FISH,
                    LootTable.lootTable()
                             .withPool(fishing())
            );

            biConsumer.accept(
                    LootTableUtil.FISHING_JUNK,
                    LootTable.lootTable()
                             .withPool(fishingJunk())
            );

            biConsumer.accept(
                    LootTableUtil.FISHING_TREASURE,
                    LootTable.lootTable()
                             .withPool(fishingTreasure())
            );

            biConsumer.accept(
                    LootTableUtil.FOGGY_MUSHROOMLAND,
                    includeCommonItems(LootTable.lootTable())
                            .withPool(foggyMushroomland())
            );

            biConsumer.accept(
                    LootTableUtil.CHORUS_FOREST,
                    includeCommonItems(LootTable.lootTable())
                            .withPool(chorusForest())
            );

            biConsumer.accept(
                    LootTableUtil.SHADOW_FOREST,
                    includeCommonItems(LootTable.lootTable())
                            .withPool(shadowForest())
            );

            biConsumer.accept(
                    LootTableUtil.LANTERN_WOODS,
                    includeCommonItems(LootTable.lootTable())
                            .withPool(lanternWoods())
            );

            biConsumer.accept(
                    LootTableUtil.UMBRELLA_JUNGLE,
                    includeCommonItems(LootTable.lootTable())
                            .withPool(umbrellaJungle())
            );

            biConsumer.accept(
                    LootTableUtil.BIOME_CHEST,
                    includeCommonItems(LootTable.lootTable())
                            .withPool(umbrellaJungle().when(LootTableUtil.IN_UMBRELLA_JUNGLE))
                            .withPool(lanternWoods().when(LootTableUtil.IN_LANTERN_WOODS))
                            .withPool(shadowForest().when(LootTableUtil.IN_SHADOW_FOREST))
                            .withPool(chorusForest().when(LootTableUtil.IN_CHORUS_FOREST))
                            .withPool(foggyMushroomland().when(LootTableUtil.IN_FOGGY_MUSHROOMLAND))
            );

            biConsumer.accept(
                    LootTableUtil.COMMON,
                    includeCommonItems(LootTable.lootTable())
            );
        }

        private LootPool.Builder fishing() {
            return LootPool
                    .lootPool()
                    .setRolls(UniformGenerator.between(1, 2))
                    .add(LootItem.lootTableItem(EndItems.END_FISH_RAW));
        }

        private LootPool.Builder fishingJunk() {
            final LootPool.Builder builder = LootPool
                    .lootPool()
                    .setRolls(UniformGenerator.between(1, 2))
                    .add(LootItem.lootTableItem(EndItems.END_LILY_LEAF))
                    .add(LootItem.lootTableItem(Items.ENDER_PEARL))
                    .add(LootItem.lootTableItem(Items.CHORUS_FRUIT))
                    .add(LootItem.lootTableItem(EndItems.GELATINE))
                    .add(LootItem.lootTableItem(EndItems.CRYSTAL_SHARDS))
                    .add(LootItem.lootTableItem(EndItems.HYDRALUX_PETAL).when(LootTableUtil.IN_SULPHUR_SPRINGS));
            addCharnia(builder);
            return builder;
        }

        private LootPool.Builder fishingTreasure() {
            return LootPool
                    .lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(EndBlocks.TERMINITE.swordBlade))
                    .add(LootItem.lootTableItem(EndBlocks.TERMINITE.forgedPlate))
                    .add(LootItem.lootTableItem(EndBlocks.MENGER_SPONGE))
                    .add(LootItem.lootTableItem(Items.BOW)
                                 .apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0F, 0.25F)))
                                 .apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30.0F))
                                                                 .allowTreasure()))
                    .add(LootItem.lootTableItem(Items.FISHING_ROD)
                                 .apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0F, 0.25F)))
                                 .apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30.0F))
                                                                 .allowTreasure()))
                    .add(LootItem.lootTableItem(Items.BOOK)
                                 .apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30.0F))
                                                                 .allowTreasure()));
        }

        private LootPool.Builder foggyMushroomland() {
            return LootPool
                    .lootPool()
                    .setRolls(UniformGenerator.between(4, 8))
                    .add(LootItem.lootTableItem(EndBlocks.MOSSY_GLOWSHROOM.getBlock(WoodenComplexMaterial.BLOCK_PLANKS)))
                    .add(LootItem.lootTableItem(EndBlocks.MOSSY_GLOWSHROOM_SAPLING))
                    .add(LootItem.lootTableItem(EndBlocks.BLUE_VINE_SEED));
        }

        private LootPool.Builder chorusForest() {
            return LootPool
                    .lootPool()
                    .setRolls(UniformGenerator.between(4, 8))
                    .add(LootItem.lootTableItem(EndBlocks.PYTHADENDRON.getBlock(WoodenComplexMaterial.BLOCK_PLANKS)))
                    .add(LootItem.lootTableItem(EndBlocks.PYTHADENDRON_SAPLING))
                    .add(LootItem.lootTableItem(EndBlocks.CHORUS_MUSHROOM))
                    .add(LootItem.lootTableItem(EndTemplates.HANDLE_ATTACHMENT));
        }

        private LootPool.Builder shadowForest() {
            return LootPool
                    .lootPool()
                    .setRolls(UniformGenerator.between(4, 8))
                    .add(LootItem.lootTableItem(EndBlocks.DRAGON_TREE.getBlock(WoodenComplexMaterial.BLOCK_PLANKS)))
                    .add(LootItem.lootTableItem(EndBlocks.DRAGON_TREE_SAPLING))
                    .add(LootItem.lootTableItem(EndBlocks.SHADOW_BERRY))
                    .add(LootItem.lootTableItem(EndItems.SHADOW_BERRY_RAW));
        }

        private LootPool.Builder lanternWoods() {
            return LootPool
                    .lootPool()
                    .setRolls(UniformGenerator.between(4, 8))
                    .add(LootItem.lootTableItem(EndBlocks.LUCERNIA.getBlock(WoodenComplexMaterial.BLOCK_PLANKS)))
                    .add(LootItem.lootTableItem(EndBlocks.LUCERNIA_SAPLING))
                    .add(LootItem.lootTableItem(EndBlocks.BOLUX_MUSHROOM));
        }

        private LootPool.Builder umbrellaJungle() {
            return LootPool
                    .lootPool()
                    .setRolls(UniformGenerator.between(4, 8))
                    .add(LootItem.lootTableItem(EndBlocks.UMBRELLA_TREE.getBlock(WoodenComplexMaterial.BLOCK_PLANKS)))
                    .add(LootItem.lootTableItem(EndBlocks.UMBRELLA_TREE_SAPLING))
                    .add(LootItem.lootTableItem(EndBlocks.SMALL_JELLYSHROOM));
        }

        private LootPool.Builder upgradeLootPool() {
            return LootPool
                    .lootPool()
                    .setRolls(UniformGenerator.between(1, 2))
                    .add(LootItem
                            .lootTableItem(EndTemplates.THALLASIUM_UPGRADE)
                            .setWeight(8)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                    )
                    .add(LootItem
                            .lootTableItem(EndTemplates.TERMINITE_UPGRADE)
                            .setWeight(4)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                    )
                    .add(LootItem
                            .lootTableItem(EndTemplates.AETERNIUM_UPGRADE)
                            .setWeight(2)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                    )
                    .add(LootItem
                            .lootTableItem(EndTemplates.NETHERITE_UPGRADE)
                            .setWeight(8)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                    );
        }

        private LootPool.Builder plateUpgradeLootPool() {
            return LootPool
                    .lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem
                            .lootTableItem(EndTemplates.PLATE_UPGRADE)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                            .when(LootItemRandomChanceCondition.randomChance(0.70F))
                    );
        }

        private LootPool.Builder leatherHandleLootPool() {
            return LootPool
                    .lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem
                            .lootTableItem(EndTemplates.LEATHER_HANDLE_ATTACHMENT)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                            .when(LootItemRandomChanceCondition.randomChance(0.75F))
                    );
        }

        private LootPool.Builder handleLootPool() {
            return LootPool
                    .lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem
                            .lootTableItem(EndTemplates.HANDLE_ATTACHMENT)
                            .setWeight(8)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
                    )
                    .add(LootItem
                            .lootTableItem(EndTemplates.TOOL_ASSEMBLY)
                            .setWeight(2)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                    );
        }

        private LootPool.Builder villageBonusLoot() {
            return LootPool
                    .lootPool()
                    .setRolls(UniformGenerator.between(1, 2))
                    .add(LootItem
                            .lootTableItem(EndItems.AETERNIUM_INGOT)
                            .setWeight(8)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
                    )
                    .add(LootItem
                            .lootTableItem(EndBlocks.FLAVOLITE_RUNED_ETERNAL)
                            .setWeight(4)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                    )
                    .add(LootItem
                            .lootTableItem(EndItems.ETERNAL_CRYSTAL)
                            .setWeight(1)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                    )
                    .add(LootItem
                            .lootTableItem(Items.SHULKER_SHELL)
                            .setWeight(1)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
                    );
        }

        private LootPool.Builder elytraLoot(float chance) {
            return LootPool
                    .lootPool()
                    .setRolls(UniformGenerator.between(1, 3))
                    .add(LootItem
                            .lootTableItem(Items.ELYTRA)
                            .setWeight(1)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                    ).add(LootItem
                            .lootTableItem(Items.FIREWORK_ROCKET)
                            .setWeight(8)
                            .apply(SetNbtFunction.setTag(fireworkRocketTag))
                            .apply(SetItemCountFunction
                                    .setCount(UniformGenerator.between(4, 8)))
                    )
                    .when(LootItemRandomChanceCondition.randomChance(chance));
        }

        private LootPool.Builder simpleVillageLoot() {
            return LootPool
                    .lootPool()
                    .setRolls(UniformGenerator.between(1, 3))
                    .add(LootItem
                            .lootTableItem(EndItems.LEATHER_WRAPPED_STICK)
                            .setWeight(10)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))
                    )
                    .add(LootItem
                            .lootTableItem(Items.ENDER_PEARL)
                            .setWeight(9)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5)))
                    )
                    .add(LootItem
                            .lootTableItem(EndItems.SHADOW_BERRY_COOKED)
                            .setWeight(8)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
                    )
                    .add(LootItem
                            .lootTableItem(EndItems.BLOSSOM_BERRY_JELLY)
                            .setWeight(4)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
                    )
                    .add(LootItem
                            .lootTableItem(EndBlocks.THALLASIUM.shovelHead)
                            .setWeight(2)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                    )
                    .add(LootItem
                            .lootTableItem(Blocks.ENDER_CHEST)
                            .setWeight(1)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                    );
        }

        private static void addCharnia(LootPool.Builder pool) {
            pool.add(LootItem.lootTableItem(EndBlocks.CHARNIA_CYAN)
                             .when(LootTableUtil.IN_GLOWING_GRASSLANDS.or(LootTableUtil.IN_MEGALAKE)
                                                                      .or(LootTableUtil.IN_MEGALAKE_GROVE)
                                                                      .or(
                                                                              LootTableUtil.IN_NEON_OASIS)));
            pool.add(LootItem.lootTableItem(EndBlocks.CHARNIA_LIGHT_BLUE)
                             .when(LootTableUtil.IN_FOGGY_MUSHROOMLAND.or(LootTableUtil.IN_GLOWING_GRASSLANDS)
                                                                      .or(LootTableUtil.IN_MEGALAKE)
                                                                      .or(LootTableUtil.IN_MEGALAKE_GROVE)
                                                                      .or(LootTableUtil.IN_UMBRELLA_JUNGLE)));
            pool.add(LootItem.lootTableItem(EndBlocks.CHARNIA_GREEN)
                             .when(LootTableUtil.IN_GLOWING_GRASSLANDS.or(LootTableUtil.IN_NEON_OASIS)
                                                                      .or(LootTableUtil.IN_SULPHUR_SPRINGS)
                                                                      .or(
                                                                              LootTableUtil.IN_UMBRELLA_JUNGLE)));
            pool.add(LootItem.lootTableItem(EndBlocks.CHARNIA_RED)
                             .when(LootTableUtil.IN_AMBER_LAND.or(LootTableUtil.IN_LANTERN_WOODS)
                                                              .or(LootTableUtil.IN_NEON_OASIS)));
            pool.add(LootItem.lootTableItem(EndBlocks.CHARNIA_ORANGE)
                             .when(LootTableUtil.IN_AMBER_LAND.or(LootTableUtil.IN_LANTERN_WOODS)
                                                              .or(LootTableUtil.IN_SULPHUR_SPRINGS)));
            pool.add(LootItem.lootTableItem(EndBlocks.CHARNIA_PURPLE)
                             .when(LootTableUtil.IN_CHORUS_FOREST.or(LootTableUtil.IN_SHADOW_FOREST)));
        }

        private static LootTable.Builder includeCommonItems(LootTable.Builder table) {
            LootPool.Builder builder = LootPool.lootPool();
            builder.setRolls(UniformGenerator.between(0, 2));
            builder.add(LootItem.lootTableItem(EndItems.MUSIC_DISC_STRANGE_AND_ALIEN));
            builder.add(LootItem.lootTableItem(EndItems.MUSIC_DISC_GRASPING_AT_STARS));
            builder.add(LootItem.lootTableItem(EndItems.MUSIC_DISC_ENDSEEKER));
            builder.add(LootItem.lootTableItem(EndItems.MUSIC_DISC_EO_DRACONA));
            table.withPool(builder);

            builder = LootPool.lootPool();
            builder.setRolls(UniformGenerator.between(4, 8));
            builder.add(LootItem.lootTableItem(EndBlocks.THALLASIUM.ingot));
            builder.add(LootItem.lootTableItem(EndBlocks.THALLASIUM.rawOre));
            builder.add(LootItem.lootTableItem(Items.ENDER_PEARL));
            table.withPool(builder);

            builder = LootPool.lootPool();
            builder.setRolls(UniformGenerator.between(2, 4));
            builder.add(LootItem.lootTableItem(EndBlocks.TERMINITE.ingot));
            builder.add(LootItem.lootTableItem(EndItems.ENDER_SHARD));
            builder.add(LootItem.lootTableItem(EndBlocks.AURORA_CRYSTAL));
            builder.add(LootItem.lootTableItem(EndBlocks.THALLASIUM.axe));
            builder.add(LootItem.lootTableItem(EndBlocks.THALLASIUM.pickaxe));
            builder.add(LootItem.lootTableItem(EndBlocks.THALLASIUM.hoe));
            builder.add(LootItem.lootTableItem(EndBlocks.THALLASIUM.sword));
            builder.add(LootItem.lootTableItem(EndBlocks.THALLASIUM.shovel));
            builder.add(LootItem.lootTableItem(Items.ENDER_EYE));
            builder.add(LootItem.lootTableItem(Blocks.OBSIDIAN));
            table.withPool(builder);

            builder = LootPool.lootPool();
            builder.setRolls(UniformGenerator.between(0, 4));
            builder.add(LootItem.lootTableItem(EndBlocks.FLAVOLITE_RUNED));
            builder.add(LootItem.lootTableItem(EndItems.AETERNIUM_INGOT));
            builder.add(LootItem.lootTableItem(EndItems.AMBER_GEM));
            builder.add(LootItem.lootTableItem(Items.END_CRYSTAL));
            builder.add(LootItem.lootTableItem(Items.GHAST_TEAR));
            table.withPool(builder);

            return table;
        }
    }
}
