package org.betterx.betterend.blocks;

import org.betterx.betterend.blocks.basis.PottableFeatureSapling;
import org.betterx.betterend.interfaces.survives.SurvivesOnShadowGrass;
import org.betterx.betterend.registry.EndFeatures;
import org.betterx.betterend.world.features.trees.DragonTreeFeature;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class DragonTreeSaplingBlock extends PottableFeatureSapling<DragonTreeFeature, NoneFeatureConfiguration> implements SurvivesOnShadowGrass {
    public DragonTreeSaplingBlock() {
        super((state) -> EndFeatures.DRAGON_TREE.configuredFeature);
    }

    @Override
    public boolean canPlantOn(Block block) {
        return isSurvivable(block.defaultBlockState());
    }
}
