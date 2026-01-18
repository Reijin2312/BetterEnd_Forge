package org.betterx.betterend.item.tool;

import org.betterx.bclib.items.tool.BasePickaxeItem;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class EndPickaxe extends BasePickaxeItem {
    public EndPickaxe(Tier material, int attackDamage, float attackSpeed, Properties settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(Blocks.END_STONE) && this.getTier().getLevel() > 2) {
            return this.speed * 3;
        }
        return super.getDestroySpeed(stack, state);
    }
}
