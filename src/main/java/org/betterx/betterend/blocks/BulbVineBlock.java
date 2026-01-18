package org.betterx.betterend.blocks;

import org.betterx.bclib.blocks.BaseVineBlock;
import org.betterx.bclib.blocks.BlockProperties.TripleShape;
import org.betterx.bclib.util.MHelper;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndItems;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

import com.google.common.collect.Lists;

import java.util.List;

public class BulbVineBlock extends BaseVineBlock {
    public BulbVineBlock() {
        super(15, true);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        if (state.getValue(SHAPE) == TripleShape.BOTTOM) {
            return Lists.newArrayList(new ItemStack(EndItems.GLOWING_BULB));
        } else if (MHelper.RANDOM.nextInt(8) == 0) {
            return Lists.newArrayList(new ItemStack(EndBlocks.BULB_VINE_SEED));
        } else {
            return Lists.newArrayList();
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state, boolean isClient) {
        return false;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        boolean canPlace = super.canSurvive(state, world, pos);
        return (state.is(this) && state.getValue(SHAPE) == TripleShape.BOTTOM)
                ? canPlace
                : canPlace && world.getBlockState(
                        pos.below()).is(this);
    }
}
