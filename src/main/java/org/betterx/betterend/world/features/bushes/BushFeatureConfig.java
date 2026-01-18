package org.betterx.betterend.world.features.bushes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;

public class BushFeatureConfig implements FeatureConfiguration {
    public static final Codec<BushFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    BlockStateProvider.CODEC.fieldOf("leaves").forGetter(o -> o.leaves),
                    BlockStateProvider.CODEC.fieldOf("stem").forGetter(o -> o.stem)
            )
            .apply(instance, BushFeatureConfig::new));


    public final BlockStateProvider leaves;
    public final BlockStateProvider stem;

    public BushFeatureConfig(Block leaves, Block stem) {
        this(
                SimpleStateProvider.simple(leaves),
                SimpleStateProvider.simple(stem)
        );
    }

    public BushFeatureConfig(
            BlockStateProvider leaves,
            BlockStateProvider stem
    ) {
        this.leaves = leaves;
        this.stem = stem;
    }
}
