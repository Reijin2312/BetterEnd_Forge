package org.betterx.datagen.betterend.worldgen;

import org.betterx.bclib.api.v3.levelgen.features.BCLPlacedFeatureBuilder;
import org.betterx.betterend.world.structures.village.VillagePools;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.EndFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class PlacedFeatureDataProvider {
    public static void bootstrap(BootstapContext<PlacedFeature> ctx) {
        BCLPlacedFeatureBuilder.registerUnbound(ctx);

        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = ctx.lookup(Registries.CONFIGURED_FEATURE);
        Holder.Reference<ConfiguredFeature<?, ?>> chorusFeature = configuredFeatures.getOrThrow(EndFeatures.CHORUS_PLANT);

        PlacementUtils.register(
                ctx,
                VillagePools.CHORUS_VILLAGE,
                chorusFeature,
                PlacementUtils.filteredByBlockSurvival(Blocks.CHORUS_PLANT)
        );
    }
}
