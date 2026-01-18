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
        String top = "polished_" + name;
        String base = top;
        String bottom = top;
        String pillar = name + "_pillar";
        if ("minecraft".equals(blockId.getNamespace())) {
            switch (name) {
                case "quartz":
                    top = "quartz_block_top";
                    base = "quartz_block";
                    bottom = "quartz_block_top";
                    pillar = "quartz_pillar";
                    break;
                case "purpur":
                    top = "purpur_block";
                    base = "purpur_block";
                    bottom = "purpur_block";
                    pillar = "purpur_pillar";
                    break;
                default:
                    break;
            }
        }
        Map<String, String> textures = new HashMap<>();
        textures.put("%mod%", blockId.getNamespace());
        textures.put("%top%", top);
        textures.put("%base%", base);
        textures.put("%pillar%", pillar);
        textures.put("%bottom%", bottom);
        return textures;
    }
}
