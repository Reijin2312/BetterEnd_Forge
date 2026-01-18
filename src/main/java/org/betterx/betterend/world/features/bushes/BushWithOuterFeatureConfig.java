package org.betterx.betterend.world.features.bushes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;

public class BushWithOuterFeatureConfig extends BushFeatureConfig {
    public static final Codec<BushWithOuterFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    BlockStateProvider.CODEC.fieldOf("leaves").forGetter(o -> o.leaves),
                    BlockStateProvider.CODEC.fieldOf("outer_leaves").forGetter(o -> o.outer_leaves),
                    BlockStateProvider.CODEC.fieldOf("stem").forGetter(o -> o.stem)
            )
            .apply(instance, BushWithOuterFeatureConfig::new));


    public final BlockStateProvider outer_leaves;

    public BushWithOuterFeatureConfig(Block leaves, Block outer_leaves, Block stem) {
        this(
                SimpleStateProvider.simple(leaves),
                SimpleStateProvider.simple(outer_leaves),
                SimpleStateProvider.simple(stem)
        );
    }

    public BushWithOuterFeatureConfig(
            BlockStateProvider leaves,
            BlockStateProvider outer_leaves,
            BlockStateProvider stem
    ) {
        super(leaves, stem);
        this.outer_leaves = outer_leaves;
    }
}
