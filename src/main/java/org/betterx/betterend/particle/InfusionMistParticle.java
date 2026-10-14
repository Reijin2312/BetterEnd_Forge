package org.betterx.betterend.particle;

import org.betterx.bclib.util.MHelper;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class InfusionMistParticle extends TextureSheetParticle {
    private static final double SINK_SPEED_MIN = 0.022;
    private static final double SINK_SPEED_MAX = 0.048;
    private static final double SWAY_STRENGTH = 0.0015;
    private static final float SWAY_SPEED_MIN = 0.12F;
    private static final float SWAY_SPEED_MAX = 0.28F;
    private static final double MAX_FALL_DRIFT = 0.012;
    private static final double GROUND_ACCELERATION = 0.0035;
    private static final double MAX_GROUND_SPEED = 0.045;
    private static final float GROUND_GROWTH = 1.025F;
    private static final float MAX_QUAD_SIZE = 0.42F;
    private static final float MAX_ALPHA = 0.85F;
    private static final int FADE_TICKS = 10;

    public static final int DEFAULT_RGB = 0xFFD98A;
    public static final int NORTH_RGB = 0xC79BFF;

    private final double driftX;
    private final double driftZ;
    private final double sinkSpeed;
    private final float swaySpeed;
    private final float swayPhase;
    private final float swayAxis;
    private double groundSpeed;
    private boolean landed;

    protected InfusionMistParticle(
            ClientLevel world,
            double x,
            double y,
            double z,
            double vX,
            double vY,
            double vZ,
            SpriteSet sprites,
            int rgb
    ) {
        super(world, x, y, z, vX, vY, vZ);
        this.pickSprite(sprites);
        this.lifetime = MHelper.randRange(50, 80, random);
        this.quadSize = MHelper.randRange(0.10F, 0.20F, random);
        this.sinkSpeed = MHelper.randRange(SINK_SPEED_MIN, SINK_SPEED_MAX, random);
        this.swaySpeed = MHelper.randRange(SWAY_SPEED_MIN, SWAY_SPEED_MAX, random);
        this.swayPhase = random.nextFloat() * Mth.TWO_PI;
        this.swayAxis = random.nextFloat() * Mth.TWO_PI;
        this.xd = vX;
        this.yd = vY;
        this.zd = vZ;
        this.rCol = ((rgb >> 16) & 0xFF) / 255.0F;
        this.gCol = ((rgb >> 8) & 0xFF) / 255.0F;
        this.bCol = (rgb & 0xFF) / 255.0F;
        this.setAlpha(0.0F);
        this.gravity = 0.0F;

        double offsetX = x - Math.floor(x) - 0.5;
        double offsetZ = z - Math.floor(z) - 0.5;
        double length = Math.sqrt(offsetX * offsetX + offsetZ * offsetZ);
        if (length < 1.0E-4) {
            float angle = random.nextFloat() * Mth.TWO_PI;
            this.driftX = Mth.cos(angle);
            this.driftZ = Mth.sin(angle);
        } else {
            this.driftX = offsetX / length;
            this.driftZ = offsetZ / length;
        }
    }

    @Override
    public void tick() {
        if (!this.landed) {
            this.yd = -this.sinkSpeed;
            float sway = Mth.sin(this.swayPhase + this.age * this.swaySpeed) * (float) SWAY_STRENGTH;
            this.xd = Mth.clamp(this.xd + Mth.cos(this.swayAxis) * sway, -MAX_FALL_DRIFT, MAX_FALL_DRIFT);
            this.zd = Mth.clamp(this.zd + Mth.sin(this.swayAxis) * sway, -MAX_FALL_DRIFT, MAX_FALL_DRIFT);
        }

        if (this.age < FADE_TICKS) {
            this.setAlpha(MAX_ALPHA * this.age / FADE_TICKS);
        } else if (this.age >= this.lifetime - FADE_TICKS) {
            this.setAlpha(MAX_ALPHA * (this.lifetime - this.age) / FADE_TICKS);
        } else {
            this.setAlpha(MAX_ALPHA);
        }

        super.tick();
        if (this.onGround) this.landed = true;
        if (this.landed) {
            this.groundSpeed = Math.min(MAX_GROUND_SPEED, this.groundSpeed + GROUND_ACCELERATION);
            this.quadSize = Math.min(MAX_QUAD_SIZE, this.quadSize * GROUND_GROWTH);
            setPos(this.x + this.driftX * this.groundSpeed, this.y, this.z + this.driftZ * this.groundSpeed);
        }
    }

    @Override
    public int getLightColor(float tint) {
        return 15728880;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class FactoryInfusionMist implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        private final int rgb;

        public FactoryInfusionMist(SpriteSet sprites) {
            this(sprites, DEFAULT_RGB);
        }

        public FactoryInfusionMist(SpriteSet sprites, int rgb) {
            this.sprites = sprites;
            this.rgb = rgb;
        }

        @Override
        public Particle createParticle(
                SimpleParticleType type,
                ClientLevel world,
                double x,
                double y,
                double z,
                double vX,
                double vY,
                double vZ
        ) {
            return new InfusionMistParticle(world, x, y, z, vX, vY, vZ, sprites, rgb);
        }
    }
}
