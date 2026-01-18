package org.betterx.datagen.betterend;

import org.betterx.bclib.api.v3.datagen.TagDataProvider;
import org.betterx.betterend.BetterEnd;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;
import org.betterx.worlds.together.tag.v3.TagManager;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;

import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class EndBlockTagDataProvider extends TagDataProvider<Block> {

    public EndBlockTagDataProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture,
            ExistingFileHelper existingFileHelper
    ) {
        super(
                TagManager.BLOCKS,
                List.of(BetterEnd.MOD_ID, "c"),
                Set.of(CommonBlockTags.NETHER_MYCELIUM),
                output,
                registriesFuture,
                existingFileHelper
        );
    }
}
