package org.betterx.betterend.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class NBTFeatureConfig implements FeatureConfiguration {
    public static final Codec<NBTFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    BlockState.CODEC.fieldOf("default").forGetter(o -> o.defaultBlock)
            )
            .apply(instance, NBTFeatureConfig::new)
    );
    public final BlockState defaultBlock;

    public NBTFeatureConfig(BlockState defaultBlock) {
        this.defaultBlock = defaultBlock;
    }
}
