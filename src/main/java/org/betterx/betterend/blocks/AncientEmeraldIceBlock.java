package org.betterx.betterend.blocks;

import org.betterx.bclib.behaviours.interfaces.BehaviourIce;
import org.betterx.bclib.blocks.BaseBlock;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.bclib.util.MHelper;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndParticles;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Collections;
import java.util.List;

public class AncientEmeraldIceBlock extends BaseBlock implements BehaviourIce {
    public AncientEmeraldIceBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.BLUE_ICE).randomTicks());
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        Direction dir = BlocksHelper.randomDirection(random);

        if (random.nextBoolean()) {
            int x = MHelper.randRange(-2, 2, random);
            int y = MHelper.randRange(-2, 2, random);
            int z = MHelper.randRange(-2, 2, random);
            BlockPos p = pos.offset(x, y, z);
            if (world.getBlockState(p).is(Blocks.WATER)) {
                world.setBlockAndUpdate(p, EndBlocks.EMERALD_ICE.defaultBlockState());
                makeParticles(world, p, random);
            }
        }

        pos = pos.relative(dir);
        state = world.getBlockState(pos);
        if (state.is(Blocks.WATER)) {
            world.setBlockAndUpdate(pos, EndBlocks.EMERALD_ICE.defaultBlockState());
            makeParticles(world, pos, random);
        } else if (state.is(EndBlocks.EMERALD_ICE)) {
            world.setBlockAndUpdate(pos, EndBlocks.DENSE_EMERALD_ICE.defaultBlockState());
            makeParticles(world, pos, random);
        }
    }

    private void makeParticles(ServerLevel world, BlockPos pos, RandomSource random) {
        world.sendParticles(
                EndParticles.SNOWFLAKE,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                20,
                0.5,
                0.5,
                0.5,
                0
        );
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        ItemStack tool = builder.getOptionalParameter(LootContextParams.TOOL);
        if (tool != null && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) != 0) {
            return Collections.singletonList(new ItemStack(this));
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        super.stepOn(level, blockPos, blockState, entity);
        entity.setIsInPowderSnow(true);
    }
}

