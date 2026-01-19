package org.betterx.betterend;

import org.betterx.bclib.api.v2.dataexchange.DataExchangeAPI;
import org.betterx.bclib.api.v2.generator.BiomeDecider;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.betterx.bclib.api.v3.levelgen.features.BCLFeature;
import org.betterx.bclib.registry.RegistryBootstrap;
import org.betterx.betterend.advancements.BECriteria;
import org.betterx.betterend.api.BetterEndPlugin;
import org.betterx.betterend.commands.CommandRegistry;
import org.betterx.betterend.config.Configs;
import org.betterx.betterend.effects.EndPotions;
import org.betterx.betterend.effects.EndStatusEffects;
import org.betterx.betterend.integration.Integrations;
import org.betterx.betterend.network.RitualUpdate;
import org.betterx.betterend.recipe.builders.InfusionRecipe;
import org.betterx.betterend.registry.*;
import org.betterx.betterend.tab.CreativeTabs;
import org.betterx.betterend.util.BonemealPlants;
import org.betterx.betterend.util.LootTableUtil;
import org.betterx.betterend.world.generator.EndLandBiomeDecider;
import org.betterx.betterend.world.generator.GeneratorOptions;
import org.betterx.worlds.together.util.Logger;
import org.betterx.worlds.together.world.WorldConfig;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;

import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

import java.util.ServiceLoader;
import java.util.List;

@Mod(BetterEnd.MOD_ID)
public class BetterEnd {
    public static final String MOD_ID = "betterend";
    public static final Logger LOGGER = new Logger(MOD_ID);
    private static boolean bonemealInitialized = false;

    public BetterEnd() {
        // Ensure custom recipe serializers are registered before registry events fire
        InfusionRecipe.register();

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(EventPriority.HIGHEST, this::ensureBlocksLoaded);
        modBus.addListener(EndEntities::onRegister);
        modBus.addListener(EndEntities::onRegisterAttributes);
        modBus.addListener((RegisterEvent event) -> EndBlockEntities.register(event));
        modBus.addListener(EndAttributes::onRegister);
        modBus.addListener(EndEnchantments::onRegister);
        modBus.addListener(EndMenuTypes::onRegister);
        modBus.addListener(EndPoiTypes::onRegister);
        modBus.addListener(EndSounds::onRegister);
        modBus.addListener(EndStatusEffects::onRegister);
        modBus.addListener(EndPotions::onRegister);
        modBus.addListener(EndParticles::onRegister);
        modBus.addListener(EndStructures::onRegister);
        modBus.addListener(EventPriority.HIGHEST, this::registerFeatures);
        modBus.addListener(EventPriority.HIGHEST, this::ensureItemsLoaded);
        modBus.addListener(EventPriority.LOWEST, RegistryBootstrap::register);
        modBus.addListener(this::onCommonSetup);

        preloadParticles();
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        onInitialize();
    }

    public void onInitialize() {
        WorldConfig.registerModCache(MOD_ID);
        EndNumericProviders.register();
        EndPortals.loadPortals();
        EndMenuTypes.ensureStaticallyLoaded();
        EndBiomes.register();
        EndTags.register();
        EndPotions.register();
        InfusionRecipe.register();
        EndStructures.register();
        GeneratorOptions.init();
        LootTableUtil.init();
        CommandRegistry.register();
        EndParticles.ensureStaticallyLoadedServerside();
        BECriteria.register();
        ServiceLoader.load(BetterEndPlugin.class).forEach(BetterEndPlugin::register);
        Integrations.init();
        Configs.saveConfigs();

        if (GeneratorOptions.useNewGenerator()) {
            BiomeDecider.registerHighPriorityDecider(makeID("end_land"), new EndLandBiomeDecider());
        }

        BiomeAPI.registerEndBiomeModification((biomeID, biome) -> {
            if (!biomeID.equals(Biomes.THE_VOID.location())) {
                EndFeatures.addBiomeFeatures(biomeID, biome);
            }
        });

        BiomeAPI.onFinishingEndBiomeTags((biomeID, biome) -> {
            if (!biomeID.equals(Biomes.THE_VOID.location())) {
                EndStructures.addBiomeStructures(biomeID, biome);
            }
        });

        DataExchangeAPI.registerDescriptors(List.of(
                RitualUpdate.DESCRIPTOR
        ));

    }

    public static ResourceLocation makeID(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    private void ensureBlocksLoaded(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.BLOCK)) {
            return;
        }
        try {
            Class.forName("org.betterx.betterend.registry.EndBlocks");
        } catch (ClassNotFoundException ignored) {
        }
        if (!bonemealInitialized) {
            BonemealPlants.init();
            bonemealInitialized = true;
        }
    }

    private void ensureItemsLoaded(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.ITEM)) {
            return;
        }
        try {
            Class.forName("org.betterx.betterend.registry.EndItems");
        } catch (ClassNotFoundException ignored) {
        }
        try {
            Class.forName("org.betterx.betterend.registry.EndTemplates");
        } catch (ClassNotFoundException ignored) {
        }
        EndEntities.registerSpawnEggs();
        CreativeTabs.register();
    }

    private void registerFeatures(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.FEATURE)) {
            return;
        }
        EndFeatures.register();
        BCLFeature.registerForDatagen();
    }

    private void preloadParticles() {
        EndParticles.ensureStaticallyLoadedServerside();
    }
}
