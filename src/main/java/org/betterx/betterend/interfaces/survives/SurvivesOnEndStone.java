package org.betterx.betterend.interfaces.survives;

import org.betterx.bclib.interfaces.SurvivesOnTags;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.List;

public interface SurvivesOnEndStone extends SurvivesOnTags {
    List<TagKey<Block>> TAGS = List.of(CommonBlockTags.END_STONES);

    @Override
    default List<TagKey<Block>> getSurvivableTags() {
        return TAGS;
    }
}
