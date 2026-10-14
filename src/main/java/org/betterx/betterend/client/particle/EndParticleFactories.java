package org.betterx.betterend.client.particle;

import org.betterx.betterend.particle.*;
import org.betterx.betterend.registry.EndParticles;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;

/** Client-only bindings between the common particle types and their render factories. */
@OnlyIn(Dist.CLIENT)
public final class EndParticleFactories {
    private EndParticleFactories() {
    }

    public static void register(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(EndParticles.GLOWING_SPHERE, ParticleGlowingSphere.FactoryGlowingSphere::new);
        event.registerSpriteSet(EndParticles.PORTAL_SPHERE, PaticlePortalSphere.FactoryPortalSphere::new);
        event.registerSpriteSet(EndParticles.INFUSION, InfusionParticle.InfusionFactory::new);
        event.registerSpriteSet(EndParticles.SULPHUR_PARTICLE, ParticleSulphur.FactorySulphur::new);
        event.registerSpriteSet(EndParticles.GEYSER_PARTICLE, ParticleGeyser.FactoryGeyser::new);
        event.registerSpriteSet(EndParticles.SNOWFLAKE, ParticleSnowflake.FactorySnowflake::new);
        event.registerSpriteSet(EndParticles.AMBER_SPHERE, ParticleGlowingSphere.FactoryGlowingSphere::new);
        event.registerSpriteSet(EndParticles.BLACK_SPORE, ParticleBlackSpore.FactoryBlackSpore::new);
        event.registerSpriteSet(EndParticles.TENANEA_PETAL, ParticleTenaneaPetal.FactoryTenaneaPetal::new);
        event.registerSpriteSet(EndParticles.JUNGLE_SPORE, ParticleJungleSpore.FactoryJungleSpore::new);
        event.registerSpriteSet(EndParticles.FIREFLY, FireflyParticle.FireflyParticleFactory::new);
        event.registerSpriteSet(EndParticles.SMARAGDANT, SmaragdantParticle.SmaragdantParticleFactory::new);
        event.registerSpriteSet(EndParticles.INFUSION_MIST, InfusionMistParticle.FactoryInfusionMist::new);
        event.registerSpriteSet(
                EndParticles.INFUSION_MIST_NORTH,
                sprites -> new InfusionMistParticle.FactoryInfusionMist(sprites, InfusionMistParticle.NORTH_RGB)
        );
    }
}
