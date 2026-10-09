package org.betterx.betterend.mixin.common;

import org.betterx.betterend.item.tool.EndHammerItem;
import org.betterx.betterend.registry.EndEnchantments;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerPlayerGameMode.class, priority = 900)
public abstract class ServerPlayerGameModeMixin {
    @Shadow @Final protected ServerPlayer player;
    @Unique private boolean betterend$resonanceMining;

    @Inject(method = "destroyBlock", at = @At("RETURN"))
    private void betterend$mineResonanceArea(BlockPos origin, CallbackInfoReturnable<Boolean> cir) {
        if (betterend$resonanceMining || !cir.getReturnValue()) return;
        ItemStack tool = player.getMainHandItem();
        if (!(tool.getItem() instanceof EndHammerItem) || player.isShiftKeyDown()) return;
        int level = EnchantmentHelper.getItemEnchantmentLevel(EndEnchantments.RESONANCE, tool);
        if (level <= 0) return;

        Direction facing = Direction.getNearest(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z);
        int[] xRange = betterend$axisRange(facing.getStepX(), level);
        int[] yRange = betterend$axisRange(facing.getStepY(), level);
        int[] zRange = betterend$axisRange(facing.getStepZ(), level);
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        betterend$resonanceMining = true;
        try {
            ServerPlayerGameMode self = (ServerPlayerGameMode) (Object) this;
            for (int x = xRange[0]; x <= xRange[1]; x++) {
                for (int y = yRange[0]; y <= yRange[1]; y++) {
                    for (int z = zRange[0]; z <= zRange[1]; z++) {
                        if (x == 0 && y == 0 && z == 0) continue;
                        if (player.getMainHandItem().isEmpty()) return;
                        cursor.set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
                        self.destroyBlock(cursor.immutable());
                    }
                }
            }
        } finally {
            betterend$resonanceMining = false;
        }
    }

    @Unique
    private static int[] betterend$axisRange(int step, int radius) {
        if (step > 0) return new int[]{0, radius * 2};
        if (step < 0) return new int[]{-radius * 2, 0};
        return new int[]{-radius, radius};
    }
}
