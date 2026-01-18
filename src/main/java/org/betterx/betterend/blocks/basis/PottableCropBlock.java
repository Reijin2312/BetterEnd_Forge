package org.betterx.betterend.blocks.basis;

import org.betterx.bclib.behaviours.interfaces.BehaviourSeed;
import org.betterx.bclib.blocks.BaseCropBlock;
import org.betterx.bclib.interfaces.SurvivesOnBlocks;
import org.betterx.betterend.interfaces.PottablePlant;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class PottableCropBlock extends BaseCropBlock implements PottablePlant, BehaviourSeed, SurvivesOnBlocks {
    private final List<Block> terrain;

    public PottableCropBlock(Item drop, Block... terrain) {
        super(drop, terrain);
        this.terrain = List.of(terrain);
    }

    @Override
    public String getPottedState() {
        return "age=3";
    }

    @Override
    public List<Block> getSurvivableBlocks() {
        return terrain;
    }

    @Override
    public boolean canPlantOn(Block block) {
        return isSurvivable(block.defaultBlockState());
    }
}
