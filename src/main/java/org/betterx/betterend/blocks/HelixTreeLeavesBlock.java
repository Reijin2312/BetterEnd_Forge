package org.betterx.betterend.blocks;

import org.betterx.bclib.behaviours.BehaviourBuilders;
import org.betterx.bclib.behaviours.interfaces.BehaviourLeaves;
import org.betterx.bclib.blocks.BaseBlock;
import org.betterx.bclib.blocks.BaseLeavesBlock;
import org.betterx.bclib.interfaces.CustomColorProvider;
import org.betterx.bclib.interfaces.tools.AddMineableShears;
import org.betterx.bclib.util.MHelper;
import org.betterx.betterend.noise.OpenSimplexNoise;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.ui.ColorUtil;
import org.betterx.worlds.together.tag.v3.TagManager;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.List;

public class HelixTreeLeavesBlock extends BaseBlock implements BehaviourLeaves, CustomColorProvider, AddMineableShears {
    public static final IntegerProperty COLOR = EndBlockProperties.COLOR;
    private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(0);

    public HelixTreeLeavesBlock() {
        super(BehaviourBuilders
                .createStaticLeaves(MapColor.COLOR_ORANGE, true)
                .sound(SoundType.WART_BLOCK)
        );

        TagManager.BLOCKS.add(BlockTags.LEAVES, this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(COLOR);
    }

    @Override
    public BlockColor getProvider() {
        return (state, world, pos, tintIndex) -> {
            return ColorUtil.color(237, getGreen(state.getValue(COLOR)), 20);
        };
    }

    @Override
    public ItemColor getItemProvider() {
        return (stack, tintIndex) -> {
            return ColorUtil.color(237, getGreen(4), 20);
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        double px = ctx.getClickedPos().getX() * 0.1;
        double py = ctx.getClickedPos().getY() * 0.1;
        double pz = ctx.getClickedPos().getZ() * 0.1;
        return this.defaultBlockState().setValue(COLOR, MHelper.floor(NOISE.eval(px, py, pz) * 3.5 + 4));
    }

    private int getGreen(int color) {
        float delta = color / 7F;
        return (int) Mth.lerp(delta, 80, 158);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return BaseLeavesBlock.getLeaveDrops(this, EndBlocks.HELIX_TREE_SAPLING, builder, 16, 32);
    }
}
