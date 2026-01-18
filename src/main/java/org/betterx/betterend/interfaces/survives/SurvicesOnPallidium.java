package org.betterx.betterend.interfaces.survives;

import org.betterx.bclib.interfaces.SurvivesOnBlocks;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.world.level.block.Block;

import java.util.List;

public interface SurvicesOnPallidium extends SurvivesOnBlocks {
    List<Block> BLOCKS = List.of(
            EndBlocks.PALLIDIUM_FULL,
            EndBlocks.PALLIDIUM_HEAVY,
            EndBlocks.PALLIDIUM_THIN,
            EndBlocks.PALLIDIUM_TINY
    );

    @Override
    default List<Block> getSurvivableBlocks() {
        return BLOCKS;
    }
}
