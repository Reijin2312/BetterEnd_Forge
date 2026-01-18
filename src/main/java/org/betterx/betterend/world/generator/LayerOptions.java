package org.betterx.betterend.world.generator;

import org.betterx.bclib.config.PathConfig;

import net.minecraft.util.Mth;

public class LayerOptions {
    public final float distance;
    public final float scale;
    public final float coverage;
    public final float center;
    public final float heightVariation;
    public final float minY;
    public final float maxY;
    public final long centerDist;
    public final boolean hasCentralIsland;

    public LayerOptions(
            String name,
            PathConfig config,
            float distance,
            float scale,
            float center,
            float heightVariation,
            boolean hasCentral
    ) {
        this.distance = clampDistance(config.getFloat(name, "distance[1-8192]", distance));
        this.scale = clampScale(config.getFloat(name, "scale[0.1-1024]", scale));
        this.center = clampCenter(config.getFloat(name, "averageHeight[0-1]", center));
        this.heightVariation = clampVariation(config.getFloat(name, "heightVariation[0-1]", heightVariation));
        this.coverage = clampCoverage(config.getFloat(name, "coverage[0-1]", 0.5F));
        this.minY = this.center - this.heightVariation;
        this.maxY = this.center + this.heightVariation;
        this.centerDist = Mth.floor(1000 / this.distance);
        this.hasCentralIsland = config.getBoolean(name, "hasCentralIsland", hasCentral);
    }

    private float clampDistance(float value) {
        return Mth.clamp(value, 1, 8192);
    }

    private float clampScale(float value) {
        return Mth.clamp(value, 0.1F, 1024);
    }

    private float clampCoverage(float value) {
        return 0.9999F - Mth.clamp(value, 0, 1) * 2;
    }

    private float clampCenter(float value) {
        return Mth.clamp(value, 0, 1.0f);
    }

    private float clampVariation(float value) {
        return Mth.clamp(value, 0, 1.0f);
    }
}
