package org.betterx.betterend.client.particle;

import org.betterx.betterend.particle.*;
import org.betterx.betterend.registry.EndParticles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

/** Client-only bindings between the common particle types and their render factories. */
@Environment(EnvType.CLIENT)
public final class EndParticleFactories {
    private EndParticleFactories() {
    }

    public static void register() {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        registry.register(EndParticles.GLOWING_SPHERE, ParticleGlowingSphere.FactoryGlowingSphere::new);
        registry.register(EndParticles.PORTAL_SPHERE, PaticlePortalSphere.FactoryPortalSphere::new);
        registry.register(EndParticles.INFUSION, InfusionParticle.InfusionFactory::new);
        registry.register(EndParticles.SULPHUR_PARTICLE, ParticleSulphur.FactorySulphur::new);
        registry.register(EndParticles.GEYSER_PARTICLE, ParticleGeyser.FactoryGeyser::new);
        registry.register(EndParticles.SNOWFLAKE, ParticleSnowflake.FactorySnowflake::new);
        registry.register(EndParticles.AMBER_SPHERE, ParticleGlowingSphere.FactoryGlowingSphere::new);
        registry.register(EndParticles.BLACK_SPORE, ParticleBlackSpore.FactoryBlackSpore::new);
        registry.register(EndParticles.TENANEA_PETAL, ParticleTenaneaPetal.FactoryTenaneaPetal::new);
        registry.register(EndParticles.JUNGLE_SPORE, ParticleJungleSpore.FactoryJungleSpore::new);
        registry.register(EndParticles.FIREFLY, FireflyParticle.FireflyParticleFactory::new);
        registry.register(EndParticles.SMARAGDANT, SmaragdantParticle.SmaragdantParticleFactory::new);
        registry.register(EndParticles.INFUSION_MIST, InfusionMistParticle.FactoryInfusionMist::new);
        registry.register(
                EndParticles.INFUSION_MIST_NORTH,
                sprites -> new InfusionMistParticle.FactoryInfusionMist(sprites, InfusionMistParticle.NORTH_RGB)
        );
    }
}
