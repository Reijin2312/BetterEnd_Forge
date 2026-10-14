package org.betterx.betterend.effects;

import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.effects.status.EndVeilEffect;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

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

    public final static MobEffect END_VEIL = registerEffect("end_veil", new EndVeilEffect());

    public static <E extends MobEffect> MobEffect registerEffect(String name, E effect) {
        return Registry.register(BuiltInRegistries.MOB_EFFECT, BetterEnd.makeID(name), effect);
    }
}
