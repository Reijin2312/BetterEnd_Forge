package org.betterx.betterend.client.effects;

import org.betterx.betterend.recipe.builders.InfusionRecipe;
import org.betterx.betterend.registry.EndParticles;
import org.betterx.betterend.rituals.InfusionRitual;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class InfusionHint {
    private static final int DURATION = 160;
    private static final int FADE_IN = 8;
    private static final int FADE_OUT = 30;
    private static final double MAX_DISTANCE_SQ = 48 * 48;
    private static final double MIST_SPAWN_HEIGHT = 1.0;
    private static final double MIST_SPAWN_JITTER = 0.35;
    private static final double MIST_SPREAD = 0.008;
    private static final int BURST_COUNT = 14;
    private static final double BURST_RADIUS = 0.34;
    private static final double BURST_HEIGHT = 0.08;
    private static final float SUBTLE_ALPHA = 0.45F;
    private static final int FLASH_PER_SPOKE = 3;
    private static final int FLASH_PULSES = 4;
    private static final int FLASH_PULSE_INTERVAL = 10;
    private static final double FLASH_HEIGHT = 1.6;

    private static BlockPos pedestalPos;
    private static long startTime;
    private static boolean subtle;
    private static BlockPos flashPos;
    private static long flashStart;

    private InfusionHint() {}

    public static void trigger(Level level, BlockPos pos) {
        start(level, pos, false);
    }

    public static void triggerSubtle(Level level, BlockPos pos) {
        start(level, pos, true);
    }

    private static void start(Level level, BlockPos pos, boolean quiet) {
        pedestalPos = pos.immutable();
        startTime = level.getGameTime();
        subtle = quiet;
    }

    public static void clear() {
        pedestalPos = null;
    }

    public static void dismiss(Level level) {
        if (pedestalPos == null) return;
        long elapsed = level.getGameTime() - startTime;
        long intoFade = DURATION - FADE_OUT;
        if (elapsed < intoFade) startTime = level.getGameTime() - intoFade;
    }

    public static BlockPos activePedestal() {
        return pedestalPos;
    }

    public static boolean isActiveAt(BlockPos pos) {
        return pedestalPos != null && pedestalPos.equals(pos);
    }

    private static float age(float partialTick) {
        if (pedestalPos == null) return -1.0F;
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        if (level == null || minecraft.player == null
                || minecraft.player.distanceToSqr(Vec3.atCenterOf(pedestalPos)) > MAX_DISTANCE_SQ) {
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

    public static void spawnMist(Level level, BlockPos pos) {
        if (subtle || !isActiveAt(pos) || age(0.0F) < 0.0F) return;
        boolean[] present = InfusionRitual.socketsPresent(level, pos);
        RandomSource random = level.getRandom();
        for (int i = 0; i < present.length; i++) {
            if (present[i] || random.nextBoolean()) continue;
            BlockPos socket = InfusionRitual.socketPos(pos, i);
            level.addParticle(
                    mistFor(i),
                    socket.getX() + 0.25 + random.nextDouble() * 0.5,
                    socket.getY() + MIST_SPAWN_HEIGHT + (random.nextDouble() - 0.5) * MIST_SPAWN_JITTER,
                    socket.getZ() + 0.25 + random.nextDouble() * 0.5,
                    (random.nextDouble() - 0.5) * MIST_SPREAD,
                    0.0,
                    (random.nextDouble() - 0.5) * MIST_SPREAD
            );
        }
    }

    public static float @Nullable [] outline(float partialTick) {
        float elapsed = age(partialTick);
        if (elapsed < 0.0F) return null;
        float fade;
        if (elapsed < FADE_IN) {
            fade = elapsed / FADE_IN;
        } else if (elapsed > DURATION - FADE_OUT) {
            fade = (DURATION - elapsed) / FADE_OUT;
        } else {
            fade = 1.0F;
        }
        if (subtle) fade *= SUBTLE_ALPHA;
        return new float[]{fade, 0.65F + 0.35F * Mth.sin(elapsed / 4.0F)};
    }

    private static SimpleParticleType mistFor(int index) {
        return index == InfusionRecipe.Catalysts.NORTH.index
                ? EndParticles.INFUSION_MIST_NORTH
                : EndParticles.INFUSION_MIST;
    }

    public static void burstAtSocket(Level level, BlockPos socketPos, int index) {
        RandomSource random = level.getRandom();
        SimpleParticleType type = mistFor(index);
        for (int i = 0; i < BURST_COUNT; i++) {
            double angle = i * Mth.TWO_PI / BURST_COUNT + random.nextDouble() * 0.3;
            level.addParticle(type,
                    socketPos.getX() + 0.5 + Math.cos(angle) * BURST_RADIUS,
                    socketPos.getY() + BURST_HEIGHT,
                    socketPos.getZ() + 0.5 + Math.sin(angle) * BURST_RADIUS,
                    0.0, 0.0, 0.0);
        }
    }

    public static void beginFlash(Level level, BlockPos pos) {
        flashPos = pos.immutable();
        flashStart = level.getGameTime();
        level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 0.8F, 1.0F, false);
    }

    public static void tickFlash(Level level, BlockPos pos) {
        if (flashPos == null || !flashPos.equals(pos)) return;
        long elapsed = level.getGameTime() - flashStart;
        if (elapsed < 0 || elapsed >= (long) FLASH_PULSES * FLASH_PULSE_INTERVAL) {
            flashPos = null;
            return;
        }
        if (elapsed % FLASH_PULSE_INTERVAL != 0) return;
        RandomSource random = level.getRandom();
        double originX = pos.getX() + 0.5;
        double originY = pos.getY() + FLASH_HEIGHT;
        double originZ = pos.getZ() + 0.5;
        for (int socket = 0; socket < InfusionRitual.getMap().length; socket++) {
            BlockPos target = InfusionRitual.socketPos(pos, socket);
            double targetX = target.getX() + 0.5;
            double targetY = target.getY() + FLASH_HEIGHT;
            double targetZ = target.getZ() + 0.5;
            for (int i = 0; i < FLASH_PER_SPOKE; i++) {
                double jitter = (random.nextDouble() - 0.5) * 0.4;
                level.addParticle(ParticleTypes.ENCHANT, targetX, targetY, targetZ,
                        originX - targetX + jitter, originY - targetY, originZ - targetZ + jitter);
            }
        }
    }

    public static float @Nullable [] wobble(float partialTick) {
        if (subtle) return null;
        return ScreenWobble.at(age(partialTick));
    }
}
