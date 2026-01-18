package org.betterx.datagen.betterend;

import org.betterx.bclib.api.v3.levelgen.features.blockpredicates.BlockPredicates;
import org.betterx.bclib.api.v3.levelgen.features.placement.PlacementModifiers;
import org.betterx.bclib.api.v2.PostInitAPI;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.advancements.BECriteria;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndFeatures;
import org.betterx.betterend.registry.EndItems;
import org.betterx.betterend.registry.EndNumericProviders;
import org.betterx.betterend.registry.EndStructures;
import org.betterx.betterend.registry.EndTemplates;
import org.betterx.datagen.betterend.advancement.EndAdvancementDataProvider;
import org.betterx.datagen.betterend.recipes.EndBlockLootTableProvider;
import org.betterx.datagen.betterend.recipes.EndChestLootTableProvider;
import org.betterx.datagen.betterend.recipes.EndRecipeDataProvider;
import org.betterx.datagen.betterend.worldgen.EndBiomesDataProvider;
import org.betterx.worlds.together.tag.v3.TagManager;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;

import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = BetterEnd.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterEndDatagen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        TagManager.ensureStaticallyLoaded();
        EndNumericProviders.register();
        EndRecipeDataProvider.buildRecipes();
        EndFeatures.register();
        EndItems.getItemRegistry();
        EndBlocks.getBlockRegistry();
        PostInitAPI.postInit(false);
        EndTemplates.ensureStaticallyLoaded();
        EndStructures.register();
        BECriteria.register();
        EndBiomesDataProvider.ensureStaticallyLoaded();
        PlacementModifiers.ensureStaticInitialization();
        BlockPredicates.ensureStaticInitialization();
        BetterEnd.LOGGER.info(
                "[datagen] init complete; includeServer={}, includeClient={}",
                event.includeServer(),
                event.includeClient()
        );

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
            RegistrySetBuilder registryBuilder = new RegistrySetBuilder();
            EndRegistrySupplier.INSTANCE.bootstrapRegistries(registryBuilder);

            DatapackBuiltinEntriesProvider datapackProvider = new DatapackBuiltinEntriesProvider(
                    output,
                    event.getLookupProvider(),
                    registryBuilder,
                    Set.of(BetterEnd.MOD_ID)
            );
            CompletableFuture<HolderLookup.Provider> registryProvider = datapackProvider.getRegistryProvider();

            generator.addProvider(true, datapackProvider);
            BetterEnd.LOGGER.info("[datagen] added registry builder for {}", BetterEnd.MOD_ID);

            generator.addProvider(true, new EndBiomesDataProvider(output, registryProvider, existingFileHelper));
            generator.addProvider(true, new EndRecipeDataProvider(output));
            generator.addProvider(
                    true,
                    new ForgeAdvancementProvider(
                            output,
                            registryProvider,
                            existingFileHelper,
                            List.of(new EndAdvancementDataProvider())
                    )
            );
            generator.addProvider(true, new EndBlockTagDataProvider(output, registryProvider, existingFileHelper));
            generator.addProvider(true, new EndItemTagDataProvider(output, registryProvider, existingFileHelper));
            generator.addProvider(
                    true,
                    new NamedDataProvider(
                            new EndChestLootTableProvider(output),
                            "BetterEnd Loot Tables - Chests"
                    )
            );
            generator.addProvider(
                    true,
                    new NamedDataProvider(
                            new EndBlockLootTableProvider(output),
                            "BetterEnd Loot Tables - Blocks"
                    )
            );
        }
    }

    // DataGenerator uses provider names for deduping; wrap to avoid LootTableProvider name collisions.
    private static final class NamedDataProvider implements DataProvider {
        private final DataProvider delegate;
        private final String name;

        private NamedDataProvider(DataProvider delegate, String name) {
            this.delegate = delegate;
            this.name = name;
        }

        @Override
        public CompletableFuture<?> run(CachedOutput cachedOutput) {
            return delegate.run(cachedOutput);
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
