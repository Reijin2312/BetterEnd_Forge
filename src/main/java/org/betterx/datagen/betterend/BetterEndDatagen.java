package org.betterx.datagen.betterend;

import org.betterx.datagen.betterend.advancement.EndAdvancementDataProvider;
import org.betterx.datagen.betterend.recipes.EndBlockLootTableProvider;
import org.betterx.datagen.betterend.recipes.EndChestLootTableProvider;
import org.betterx.datagen.betterend.recipes.EndRecipeDataProvider;
import org.betterx.datagen.betterend.worldgen.EndBiomesDataProvider;
import org.betterx.datagen.betterend.worldgen.EndRegistriesDataProvider;
import org.betterx.datagen.betterend.worldgen.TemplatePoolDataProvider;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class BetterEndDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {

        EndBiomesDataProvider.ensureStaticallyLoaded();
        EndRecipeDataProvider.buildRecipes();
        TemplatePoolDataProvider.buildStructures();

        final FabricDataGenerator.Pack pack = dataGenerator.createPack();
        pack.addProvider(EndBiomesDataProvider::new);

        pack.addProvider(EndRecipeDataProvider::new);
        pack.addProvider(EndRegistriesDataProvider::new);
        pack.addProvider(EndAdvancementDataProvider::new);
        pack.addProvider(EndBlockTagDataProvider::new);
        pack.addProvider(EndItemTagDataProvider::new);
        pack.addProvider(EndChestLootTableProvider::new);
        pack.addProvider(EndBlockLootTableProvider::new);
    }


    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        EndBiomesDataProvider.ensureStaticallyLoaded();
        EndRegistrySupplier.INSTANCE.bootstrapRegistries(registryBuilder);
        registryBuilder.add(Registries.BIOME, EndBiomesDataProvider::bootstrap);
    }
}
