package org.betterx.betterend.blocks;

import org.betterx.betterend.blocks.basis.PottableFeatureSapling;
import org.betterx.betterend.interfaces.survives.SurvivesOnMossOrDust;
import org.betterx.betterend.registry.EndFeatures;
import org.betterx.betterend.world.features.trees.LacugroveFeature;

import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class LacugroveSaplingBlock extends PottableFeatureSapling<LacugroveFeature, NoneFeatureConfiguration> implements SurvivesOnMossOrDust {
    public LacugroveSaplingBlock() {
        super((state) -> EndFeatures.LACUGROVE.configuredFeature);
    }
}
