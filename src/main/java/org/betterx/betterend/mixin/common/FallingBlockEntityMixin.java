package org.betterx.betterend.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin {
    @Redirect(
            method = "tick",
            at = @At(value = "NEW", target = "net/minecraft/world/item/context/DirectionalPlaceContext")
    )
    private DirectionalPlaceContext be_createSafeFallingBlockPlaceContext(
            Level level,
            BlockPos pos,
            Direction direction,
            ItemStack itemStack,
            Direction face
    ) {
        return new SafeFallingBlockPlaceContext(level, pos, direction, face);
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;canBeReplaced(Lnet/minecraft/world/item/context/BlockPlaceContext;)Z"
            )
    )
    private boolean be_skipRecursiveSlabReplaceCheck(BlockState targetState, BlockPlaceContext context) {
        if (targetState.getBlock() instanceof SlabBlock) {
            return false;
        }
        return targetState.canBeReplaced(context);
    }

    @Unique
    private static class SafeFallingBlockPlaceContext extends DirectionalPlaceContext {
        SafeFallingBlockPlaceContext(Level level, BlockPos pos, Direction direction, Direction face) {
            super(level, pos, direction, ItemStack.EMPTY, face);
        }

        @Override
        public boolean canPlace() {
            return false;
        }

        @Override
        public boolean replacingClickedOnBlock() {
            return false;
        }
    }
}
