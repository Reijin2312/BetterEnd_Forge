package org.betterx.betterend.integration.byg;

import org.betterx.bclib.integration.ModIntegration;
import org.betterx.betterend.integration.EndBiomeIntegration;
import org.betterx.betterend.integration.Integrations;
import org.betterx.betterend.integration.byg.biomes.BYGBiomes;
import org.betterx.betterend.integration.byg.features.BYGFeatures;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;
import org.betterx.worlds.together.tag.v3.TagManager;

import net.minecraft.world.level.block.Block;

public class BYGIntegration extends ModIntegration implements EndBiomeIntegration {
    public BYGIntegration() {
        super("byg");
    }

    @Override
    public void init() {
        Block block = Integrations.BYG.getBlock("ivis_phylium");
        if (block != null) {
            TagManager.BLOCKS.add(block, CommonBlockTags.END_STONES, CommonBlockTags.GEN_END_STONES);
        }
        BYGBlocks.register();
        BYGFeatures.register();
        BYGBiomes.register();
    }

    @Override
    public void addBiomes() {
        BYGBiomes.addBiomes();
    }
}
