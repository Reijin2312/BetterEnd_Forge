package org.betterx.datagen.betterend;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeRegistry;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeData;
import org.betterx.bclib.api.v3.datagen.RegistrySupplier;
import org.betterx.betterend.BetterEnd;
import org.betterx.datagen.betterend.worldgen.*;
import org.betterx.worlds.together.surfaceRules.AssignedSurfaceRule;
import org.betterx.worlds.together.surfaceRules.SurfaceRuleRegistry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public class EndRegistrySupplier extends RegistrySupplier {
    public static final EndRegistrySupplier INSTANCE = new EndRegistrySupplier();

    protected EndRegistrySupplier() {
        super(List.of(BetterEnd.MOD_ID));
    }

    @Override
    protected List<RegistryInfo<?>> initializeRegistryList(@Nullable List<String> modIDs) {
        return List.of(
                new RegistryInfo<>(
                        SurfaceRuleRegistry.SURFACE_RULES_REGISTRY,
                        AssignedSurfaceRule.CODEC,
                        SurfaceRuleRegistry::bootstrap
                ),
                new RegistryInfo<>(
                        Registries.STRUCTURE,
                        Structure.DIRECT_CODEC, StructureDataProvider::bootstrap
                ),
                new RegistryInfo<>(
                        Registries.STRUCTURE_SET,
                        StructureSet.DIRECT_CODEC,
                        StructureDataProvider::bootstrapSets
                ),
                new RegistryInfo<>(
                        Registries.CONFIGURED_FEATURE,
                        ConfiguredFeature.DIRECT_CODEC,
                        ConfiguredFeatureDataProvider::bootstrap
                ),
                new RegistryInfo<>(
                        Registries.PROCESSOR_LIST,
                        StructureProcessorType.DIRECT_CODEC,
                        ProcessorsDataProvider::bootstrap
                ),
                new RegistryInfo<>(
                        Registries.PLACED_FEATURE,
                        PlacedFeature.DIRECT_CODEC,
                        PlacedFeatureDataProvider::bootstrap
                ),
                new RegistryInfo<>(
                        Registries.TEMPLATE_POOL,
                        StructureTemplatePool.DIRECT_CODEC,
                        TemplatePoolDataProvider::bootstrap
                ),
                new RegistryInfo<>(
                        Registries.BIOME,
                        Biome.DIRECT_CODEC,
                        EndBiomesDataProvider::bootstrap
                ),
                new RegistryInfo<>(
                        BCLBiomeRegistry.BCL_BIOMES_REGISTRY,
                        BiomeData.CODEC,
                        EndBiomesDataProvider::bootstrapBCL
                )
        );
    }
}
