package org.betterx.betterend.blocks.basis;

import org.betterx.bclib.behaviours.interfaces.BehaviourStone;
import org.betterx.bclib.blocks.BaseTerrainBlock;
import org.betterx.bclib.interfaces.TagProvider;
import org.betterx.betterend.interfaces.PottableTerrain;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;

import java.util.List;

public class EndTerrainBlock extends BaseTerrainBlock implements PottableTerrain, TagProvider, BehaviourStone {
    public EndTerrainBlock(MapColor color) {
        super(Blocks.END_STONE, color);
    }

    @Override
    public void addTags(List<TagKey<Block>> blockTags, List<TagKey<Item>> itemTags) {
        blockTags.add(CommonBlockTags.END_STONES);
    }
}
