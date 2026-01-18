package org.betterx.betterend.blocks;

import org.betterx.bclib.client.render.BCLRenderLayer;
import org.betterx.betterend.blocks.basis.PottableFeatureSapling;
import org.betterx.betterend.interfaces.survives.SurvivesOnJungleMoss;
import org.betterx.betterend.registry.EndFeatures;
import org.betterx.betterend.world.features.trees.UmbrellaTreeFeature;

import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class UmbrellaTreeSaplingBlock extends PottableFeatureSapling<UmbrellaTreeFeature, NoneFeatureConfiguration> implements SurvivesOnJungleMoss {
    public UmbrellaTreeSaplingBlock() {
        super((state) -> EndFeatures.UMBRELLA_TREE.configuredFeature);
    }

    @Override
    public BCLRenderLayer getRenderLayer() {
        return BCLRenderLayer.TRANSLUCENT;
    }
}
