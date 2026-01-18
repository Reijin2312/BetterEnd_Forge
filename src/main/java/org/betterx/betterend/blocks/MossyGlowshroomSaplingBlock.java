package org.betterx.betterend.blocks;

import org.betterx.betterend.blocks.basis.PottableFeatureSapling;
import org.betterx.betterend.interfaces.survives.SurvivesOnMossOrMycelium;
import org.betterx.betterend.registry.EndFeatures;
import org.betterx.betterend.world.features.trees.MossyGlowshroomFeature;

import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class MossyGlowshroomSaplingBlock extends PottableFeatureSapling<MossyGlowshroomFeature, NoneFeatureConfiguration> implements SurvivesOnMossOrMycelium {
    public MossyGlowshroomSaplingBlock() {
        super(7, (state) -> EndFeatures.MOSSY_GLOWSHROOM.configuredFeature);
    }
}
