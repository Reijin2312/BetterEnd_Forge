package org.betterx.betterend.interfaces.survives;

import org.betterx.bclib.interfaces.SurvivesOnBlocks;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.world.level.block.Block;

import java.util.List;

public interface SurvivesOnEndMoss extends SurvivesOnBlocks {
    List<Block> BLOCKS = List.of(EndBlocks.END_MOSS);

    @Override
    default List<Block> getSurvivableBlocks() {
        return BLOCKS;
    }
}
