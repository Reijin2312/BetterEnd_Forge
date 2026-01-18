package org.betterx.betterend.effects;

import org.betterx.bclib.mixin.common.PotionBrewingAccessor;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndItems;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

import net.minecraftforge.registries.RegisterEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class EndPotions {
    private static final Map<ResourceLocation, Potion> POTIONS = new LinkedHashMap<>();

    public final static Potion END_VEIL = registerPotion("end_veil", EndStatusEffects.END_VEIL, 3600);
    public final static Potion LONG_END_VEIL = registerPotion("long_end_veil", EndStatusEffects.END_VEIL, 9600);

    public static Potion registerPotion(String name, MobEffect effect, int duration) {
        return registerPotion(name, new Potion(name, new MobEffectInstance(effect, duration)));
    }

    public static Potion registerPotion(String name, Potion potion) {
        POTIONS.putIfAbsent(BetterEnd.makeID(name), potion);
        return potion;
    }

    public static void register() {
        PotionBrewingAccessor.callAddMix(Potions.AWKWARD, EndItems.ENDER_DUST, END_VEIL);
        PotionBrewingAccessor.callAddMix(END_VEIL, Items.REDSTONE, LONG_END_VEIL);
        PotionBrewingAccessor.callAddMix(Potions.AWKWARD, EndBlocks.MURKWEED.asItem(), Potions.NIGHT_VISION);
    }

    public static void onRegister(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.POTION)) {
            return;
        }
        event.register(Registries.POTION, helper -> {
            POTIONS.forEach(helper::register);
            POTIONS.clear();
        });
    }
}
