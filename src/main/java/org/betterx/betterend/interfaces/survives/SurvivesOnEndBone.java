package org.betterx.betterend.interfaces.survives;

import org.betterx.bclib.interfaces.SurvivesOnBlocks;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.world.level.block.Block;

import java.util.List;

public interface SurvivesOnEndBone extends SurvivesOnBlocks {
    List<Block> BLOCKS = List.of(
            EndBlocks.SANGNUM,
            EndBlocks.MOSSY_OBSIDIAN,
            EndBlocks.MOSSY_DRAGON_BONE
    );

    @Override
    default List<Block> getSurvivableBlocks() {
        return BLOCKS;
    }
}
