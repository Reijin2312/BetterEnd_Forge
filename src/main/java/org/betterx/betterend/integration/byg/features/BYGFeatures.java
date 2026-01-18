package org.betterx.betterend.integration.byg.features;

import org.betterx.bclib.api.v3.levelgen.features.BCLFeature;
import org.betterx.bclib.api.v3.levelgen.features.BCLFeatureBuilder;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.integration.Integrations;
import org.betterx.betterend.integration.byg.BYGBlocks;
import org.betterx.betterend.registry.EndFeatures;
import org.betterx.betterend.world.features.*;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BYGFeatures {
    public static final BCLFeature<OldBulbisTreeFeature, NoneFeatureConfiguration> OLD_BULBIS_TREE = registerVegetation(
            "old_bulbis_tree",
            EndFeatures.inlineBuild("old_bulbis_tree_feature", new OldBulbisTreeFeature()),
            1
    );
    public static final BCLFeature<SinglePlantFeature, SinglePlantFeatureConfig> IVIS_SPROUT = registerVegetation(
            "ivis_sprout",
            EndFeatures.SINGLE_PLANT_FEATURE,
            new SinglePlantFeatureConfig(Integrations.BYG.getBlock("ivis_sprout"), 6, 2),
            6
    );
    public static final BCLFeature<VineFeature, VineFeatureConfig> IVIS_VINE = registerVegetation(
            "ivis_vine",
            EndFeatures.VINE_FEATURE,
            new VineFeatureConfig(BYGBlocks.IVIS_VINE, 24),
            5
    );
    public static final BCLFeature<WallPlantFeature, WallPlantFeatureConfig> IVIS_MOSS = registerVegetation(
            "ivis_moss",
            EndFeatures.WALL_PLANT_FEATURE,
            new WallPlantFeatureConfig(BYGBlocks.IVIS_MOSS, 6),
            1
    );
    public static final BCLFeature<WallPlantOnLogFeature, WallPlantFeatureConfig> IVIS_MOSS_WOOD = registerVegetation(
            "ivis_moss_wood",
            EndFeatures.WALL_PLANT_ON_LOG_FEATURE,
            new WallPlantFeatureConfig(BYGBlocks.IVIS_MOSS, 6),
            15
    );
    public static final BCLFeature<WallPlantFeature, WallPlantFeatureConfig> NIGHTSHADE_MOSS = registerVegetation(
            "nightshade_moss",
            EndFeatures.WALL_PLANT_FEATURE,
            new WallPlantFeatureConfig(BYGBlocks.NIGHTSHADE_MOSS, 5),
            2
    );
    public static final BCLFeature<WallPlantOnLogFeature, WallPlantFeatureConfig> NIGHTSHADE_MOSS_WOOD = registerVegetation(
            "nightshade_moss_wood",
            EndFeatures.WALL_PLANT_ON_LOG_FEATURE,
            new WallPlantFeatureConfig(BYGBlocks.NIGHTSHADE_MOSS, 5),
            8
    );

    public static final NightshadeRedwoodTreeFeature NIGHTSHADE_REDWOOD_TREE_FEATURE =
            EndFeatures.inlineBuild(
                    "nightshade_redwood_tree",
                    new NightshadeRedwoodTreeFeature()
            );

    public static final BCLFeature<NightshadeRedwoodTreeFeature, NoneFeatureConfiguration> NIGHTSHADE_REDWOOD_TREE = registerVegetation(
            "nightshade_redwood_tree",
            NIGHTSHADE_REDWOOD_TREE_FEATURE,
            1
    );
    public static final BigEtherTreeFeature BIG_ETHER_TREE_FEATURE =
            EndFeatures.inlineBuild(
                    "big_ether_tree",
                    new BigEtherTreeFeature()
            );

    public static final BCLFeature<BigEtherTreeFeature, NoneFeatureConfiguration> BIG_ETHER_TREE = registerVegetation(
            "big_ether_tree",
            BIG_ETHER_TREE_FEATURE,
            1
    );

    public static void register() {
    }

    private static <F extends Feature<NoneFeatureConfiguration>> BCLFeature<F, NoneFeatureConfiguration> registerVegetation(
            String name,
            F feature,
            int density
    ) {
        return registerVegetation(name, feature, NoneFeatureConfiguration.NONE, density);
    }

    private static <F extends Feature<FC>, FC extends FeatureConfiguration> BCLFeature<F, FC> registerVegetation(
            String name,
            F feature,
            FC config,
            int density
    ) {
        ResourceLocation id = BetterEnd.makeID(name);
        return BCLFeatureBuilder
                .start(id, feature)
                .configuration(config)
                .build()
                .place()
                .countMax(density)
                .squarePlacement()
                .heightmap()
                .onlyInBiome()
                .build();
    }
}
