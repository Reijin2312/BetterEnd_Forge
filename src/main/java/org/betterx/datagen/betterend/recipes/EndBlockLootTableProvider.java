package org.betterx.datagen.betterend.recipes;

import org.betterx.bclib.api.v3.datagen.LootDropProvider;
import org.betterx.bclib.registry.BaseRegistry;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class EndBlockLootTableProvider extends LootTableProvider {
    public EndBlockLootTableProvider(PackOutput output) {
        super(
                output,
                Set.of(),
                List.of(new SubProviderEntry(EndBlockLootSubProvider::new, LootContextParamSets.BLOCK))
        );
    }

    private static final class EndBlockLootSubProvider implements LootTableSubProvider {
        @Override
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
            // Ensure BetterEnd blocks are registered before collecting loot tables
            EndBlocks.ensureStaticallyLoaded();

            for (Block block : BaseRegistry.getModBlocks(BetterEnd.MOD_ID)) {
                if (block instanceof LootDropProvider dropper) {
                    final ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                    if (id != null && id != BuiltInRegistries.BLOCK.getDefaultKey()) {
                        LootTable.Builder builder = LootTable.lootTable();
                        dropper.getDroppedItemsBCL(builder);
                        biConsumer.accept(id.withPrefix("blocks/"), builder);
                    }
                }
            }
        }
    }
}
