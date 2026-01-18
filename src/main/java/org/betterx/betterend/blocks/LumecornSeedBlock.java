package org.betterx.betterend.blocks;

import org.betterx.bclib.behaviours.BehaviourBuilders;
import org.betterx.bclib.behaviours.interfaces.BehaviourSeed;
import org.betterx.betterend.blocks.basis.EndPlantWithAgeBlock;
import org.betterx.betterend.interfaces.survives.SurvivesOnEndMoss;
import org.betterx.betterend.registry.EndFeatures;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.MapColor;

import java.util.Optional;

public class LumecornSeedBlock extends EndPlantWithAgeBlock implements BehaviourSeed, SurvivesOnEndMoss {

    public LumecornSeedBlock() {
        super(BehaviourBuilders.createSeed(MapColor.COLOR_LIGHT_BLUE));
    }

    @Override
    public void growAdult(WorldGenLevel world, RandomSource random, BlockPos pos) {
        ((Feature<NoneFeatureConfiguration>) (EndFeatures.LUMECORN.getFeature())).place(new FeaturePlaceContext<>(
                Optional.empty(),
                world,
                null,
                random,
                pos,
                null
        ));
    }

    @Override
    public boolean isTerrain(BlockState state) {
        return SurvivesOnEndMoss.super.isTerrain(state);
    }
}
