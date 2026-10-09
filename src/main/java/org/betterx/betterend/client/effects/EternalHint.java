package org.betterx.betterend.client.effects;

import org.betterx.betterend.blocks.RunedFlavolite;
import org.betterx.betterend.blocks.basis.PedestalBlock;
import org.betterx.betterend.portal.PortalBuilder;
import org.betterx.betterend.rituals.EternalRitual;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

import java.awt.Point;

public final class EternalHint {
    private static final int DURATION = 220;
    private static BlockPos source;
    private static BlockPos center;
    private static Direction.Axis axis;
    private static long expiresAt;

    private EternalHint() {}

    public static void trigger(Level level, BlockPos pos, Direction facing) {
        EternalRitual ritual = new EternalRitual(level, pos);
        source = pos.immutable();
        center = ritual.getCenter();
        axis = ritual.getAxis();
        if (center == null || axis == null) {
            Direction horizontal = facing.getAxis().isHorizontal() ? facing : Direction.NORTH;
            axis = horizontal.getAxis();
            center = pos.relative(horizontal, 6).immutable();
        }
        expiresAt = level.getGameTime() + DURATION;
    }

    public static void tick(Level level, BlockPos pos, RandomSource random) {
        if (source == null || !source.equals(pos)) return;
        if (level.getGameTime() > expiresAt) {
            source = null;
            return;
        }
        Direction moveX = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
        Direction moveY = axis == Direction.Axis.X ? Direction.NORTH : Direction.EAST;
        for (Point offset : EternalRitual.pedestalOffsets()) {
            BlockPos marker = center.relative(moveX, offset.x).relative(moveY, offset.y);
            if (level.getBlockState(marker).getBlock() instanceof PedestalBlock || random.nextBoolean()) continue;
            particle(level, marker, random, 1.2);
        }
        BlockPos frameBase = center.below();
        Direction frameDirection = axis == Direction.Axis.X ? Direction.NORTH : Direction.EAST;
        for (Point offset : PortalBuilder.FRAME_POSITIONS) {
            for (int sign = -1; sign <= 1; sign += 2) {
                if (sign > 0 && offset.x == 0) continue;
                BlockPos marker = frameBase.relative(frameDirection, sign * offset.x).above(offset.y);
                if (level.getBlockState(marker).getBlock() instanceof RunedFlavolite || random.nextInt(3) != 0) continue;
                particle(level, marker, random, 0.9);
            }
        }
    }

    private static void particle(Level level, BlockPos pos, RandomSource random, double height) {
        level.addParticle(ParticleTypes.END_ROD,
                pos.getX() + 0.15 + random.nextDouble() * 0.7,
                pos.getY() + 0.1 + random.nextDouble() * height,
                pos.getZ() + 0.15 + random.nextDouble() * 0.7,
                0, 0.01, 0);
    }
}
