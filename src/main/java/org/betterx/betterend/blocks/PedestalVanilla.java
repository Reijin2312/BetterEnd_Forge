package org.betterx.betterend.blocks;

import org.betterx.bclib.behaviours.interfaces.BehaviourStone;
import org.betterx.betterend.blocks.basis.PedestalBlock;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class PedestalVanilla extends PedestalBlock implements BehaviourStone {

    public PedestalVanilla(Block parent) {
        super(parent);
    }

    @Override
    protected Map<String, String> createTexturesMap() {
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(parent);
        String name = blockId.getPath().replace("_block", "");
        return new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;

            {
                put("%mod%", blockId.getNamespace());
                put("%top%", "polished_" + name);
                put("%base%", "polished_" + name);
                put("%pillar%", name + "_pillar");
                put("%bottom%", "polished_" + name);
            }
        };
    }
}
