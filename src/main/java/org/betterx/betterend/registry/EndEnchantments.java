package org.betterx.betterend.registry;

import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.config.Configs;
import org.betterx.betterend.effects.enchantment.EndVeilEnchantment;
import org.betterx.betterend.effects.enchantment.ResonanceEnchantment;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.Enchantment;

public class EndEnchantments {
    public final static Enchantment END_VEIL = registerEnchantment("end_veil", new EndVeilEnchantment());
    public final static Enchantment RESONANCE = registerEnchantment("resonance", new ResonanceEnchantment());

    public static Enchantment registerEnchantment(String name, Enchantment enchantment) {
        if (!Configs.ENCHANTMENT_CONFIG.getBooleanRoot(name, true)) {
            return enchantment;
        }
        return Registry.register(BuiltInRegistries.ENCHANTMENT, BetterEnd.makeID(name), enchantment);
    }

    public static void register() {
    }
}
