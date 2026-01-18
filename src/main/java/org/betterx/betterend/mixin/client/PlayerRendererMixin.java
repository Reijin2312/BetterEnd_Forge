package org.betterx.betterend.mixin.client;

import org.betterx.betterend.client.render.ArmoredElytraLayer;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public PlayerRendererMixin(
            EntityRendererProvider.Context context,
            PlayerModel<AbstractClientPlayer> entityModel,
            float f
    ) {
        super(context, entityModel, f);
    }

    @Inject(method = "<init>*", at = @At("TAIL"))
    public void be_addCustomLayer(EntityRendererProvider.Context context, boolean bl, CallbackInfo ci) {
        addLayer(new ArmoredElytraLayer<>(this, context.getModelSet()));
    }
}
