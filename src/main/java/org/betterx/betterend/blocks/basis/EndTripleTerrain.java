package org.betterx.betterend.blocks.basis;

import org.betterx.bclib.behaviours.interfaces.BehaviourStone;
import org.betterx.bclib.blocks.TripleTerrainBlock;
import org.betterx.betterend.interfaces.PottableTerrain;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;

public class EndTripleTerrain extends TripleTerrainBlock implements PottableTerrain, BehaviourStone {
    public EndTripleTerrain(MapColor color) {
        super(Blocks.END_STONE, color);
    }
}
