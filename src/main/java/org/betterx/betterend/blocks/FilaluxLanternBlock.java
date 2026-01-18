package org.betterx.betterend.blocks;

import org.betterx.bclib.blocks.BaseBlock;

import net.minecraft.world.level.block.SoundType;

public class FilaluxLanternBlock extends BaseBlock.Wood {
    public FilaluxLanternBlock() {
        super(Properties.of()
                        .lightLevel((bs) -> 15)
                        .sound(SoundType.WOOD));
    }
}
