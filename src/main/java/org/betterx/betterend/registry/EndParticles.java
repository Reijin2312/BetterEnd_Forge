package org.betterx.betterend.registry;

import org.betterx.bclib.particles.BCLParticleType;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.particle.InfusionParticleType;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

import net.minecraftforge.registries.RegisterEvent;

public class EndParticles {
    public static SimpleParticleType GLOWING_SPHERE;
    public static SimpleParticleType PORTAL_SPHERE;
    public static ParticleType<InfusionParticleType> INFUSION;

    public static SimpleParticleType SULPHUR_PARTICLE;
    public static SimpleParticleType GEYSER_PARTICLE;
    public static SimpleParticleType SNOWFLAKE;
    public static SimpleParticleType AMBER_SPHERE;
    public static SimpleParticleType BLACK_SPORE;
    public static SimpleParticleType TENANEA_PETAL;
    public static SimpleParticleType JUNGLE_SPORE;
    public static SimpleParticleType FIREFLY;
    public static SimpleParticleType SMARAGDANT;
    public static SimpleParticleType INFUSION_MIST;
    public static SimpleParticleType INFUSION_MIST_NORTH;

    private static boolean registered = false;

    public static void onRegister(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.PARTICLE_TYPE)) {
            return;
        }
        registerTypes();
    }

    private static void registerTypes() {
        if (registered) {
            return;
        }
        registered = true;

        GLOWING_SPHERE = register("glowing_sphere");
        PORTAL_SPHERE = register("portal_sphere");
        INFUSION = BCLParticleType.register(
                BetterEnd.makeID("infusion"),
                InfusionParticleType.PARAMETERS_FACTORY,
                InfusionParticleType.CODEC
        );

        SULPHUR_PARTICLE = register("sulphur_particle");
        GEYSER_PARTICLE = registerFar("geyser_particle");
        SNOWFLAKE = register("snowflake");
        AMBER_SPHERE = register("amber_sphere");
        BLACK_SPORE = register("black_spore");
        TENANEA_PETAL = register("tenanea_petal");
        JUNGLE_SPORE = register("jungle_spore");
        FIREFLY = register("firefly");
        SMARAGDANT = register("smaragdant_particle");
        INFUSION_MIST = register("infusion_mist");
        INFUSION_MIST_NORTH = register("infusion_mist_north");
    }

    private static SimpleParticleType register(String name) {
        return BCLParticleType.register(BetterEnd.makeID(name));
    }

    private static SimpleParticleType registerFar(String name) {
        return BCLParticleType.register(BetterEnd.makeID(name), true);
    }

    public static void ensureStaticallyLoadedServerside() {
        registerTypes();
    }
}
