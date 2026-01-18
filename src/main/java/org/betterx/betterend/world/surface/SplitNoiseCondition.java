package org.betterx.betterend.world.surface;

import org.betterx.bclib.interfaces.NumericProvider;
import org.betterx.bclib.mixin.common.SurfaceRulesContextAccessor;
import org.betterx.bclib.util.MHelper;
import org.betterx.betterend.noise.OpenSimplexNoise;

import com.mojang.serialization.Codec;

/**
 * Noise source that returns a value in [0, 1]
 */
public class SplitNoiseCondition implements NumericProvider {
    public static final SplitNoiseCondition DEFAULT = new SplitNoiseCondition();
    public static final Codec<SplitNoiseCondition> CODEC = Codec.BYTE.fieldOf("split_noise")
                                                                     .xmap((obj) -> DEFAULT, obj -> (byte) 0)
                                                                     .codec();

    private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(4141);

    @Override
    public int getNumber(SurfaceRulesContextAccessor context) {
        final int x = context.getBlockX();
        final int z = context.getBlockZ();
        float noise = (float) NOISE.eval(x * 0.1, z * 0.1) + MHelper.randRange(-0.4F, 0.4F, MHelper.RANDOM_SOURCE);
        return noise > 0 ? 1 : 0;
    }

    public double getNoise(int x, int z) {
        float noise = (float) NOISE.eval(x * 0.1, z * 0.1) + MHelper.randRange(
                -0.2F,
                0.2F,
                MHelper.RANDOM_SOURCE
        );
        return noise;
    }


    @Override
    public Codec<? extends NumericProvider> pcodec() {
        return CODEC;
    }


}
