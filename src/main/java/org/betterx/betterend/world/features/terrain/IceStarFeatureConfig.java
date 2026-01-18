package org.betterx.betterend.world.features.terrain;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class IceStarFeatureConfig implements FeatureConfiguration {
    public static final Codec<IceStarFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    Codec.FLOAT.fieldOf("min_size").forGetter(o -> o.minSize),
                    Codec.FLOAT.fieldOf("max_size").forGetter(o -> o.maxSize),
                    Codec.INT.fieldOf("min_count").forGetter(o -> o.minCount),
                    Codec.INT.fieldOf("max_count").forGetter(o -> o.maxCount)
            )
            .apply(instance, IceStarFeatureConfig::new));


    public final float minSize;
    public final float maxSize;
    public final int minCount;
    public final int maxCount;


    public IceStarFeatureConfig(float minSize, float maxSize, int minCount, int maxCount) {
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }
}
