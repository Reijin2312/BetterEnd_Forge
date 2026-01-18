package org.betterx.betterend.registry;

import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.entity.model.*;
import org.betterx.betterend.entity.render.*;
import org.betterx.betterend.item.model.*;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.EntityType;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class EndEntitiesRenders {
    public static final ModelLayerLocation DRAGONFLY_MODEL = registerMain("dragonfly");
    public static final ModelLayerLocation END_SLIME_SHELL_MODEL = registerMain("endslime_shell");
    public static final ModelLayerLocation END_SLIME_MODEL = registerMain("endslime");
    public static final ModelLayerLocation END_FISH_MODEL = registerMain("endfish");
    public static final ModelLayerLocation CUBOZOA_MODEL = registerMain("cubozoa");
    public static final ModelLayerLocation SILK_MOTH_MODEL = registerMain("silkmoth");
    public static final ModelLayerLocation TEST_MODEL = registerMain("test");

    public static final ModelLayerLocation ARMORED_ELYTRA = registerMain("armored_elytra");
    public static final ModelLayerLocation CRYSTALITE_CHESTPLATE = registerMain("crystalite_chestplate");
    public static final ModelLayerLocation CRYSTALITE_CHESTPLATE_THIN = registerMain("crystalite_chestplate_thin");
    public static final ModelLayerLocation CRYSTALITE_HELMET = registerMain("crystalite_helmet");
    public static final ModelLayerLocation CRYSTALITE_LEGGINGS = registerMain("crystalite_leggings");
    public static final ModelLayerLocation CRYSTALITE_BOOTS = registerMain("crystalite_boots");

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        register(event, EndEntities.DRAGONFLY.type(), RendererEntityDragonfly::new);
        register(event, EndEntities.END_SLIME.type(), RendererEntityEndSlime::new);
        register(event, EndEntities.END_FISH.type(), RendererEntityEndFish::new);
        register(event, EndEntities.SHADOW_WALKER.type(), RendererEntityShadowWalker::new);
        register(event, EndEntities.CUBOZOA.type(), RendererEntityCubozoa::new);
        register(event, EndEntities.SILK_MOTH.type(), SilkMothEntityRenderer::new);
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DRAGONFLY_MODEL, DragonflyEntityModel::getTexturedModelData);
        event.registerLayerDefinition(
                END_SLIME_SHELL_MODEL,
                EndSlimeEntityModel::getShellOnlyTexturedModelData
        );
        event.registerLayerDefinition(END_SLIME_MODEL, EndSlimeEntityModel::getCompleteTexturedModelData);
        event.registerLayerDefinition(END_FISH_MODEL, EndFishEntityModel::getTexturedModelData);
        event.registerLayerDefinition(CUBOZOA_MODEL, CubozoaEntityModel::getTexturedModelData);
        event.registerLayerDefinition(SILK_MOTH_MODEL, SilkMothEntityModel::getTexturedModelData);

        event.registerLayerDefinition(ARMORED_ELYTRA, ArmoredElytraModel::getTexturedModelData);
        event.registerLayerDefinition(
                CRYSTALITE_CHESTPLATE,
                CrystaliteChestplateModel::getRegularTexturedModelData
        );
        event.registerLayerDefinition(
                CRYSTALITE_CHESTPLATE_THIN,
                CrystaliteChestplateModel::getThinTexturedModelData
        );
        event.registerLayerDefinition(CRYSTALITE_HELMET, CrystaliteHelmetModel::getTexturedModelData);
        event.registerLayerDefinition(CRYSTALITE_LEGGINGS, CrystaliteLeggingsModel::getTexturedModelData);
        event.registerLayerDefinition(CRYSTALITE_BOOTS, CrystaliteBootsModel::getTexturedModelData);
    }

    private static void register(EntityRenderersEvent.RegisterRenderers event, EntityType<?> type, Function<Context, MobRenderer> renderer) {
        event.registerEntityRenderer(type, renderer::apply);
    }

    private static ModelLayerLocation registerMain(String id) {
        return new ModelLayerLocation(BetterEnd.makeID(id), "main");
    }
}
