package org.betterx.betterend.interfaces.survives;

import org.betterx.bclib.interfaces.SurvivesOnBlocks;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.world.level.block.Block;

import java.util.List;

public interface SurvivesOnMossOrMycelium extends SurvivesOnBlocks {
    List<Block> BLOCKS = List.of(EndBlocks.END_MOSS, EndBlocks.END_MYCELIUM);

    @Override
    default List<Block> getSurvivableBlocks() {
        return BLOCKS;
    }
}
