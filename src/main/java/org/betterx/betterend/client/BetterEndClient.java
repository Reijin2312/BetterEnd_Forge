package org.betterx.betterend.client;

import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.client.render.BetterEndSkyEffect;
import org.betterx.betterend.events.ItemTooltipCallback;
import org.betterx.betterend.interfaces.MultiModelItem;
import org.betterx.betterend.item.CrystaliteArmor;
import org.betterx.betterend.registry.*;
import org.betterx.betterend.world.generator.GeneratorOptions;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = BetterEnd.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BetterEndClient {
    private static final ModelResourceLocation CHECK_FLOWER_ID = new ModelResourceLocation(
            new ResourceLocation("minecraft", "chorus_flower"),
            "inventory"
    );
    private static final ModelResourceLocation CHECK_PLANT_ID = new ModelResourceLocation(
            new ResourceLocation("minecraft", "chorus_plant"),
            "inventory"
    );
    private static final ModelResourceLocation TO_LOAD_FLOWER_ID = new ModelResourceLocation(
            BetterEnd.makeID("custom_chorus_flower"),
            "inventory"
    );
    private static final ModelResourceLocation TO_LOAD_PLANT_ID = new ModelResourceLocation(
            BetterEnd.makeID("custom_chorus_plant"),
            "inventory"
    );

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EndBlocks.ensureStaticallyLoaded();
            EndBlocks.getWoodTypes().forEach(Sheets::addWoodType);

            EndModelProviders.register();
            MultiModelItem.register();
            ClientOptions.init();
            EndScreens.register();
            registerTooltips();
            MinecraftForge.EVENT_BUS.addListener(BetterEndClient::onItemTooltip);
        });
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        EndParticles.registerProviders(event);
    }

    @SubscribeEvent
    public static void registerDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
        if (!ClientOptions.isCustomSky()) {
            return;
        }
        event.register(new ResourceLocation("minecraft", "the_end"), new BetterEndSkyEffect());
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        EndEntitiesRenders.registerRenderers(event);
        EndBlockEntityRenders.registerRenderers(event);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        EndEntitiesRenders.registerLayerDefinitions(event);
    }

    @SubscribeEvent
    public static void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
        if (!GeneratorOptions.changeChorusPlant()) {
            return;
        }
        event.register(TO_LOAD_FLOWER_ID);
        event.register(TO_LOAD_PLANT_ID);
    }

    @SubscribeEvent
    public static void onModifyBakedModels(ModelEvent.ModifyBakingResult event) {
        Map<ResourceLocation, net.minecraft.client.resources.model.BakedModel> models = event.getModels();
        if (GeneratorOptions.changeChorusPlant()) {
            var flowerModel = models.get(TO_LOAD_FLOWER_ID);
            var plantModel = models.get(TO_LOAD_PLANT_ID);
            if (flowerModel != null) {
                models.put(CHECK_FLOWER_ID, flowerModel);
            }
            if (plantModel != null) {
                models.put(CHECK_PLANT_ID, plantModel);
            }
        }
    }

    private static void onItemTooltip(ItemTooltipEvent event) {
        ItemTooltipCallback.fire(event.getEntity(), event.getItemStack(), event.getFlags(), event.getToolTip());
    }

    public static void registerTooltips() {
        ItemTooltipCallback.register((player, stack, context, lines) -> {
            if (stack.getItem() instanceof CrystaliteArmor) {
                boolean hasSet = false;
                if (player != null) {
                    hasSet = CrystaliteArmor.hasFullSet(player);
                }
                MutableComponent setDesc = Component.translatable("tooltip.armor.crystalite_set");

                setDesc.setStyle(Style.EMPTY.applyFormats(
                        hasSet ? ChatFormatting.BLUE : ChatFormatting.DARK_GRAY,
                        ChatFormatting.ITALIC
                ));
                lines.add(Component.empty());
                lines.add(setDesc);
            }
        });
    }
}
