package org.betterx.betterend.blocks;

import org.betterx.bclib.behaviours.BehaviourBuilders;
import org.betterx.bclib.behaviours.interfaces.BehaviourGlass;
import org.betterx.betterend.blocks.basis.LitPillarBlock;

import net.minecraft.world.level.block.SoundType;

public class SmaragdantCrystalBlock extends LitPillarBlock implements BehaviourGlass {
    public SmaragdantCrystalBlock() {
        super(BehaviourBuilders
                .createGlass()
                .lightLevel((bs) -> 15)
                .strength(1F)
                .noOcclusion()
                .sound(SoundType.AMETHYST));
    }
}
