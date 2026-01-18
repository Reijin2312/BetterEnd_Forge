package org.betterx.betterend.blocks;

import org.betterx.bclib.blocks.BaseAttachedBlock;
import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.client.render.BCLRenderLayer;
import org.betterx.bclib.interfaces.BlockModelProvider;
import org.betterx.bclib.interfaces.RenderLayerProvider;
import org.betterx.betterend.client.models.Patterns;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.block.state.BlockBehaviour;

import com.google.common.collect.Maps;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;

public class ChandelierBlock extends BaseAttachedBlock.Metal implements RenderLayerProvider, BlockModelProvider {
    private static final EnumMap<Direction, VoxelShape> BOUNDING_SHAPES = Maps.newEnumMap(Direction.class);

    public ChandelierBlock(Block source) {
        super(BlockBehaviour.Properties.copy(source)
                                 .lightLevel((bs) -> 15)
                                 .noCollission()
                                 .noOcclusion()
                                 .requiresCorrectToolForDrops());
    }

    @Override
    public BCLRenderLayer getRenderLayer() {
        return BCLRenderLayer.CUTOUT;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
        return BOUNDING_SHAPES.get(state.getValue(FACING));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BlockModel getItemModel(ResourceLocation blockId) {
        return ModelsHelper.createItemModel(blockId);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public @Nullable BlockModel getBlockModel(ResourceLocation resourceLocation, BlockState blockState) {
        Optional<String> pattern;
        switch (blockState.getValue(FACING)) {
            case UP:
                pattern = Patterns.createJson(Patterns.BLOCK_CHANDELIER_FLOOR, resourceLocation.getPath());
                break;
            case DOWN:
                pattern = Patterns.createJson(Patterns.BLOCK_CHANDELIER_CEIL, resourceLocation.getPath());
                break;
            default:
                pattern = Patterns.createJson(Patterns.BLOCK_CHANDELIER_WALL, resourceLocation.getPath());
        }
        return ModelsHelper.fromPattern(pattern);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public UnbakedModel getModelVariant(
            ResourceLocation stateId,
            BlockState blockState,
            Map<ResourceLocation, UnbakedModel> modelCache
    ) {
        String state = "_wall";
        BlockModelRotation rotation = BlockModelRotation.X0_Y0;
        switch (blockState.getValue(FACING)) {
            case UP:
                state = "_floor";
                break;
            case DOWN:
                state = "_ceil";
                break;
            case EAST:
                rotation = BlockModelRotation.X0_Y270;
                break;
            case NORTH:
                rotation = BlockModelRotation.X0_Y180;
                break;
            case WEST:
                rotation = BlockModelRotation.X0_Y90;
                break;
            default:
                break;
        }
        ResourceLocation modelId = new ResourceLocation(stateId.getNamespace(), "block/" + stateId.getPath() + state);
        registerBlockModel(stateId, modelId, blockState, modelCache);
        return ModelsHelper.createMultiVariant(modelId, rotation.getRotation(), false);
    }

    static {
        BOUNDING_SHAPES.put(Direction.UP, Block.box(5, 0, 5, 11, 13, 11));
        BOUNDING_SHAPES.put(Direction.DOWN, Block.box(5, 3, 5, 11, 16, 11));
        BOUNDING_SHAPES.put(Direction.NORTH, Shapes.box(0.0, 0.0, 0.5, 1.0, 1.0, 1.0));
        BOUNDING_SHAPES.put(Direction.SOUTH, Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 0.5));
        BOUNDING_SHAPES.put(Direction.WEST, Shapes.box(0.5, 0.0, 0.0, 1.0, 1.0, 1.0));
        BOUNDING_SHAPES.put(Direction.EAST, Shapes.box(0.0, 0.0, 0.0, 0.5, 1.0, 1.0));
    }
}


