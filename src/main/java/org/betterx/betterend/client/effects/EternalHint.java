package org.betterx.betterend.client.effects;

import de.ambertation.wunderlib.math.Float3;
import org.betterx.bclib.interfaces.ClientLevelAccess;
import org.betterx.betterend.blocks.RunedFlavolite;
import org.betterx.betterend.client.render.EternalCrystalRenderer;
import org.betterx.betterend.portal.PortalBuilder;
import org.betterx.betterend.rituals.EternalRitual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public final class EternalHint {
    private static final int DURATION = 220;
    private static final int FADE_IN = 8;
    private static final int FADE_OUT = 40;
    private static final double MAX_DISTANCE_SQ = 64 * 64;
    public static final double CRYSTAL_HEIGHT = 1.35;
    private static final double STREAK_SPEED = 0.55;
    private static final int STREAKS_PER_TICK = 2;
    private static final float DRIP_CHANCE = 0.35F;

    private static BlockPos center;
    private static BlockPos source;
    private static Direction.Axis axis;
    private static long startTime;

    private EternalHint() {}

    public static void trigger(Level level, BlockPos pos) {
        EternalRitual ritual = new EternalRitual(level, pos);
        if (ritual.getCenter() == null || ritual.getAxis() == null) return;
        center = ritual.getCenter().immutable();
        source = pos.immutable();
        axis = ritual.getAxis();
        startTime = level.getGameTime();
    }

    public static void clear() {
        center = null;
        source = null;
        axis = null;
    }

    public static @Nullable BlockPos anchorPedestal(Level level) {
        return intensity(0.0F) > 0.0F ? source : null;
    }

    public static @Nullable BlockPos center() {
        return center;
    }

    private static float age(float partialTick) {
        if (center == null || axis == null) return -1.0F;
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        if (level == null || minecraft.player == null
                || minecraft.player.distanceToSqr(Vec3.atCenterOf(center)) > MAX_DISTANCE_SQ) {
            clear();
            return -1.0F;
        }
        float elapsed = level.getGameTime() - startTime + partialTick;
        if (elapsed < 0.0F || elapsed > DURATION) {
            clear();
            return -1.0F;
        }
        return elapsed;
    }

    public static float intensity(float partialTick) {
        float elapsed = age(partialTick);
        if (elapsed < 0.0F) return 0.0F;
        if (elapsed < FADE_IN) return elapsed / FADE_IN;
        if (elapsed > DURATION - FADE_OUT) return (DURATION - elapsed) / FADE_OUT;
        return 1.0F;
    }

    public static float @Nullable [] wobble(float partialTick) {
        return ScreenWobble.at(age(partialTick));
    }

    public static List<BlockPos> pedestalPositions() {
        List<BlockPos> result = new ArrayList<>();
        if (center == null || axis == null) return result;
        Direction moveX = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
        Direction moveY = axis == Direction.Axis.X ? Direction.NORTH : Direction.EAST;
        for (Point point : EternalRitual.pedestalOffsets()) {
            result.add(center.relative(moveX, point.x).relative(moveY, point.y));
        }
        return result;
    }

    public static List<BlockPos> missingFrame(Level level) {
        List<BlockPos> result = new ArrayList<>();
        if (center == null || axis == null) return result;
        BlockPos framePos = center.below();
        Direction moveDir = axis == Direction.Axis.X ? Direction.NORTH : Direction.EAST;
        for (Point point : PortalBuilder.FRAME_POSITIONS) {
            for (int sign = -1; sign <= 1; sign += 2) {
                if (sign > 0 && point.x == 0) continue;
                BlockPos pos = framePos.relative(moveDir, sign * point.x).above(point.y);
                if (!(level.getBlockState(pos).getBlock() instanceof RunedFlavolite)) result.add(pos);
            }
        }
        return result;
    }

    public static void tickParticles(Level level, BlockPos pedestalPos, RandomSource random) {
        if (intensity(0.0F) <= 0.0F || !(level instanceof ClientLevelAccess clientLevel)) return;
        float[] color = EternalCrystalRenderer.colors((int) (level.getGameTime() % 314L));
        streakToCentre(clientLevel, pedestalPos, random, color);
        markPedestals(clientLevel, random, color);
        dripFromGaps(clientLevel, level, random, color);
    }

    private static void markPedestals(ClientLevelAccess level, RandomSource random, float[] color) {
        for (BlockPos marker : pedestalPositions()) {
            if (random.nextFloat() > 0.45F) continue;
            Particle particle = level.bcl_addParticle(
                    ParticleTypes.EFFECT,
                    marker.getX() + 0.35 + random.nextDouble() * 0.3,
                    marker.getY() + CRYSTAL_HEIGHT + (random.nextDouble() - 0.5) * 0.25,
                    marker.getZ() + 0.35 + random.nextDouble() * 0.3,
                    0.0, 0.015, 0.0
            );
            if (particle != null) {
                particle.setColor(color[0], color[1], color[2]);
                particle.setLifetime(12 + random.nextInt(8));
            }
        }
    }

    private static void streakToCentre(
            ClientLevelAccess level,
            BlockPos pedestalPos,
            RandomSource random,
            float[] color
    ) {
        if (center == null) return;
        Float3 start = Float3.of(pedestalPos);
        Float3 towards = Float3.of(center).sub(start).normalized().mul(STREAK_SPEED);
        for (int i = 0; i < STREAKS_PER_TICK; i++) {
            Particle particle = level.bcl_addParticle(
                    ParticleTypes.EFFECT,
                    start.x + 0.3 + random.nextFloat() * 0.4,
                    start.y + CRYSTAL_HEIGHT,
                    start.z + 0.3 + random.nextFloat() * 0.4,
                    0.0, 0.0, 0.0
            );
            if (particle == null) continue;
            particle.setColor(color[0], color[1], color[2]);
            particle.setParticleSpeed(towards.x, towards.y, towards.z);
            particle.setLifetime(5 + random.nextInt(3));
        }
    }

    private static void dripFromGaps(
            ClientLevelAccess clientLevel,
            Level level,
            RandomSource random,
            float[] color
    ) {
        for (BlockPos gap : missingFrame(level)) {
            if (random.nextFloat() > DRIP_CHANCE) continue;
            Particle particle = clientLevel.bcl_addParticle(
                    ParticleTypes.EFFECT,
                    gap.getX() + random.nextDouble(),
                    gap.getY() + 0.05,
                    gap.getZ() + random.nextDouble(),
                    0.0, 0.0, 0.0
            );
            if (particle == null) continue;
            particle.setColor(color[0], color[1], color[2]);
            particle.setParticleSpeed(0.0, -0.08 - random.nextDouble() * 0.05, 0.0);
        }
    }
}
