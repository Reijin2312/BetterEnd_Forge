package org.betterx.betterend.client.render;

import org.betterx.bclib.items.elytra.BCLElytraItem;
import org.betterx.bclib.items.elytra.BCLElytraUtils;
import org.betterx.betterend.item.model.ArmoredElytraModel;
import org.betterx.betterend.registry.EndEntitiesRenders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;

public class ArmoredElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T, M> {
    private static final ResourceLocation VANILLA_WINGS = new ResourceLocation("minecraft", "textures/entity/elytra.png");
    private final ArmoredElytraModel<T> elytraModel;

    public ArmoredElytraLayer(RenderLayerParent<T, M> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent, entityModelSet);
        ArmoredElytraModel<T> model;
        try {
            model = new ArmoredElytraModel<>(entityModelSet.bakeLayer(EndEntitiesRenders.ARMORED_ELYTRA));
        } catch (IllegalArgumentException ex) {
            model = null;
        }
        elytraModel = model;
    }

    private ItemStack getElytraStack(T livingEntity) {
        ItemStack itemStack = null;
        if (BCLElytraUtils.slotProvider != null) {
            itemStack = BCLElytraUtils.slotProvider.getElytra(livingEntity, livingEntity::getItemBySlot);
        }
        if (itemStack == null
                || itemStack.isEmpty()
                || !(itemStack.getItem() instanceof ElytraItem || itemStack.getItem() instanceof BCLElytraItem)) {
            itemStack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
        }
        return itemStack;
    }

    private ResourceLocation getDefaultWingsTexture(ItemStack itemStack) {
        if (itemStack.getItem() instanceof BCLElytraItem) {
            return ((BCLElytraItem) itemStack.getItem()).getModelTexture();
        }
        return VANILLA_WINGS;
    }

    public void render(
            PoseStack poseStack,
            MultiBufferSource multiBufferSource,
            int i,
            T livingEntity,
            float f,
            float g,
            float h,
            float j,
            float k,
            float l
    ) {
        if (elytraModel == null) {
            return;
        }
        ItemStack itemStack = getElytraStack(livingEntity);
        if (itemStack.isEmpty()) {
            return;
        }

        boolean isVanillaElytra = itemStack.getItem() instanceof ElytraItem
                && !(itemStack.getItem() instanceof BCLElytraItem);
        boolean isCustomElytra = itemStack.getItem() instanceof BCLElytraItem;
        if (!isVanillaElytra && !isCustomElytra) {
            return;
        }

        ResourceLocation wingsTexture = getDefaultWingsTexture(itemStack);
        if (isVanillaElytra && livingEntity instanceof AbstractClientPlayer) {
            AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer) livingEntity;
            if (abstractClientPlayer.isElytraLoaded() && abstractClientPlayer.getElytraTextureLocation() != null) {
                wingsTexture = abstractClientPlayer.getElytraTextureLocation();
            } else if (abstractClientPlayer.isCapeLoaded()
                    && abstractClientPlayer.getCloakTextureLocation() != null
                    && abstractClientPlayer.isModelPartShown(PlayerModelPart.CAPE)) {
                wingsTexture = abstractClientPlayer.getCloakTextureLocation();
            }
        }

        poseStack.pushPose();
        poseStack.translate(0.0D, 0.0D, 0.125D);
        getParentModel().copyPropertiesTo(elytraModel);
        elytraModel.setupAnim(livingEntity, f, g, j, k, l);
        VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(
                multiBufferSource,
                RenderType.armorCutoutNoCull(wingsTexture),
                false,
                itemStack.hasFoil()
        );
        elytraModel.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }
}
