package org.betterx.betterend.item;

import org.betterx.betterend.effects.EndStatusEffects;
import org.betterx.betterend.interfaces.MobEffectApplier;
import org.betterx.betterend.registry.EndItems;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public class CrystaliteBoots extends CrystaliteArmor implements MobEffectApplier {

    public CrystaliteBoots() {
        super(Type.BOOTS, EndItems.makeEndItemSettings().rarity(Rarity.RARE));
    }

    @Override
    public void applyEffect(LivingEntity owner) {
        if ((owner.tickCount & 63) == 0) {
            owner.addEffect(new MobEffectInstance(EndStatusEffects.CRYSTALITE_MOVE_SPEED));
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag tooltip) {
        super.appendHoverText(stack, level, lines, tooltip);
        lines.add(1, Component.empty());
        lines.add(2, BOOTS_DESC);
    }
}

