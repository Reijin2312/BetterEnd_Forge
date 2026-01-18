package org.betterx.betterend.effects;

import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.effects.status.EndVeilEffect;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import net.minecraftforge.registries.RegisterEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class EndStatusEffects {
    public final static MobEffectInstance CRYSTALITE_HEALTH_REGEN = new MobEffectInstance(
            MobEffects.REGENERATION,
            80,
            0,
            true,
            false,
            true
    );
    public final static MobEffectInstance CRYSTALITE_DIG_SPEED = new MobEffectInstance(
            MobEffects.DIG_SPEED,
            80,
            0,
            true,
            false,
            true
    );
    public final static MobEffectInstance CRYSTALITE_MOVE_SPEED = new MobEffectInstance(
            MobEffects.MOVEMENT_SPEED,
            80,
            0,
            true,
            false,
            true
    );

    private static final Map<ResourceLocation, MobEffect> EFFECTS = new LinkedHashMap<>();

    public final static MobEffect END_VEIL = registerEffect("end_veil", new EndVeilEffect());

    public static <E extends MobEffect> MobEffect registerEffect(String name, E effect) {
        EFFECTS.putIfAbsent(BetterEnd.makeID(name), effect);
        return effect;
    }

    public static void onRegister(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.MOB_EFFECT)) {
            return;
        }
        event.register(Registries.MOB_EFFECT, helper -> {
            EFFECTS.forEach(helper::register);
            EFFECTS.clear();
        });
    }
}
