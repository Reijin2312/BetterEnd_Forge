package org.betterx.betterend.blocks;

import org.betterx.betterend.blocks.basis.PottableFeatureSapling;
import org.betterx.betterend.interfaces.survives.SurvivesOnChorusNylium;
import org.betterx.betterend.registry.EndFeatures;
import org.betterx.betterend.world.features.trees.PythadendronTreeFeature;

import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class PythadendronSaplingBlock extends PottableFeatureSapling<PythadendronTreeFeature, NoneFeatureConfiguration> implements SurvivesOnChorusNylium {
    public PythadendronSaplingBlock() {
        super((state) -> EndFeatures.PYTHADENDRON_TREE.configuredFeature);
    }
}
