package org.betterx.betterend.blocks;

import org.betterx.bclib.behaviours.BehaviourBuilders;
import org.betterx.bclib.behaviours.interfaces.BehaviourPlant;
import org.betterx.bclib.interfaces.SurvivesOnBlocks;
import org.betterx.betterend.blocks.basis.EndPlantBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import java.util.List;

public class TerrainPlantBlock extends EndPlantBlock implements SurvivesOnBlocks, BehaviourPlant {
    private final List<Block> ground;

    public TerrainPlantBlock(Block... ground) {
        super(BehaviourBuilders.createPlant(ground.length == 0 ? MapColor.PLANT : ground[0].defaultMapColor())
                               .ignitedByLava()
                               .offsetType(OffsetType.XZ)
                               .replaceable());
        this.ground = List.of(ground);
    }

    @Override
    public List<Block> getSurvivableBlocks() {
        return ground;
    }

    @Override
    public boolean isTerrain(BlockState state) {
        return SurvivesOnBlocks.super.isTerrain(state);
    }
}
