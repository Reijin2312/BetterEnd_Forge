package org.betterx.betterend.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;

import org.joml.Matrix4f;

public class BetterEndSkyEffect extends DimensionSpecialEffects {
    private final BetterEndSkyRenderer renderer = new BetterEndSkyRenderer();

    public BetterEndSkyEffect() {
        super(Float.NaN, false, SkyType.END, true, false);
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 color, float sunHeight) {
        return color.scale(0.15F);
    }

    @Override
    public float[] getSunriseColor(float timeOfDay, float partialTicks) {
        return null;
    }

    @Override
    public boolean isFoggyAt(int x, int z) {
        return false;
    }

    @Override
    public boolean renderSky(
            ClientLevel level,
            int ticks,
            float partialTick,
            PoseStack poseStack,
            Camera camera,
            Matrix4f projectionMatrix,
            boolean isFoggy,
            Runnable setupFog
    ) {
        float time = (float) (((level.getDayTime() + (double) partialTick) % 360000L) * 0.000017453292F);
        renderer.renderSkyboxWithStars(poseStack, projectionMatrix, time);
        return true;
    }
}
