package org.betterx.betterend.client.effects;

import org.betterx.betterend.rituals.InfusionRitual;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

public final class InfusionHint {
    private static final int DURATION = 160;
    private static BlockPos pedestal;
    private static long expiresAt;

    private InfusionHint() {}

    public static void trigger(Level level, BlockPos pos) {
        pedestal = pos.immutable();
        expiresAt = level.getGameTime() + DURATION;
    }

    public static void tick(Level level, BlockPos pos) {
        if (pedestal == null || !pedestal.equals(pos)) return;
        if (level.getGameTime() > expiresAt) {
            pedestal = null;
            return;
        }
        boolean[] present = InfusionRitual.socketsPresent(level, pos);
        RandomSource random = level.getRandom();
        for (int i = 0; i < present.length; i++) {
            if (present[i] || random.nextBoolean()) continue;
            BlockPos socket = InfusionRitual.socketPos(pos, i);
            level.addParticle(ParticleTypes.END_ROD,
                    socket.getX() + 0.2 + random.nextDouble() * 0.6,
                    socket.getY() + 0.1 + random.nextDouble() * 1.2,
                    socket.getZ() + 0.2 + random.nextDouble() * 0.6,
                    0, 0.015, 0);
        }
    }
}
