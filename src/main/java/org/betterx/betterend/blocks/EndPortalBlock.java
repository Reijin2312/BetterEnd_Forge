package org.betterx.betterend.blocks;

import org.betterx.bclib.client.render.BCLRenderLayer;
import org.betterx.bclib.interfaces.CustomColorProvider;
import org.betterx.bclib.interfaces.RenderLayerProvider;
import org.betterx.betterend.portal.TravelingEntity;
import org.betterx.betterend.registry.EndParticles;
import org.betterx.betterend.registry.EndPortals;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class EndPortalBlock extends NetherPortalBlock implements RenderLayerProvider, CustomColorProvider {
    public static final IntegerProperty PORTAL = EndBlockProperties.PORTAL;

    public EndPortalBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.NETHER_PORTAL)
                                 .explosionResistance(Blocks.BEDROCK.getExplosionResistance())
                                 .lightLevel((bs) -> 15));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PORTAL);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (random.nextInt(100) == 0) {
            world.playLocalSound(
                    pos.getX() + 0.5D,
                    pos.getY() + 0.5D,
                    pos.getZ() + 0.5D,
                    SoundEvents.PORTAL_AMBIENT,
                    SoundSource.BLOCKS,
                    0.5F,
                    random.nextFloat() * 0.4F + 0.8F,
                    false
            );
        }

        double x = pos.getX() + random.nextDouble();
        double y = pos.getY() + random.nextDouble();
        double z = pos.getZ() + random.nextDouble();
        int k = random.nextInt(2) * 2 - 1;
        if (!world.getBlockState(pos.west()).is(this) && !world.getBlockState(pos.east()).is(this)) {
            x = pos.getX() + 0.5D + 0.25D * k;
        } else {
            z = pos.getZ() + 0.5D + 0.25D * k;
        }

        world.addParticle(EndParticles.PORTAL_SPHERE, x, y, z, 0, 0, 0);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
    }

    @Override
    public BlockState updateShape(
            BlockState state,
            Direction direction,
            BlockState newState,
            LevelAccessor world,
            BlockPos pos,
            BlockPos posFrom
    ) {
        return state;
    }


    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (validate(entity) && entity instanceof TravelingEntity te && te.be_getTravelerState() != null) {
            te.be_getTravelerState().handleInsidePortal(pos);
        }
    }

    private boolean validate(Entity entity) {
        return !entity.isPassenger() && !entity.isVehicle() && entity.canChangeDimensions();
    }

    @Override
    public BCLRenderLayer getRenderLayer() {
        return BCLRenderLayer.TRANSLUCENT;
    }

    @Override
    public BlockColor getProvider() {
        return (state, world, pos, tintIndex) -> EndPortals.getColor(state.getValue(PORTAL));
    }

    @Override
    public ItemColor getItemProvider() {
        return (stack, tintIndex) -> EndPortals.getColor(0);
    }
}


