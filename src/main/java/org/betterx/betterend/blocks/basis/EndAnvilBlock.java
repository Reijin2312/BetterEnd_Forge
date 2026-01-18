package org.betterx.betterend.blocks.basis;

import org.betterx.bclib.behaviours.interfaces.BehaviourMetal;
import org.betterx.bclib.blocks.LeveledAnvilBlock;
import org.betterx.betterend.complexmaterials.MetalMaterial;

import net.minecraft.world.level.material.MapColor;

public class EndAnvilBlock extends LeveledAnvilBlock implements BehaviourMetal {
    protected MetalMaterial metalMaterial;

    public EndAnvilBlock(MapColor color, int level) {
        super(color, level);
    }

    public EndAnvilBlock(MetalMaterial metalMaterial, MapColor color, int level) {
        super(color, level);
        this.metalMaterial = metalMaterial;
    }
}
