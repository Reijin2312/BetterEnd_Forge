package org.betterx.betterend.blocks;

import org.betterx.bclib.behaviours.BehaviourBuilders;
import org.betterx.bclib.behaviours.interfaces.BehaviourPlant;
import org.betterx.betterend.blocks.basis.EndPlantBlock;
import org.betterx.betterend.interfaces.survives.SurvivesOnChorusNylium;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class ChorusGrassBlock extends EndPlantBlock implements SurvivesOnChorusNylium, BehaviourPlant {
    public ChorusGrassBlock() {
        super(BehaviourBuilders.createGrass(MapColor.COLOR_PURPLE).replaceable());
    }

    @Override
    public boolean isTerrain(BlockState state) {
        return SurvivesOnChorusNylium.super.isTerrain(state);
    }
}
