package org.betterx.betterend.blocks;

import org.betterx.bclib.interfaces.TagProvider;
import org.betterx.betterend.blocks.basis.EndTerrainBlock;
import org.betterx.betterend.registry.EndParticles;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class ShadowGrassBlock extends EndTerrainBlock implements TagProvider {
    public ShadowGrassBlock() {
        super(MapColor.COLOR_BLACK);
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        super.animateTick(state, world, pos, random);
        if (random.nextInt(32) == 0) {
            world.addParticle(
                    EndParticles.BLACK_SPORE,
                    (double) pos.getX() + random.nextDouble(),
                    (double) pos.getY() + 1.1D,
                    (double) pos.getZ() + random.nextDouble(),
                    0.0D,
                    0.0D,
                    0.0D
            );
        }
    }

    @Override
    public void addTags(List<TagKey<Block>> blockTags, List<TagKey<Item>> itemTags) {
        blockTags.add(CommonBlockTags.END_STONES);
    }
}

