package org.betterx.betterend.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class ScatterFeatureConfig implements FeatureConfiguration {
    public static final Codec<ScatterFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    Codec.INT.fieldOf("radius").forGetter(o -> o.radius)
            )
            .apply(instance, ScatterFeatureConfig::new));

    public final int radius;

    public ScatterFeatureConfig(int radius) {
        this.radius = radius;
    }

    public static ScatterFeatureConfig blueVine() {
        return new ScatterFeatureConfig(5);
    }

    public static ScatterFeatureConfig filalux() {
        return new ScatterFeatureConfig(10);
    }

    public static ScatterFeatureConfig glowPillar() {
        return new ScatterFeatureConfig(9);
    }

    public static ScatterFeatureConfig lanceleaf() {
        return new ScatterFeatureConfig(7);
    }
}
