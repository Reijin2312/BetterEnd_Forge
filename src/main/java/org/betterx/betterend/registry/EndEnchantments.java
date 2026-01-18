package org.betterx.betterend.registry;

import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.config.Configs;
import org.betterx.betterend.effects.enchantment.EndVeilEnchantment;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

import net.minecraftforge.registries.RegisterEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class EndEnchantments {
    private static final Map<ResourceLocation, Enchantment> ENCHANTMENTS = new LinkedHashMap<>();

    public final static Enchantment END_VEIL = registerEnchantment("end_veil", new EndVeilEnchantment());

    public static Enchantment registerEnchantment(String name, Enchantment enchantment) {
        if (!Configs.ENCHANTMENT_CONFIG.getBooleanRoot(name, true)) {
            return enchantment;
        }
        ENCHANTMENTS.putIfAbsent(BetterEnd.makeID(name), enchantment);
        return enchantment;
    }

    public static void register() {
    }

    public static void onRegister(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.ENCHANTMENT)) {
            return;
        }
        event.register(Registries.ENCHANTMENT, helper -> {
            ENCHANTMENTS.forEach(helper::register);
            ENCHANTMENTS.clear();
        });
    }
}
