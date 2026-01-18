package org.betterx.betterend.tab;

import org.betterx.bclib.creativetab.BCLCreativeTab;
import org.betterx.bclib.creativetab.BCLCreativeTabManager;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndItems;

public class CreativeTabs {

    public static void register() {
        BCLCreativeTabManager.create(BetterEnd.MOD_ID)
                             .createTab("nature")
                             .setPredicate(item -> BCLCreativeTab.NATURE.contains(item)
                                     || item == EndItems.END_LILY_LEAF
                                     || item == EndItems.END_LILY_LEAF_DRIED
                             )
                             .setIcon(EndBlocks.TENANEA_FLOWERS)
                             .build()
                             .createBlockTab(EndBlocks.END_MYCELIUM)
                             .build()
                             .createItemsTab(EndItems.ETERNAL_CRYSTAL)
                             .build()
                             .processBCLRegistry()
                             .register();
    }
}
