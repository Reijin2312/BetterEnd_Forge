package org.betterx.datagen.betterend;

import org.betterx.bclib.api.v3.datagen.TagDataProvider;
import org.betterx.betterend.BetterEnd;
import org.betterx.worlds.together.tag.v3.TagManager;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;

import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EndItemTagDataProvider extends TagDataProvider<Item> {

    public EndItemTagDataProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture,
            ExistingFileHelper existingFileHelper
    ) {
        super(TagManager.ITEMS, List.of(BetterEnd.MOD_ID, "c"), output, registriesFuture, existingFileHelper);
    }
}
