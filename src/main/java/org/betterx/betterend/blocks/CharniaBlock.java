package org.betterx.betterend.blocks;

import org.betterx.bclib.behaviours.BehaviourBuilders;
import org.betterx.bclib.behaviours.interfaces.BehaviourWaterPlant;
import org.betterx.bclib.interfaces.SurvivesOnSpecialGround;
import org.betterx.betterend.blocks.basis.EndUnderwaterPlantBlock;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public class CharniaBlock extends EndUnderwaterPlantBlock implements BehaviourWaterPlant {
    public CharniaBlock() {
        super(
                BehaviourBuilders.createWaterPlant()
        );
    }

    @Override
    public void appendHoverText(
            ItemStack itemStack,
            @Nullable BlockGetter blockGetter,
            List<Component> list,
            TooltipFlag tooltipFlag
    ) {
        super.appendHoverText(itemStack, blockGetter, list, tooltipFlag);
        SurvivesOnSpecialGround.appendHoverTextUnderwater(list);
    }

    @Override
    protected boolean isTerrain(BlockState state) {
        return state.isSolid();
    }
}
