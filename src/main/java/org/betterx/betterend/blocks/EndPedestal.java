package org.betterx.betterend.blocks;

import org.betterx.bclib.behaviours.interfaces.BehaviourMetal;
import org.betterx.bclib.behaviours.interfaces.BehaviourStone;
import org.betterx.bclib.behaviours.interfaces.BehaviourWood;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.blocks.basis.PedestalBlock;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import com.google.common.collect.Maps;

import java.util.Map;

public abstract class EndPedestal extends PedestalBlock {

    protected EndPedestal(Block parent) {
        super(parent);
    }

    @Override
    protected Map<String, String> createTexturesMap() {
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(parent);
        String name = blockId.getPath();
        Map<String, String> textures = Maps.newHashMap();
        textures.put("%mod%", BetterEnd.MOD_ID);
        textures.put("%top%", name + "_polished");
        textures.put("%base%", name + "_polished");
        textures.put("%pillar%", name + "_pillar_side");
        textures.put("%bottom%", name + "_polished");
        return textures;
    }

    public static class Stone extends EndPedestal implements BehaviourStone {
        public Stone(Block parent) {
            super(parent);
        }

    }

    public static class Wood extends EndPedestal implements BehaviourWood {
        public Wood(Block parent) {
            super(parent);
        }

    }

    public static class Metal extends EndPedestal implements BehaviourMetal {
        public Metal(Block parent) {
            super(parent);
        }

    }
}
