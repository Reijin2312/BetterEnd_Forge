package org.betterx.betterend.blocks;

import org.betterx.bclib.items.BaseAnvilItem;
import org.betterx.betterend.blocks.basis.EndAnvilBlock;
import org.betterx.betterend.item.material.EndToolMaterial;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class AeterniumAnvil extends EndAnvilBlock {
    public AeterniumAnvil() {
        super(EndBlocks.AETERNIUM_BLOCK.defaultMapColor(), EndToolMaterial.AETERNIUM.getLevel());
    }

    @Override
    public int getMaxDurability() {
        return 12;
    }

    @Override
    public BlockItem getCustomItem(ResourceLocation blockID, Item.Properties settings) {
        return new BaseAnvilItem(this, settings.fireResistant());
    }
}
