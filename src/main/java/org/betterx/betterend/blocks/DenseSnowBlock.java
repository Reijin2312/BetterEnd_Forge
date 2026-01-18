package org.betterx.betterend.blocks;

import org.betterx.bclib.behaviours.BehaviourBuilders;
import org.betterx.bclib.behaviours.interfaces.BehaviourSnow;
import org.betterx.bclib.blocks.BaseBlock;

public class DenseSnowBlock extends BaseBlock implements BehaviourSnow {
    public DenseSnowBlock() {
        super(BehaviourBuilders.createSnow());
    }
}
