package org.betterx.betterend.mixin.client;

import org.betterx.betterend.client.effects.InfusionHint;
import org.betterx.betterend.client.effects.EternalHint;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.GameRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "bobHurt", at = @At("RETURN"))
    private void betterend_wobbleForBuildHint(PoseStack poseStack, float partialTick, CallbackInfo info) {
        float[] wobble = InfusionHint.wobble(partialTick);
        if (wobble == null) wobble = EternalHint.wobble(partialTick);
        if (wobble == null) return;

        poseStack.mulPose(Axis.ZP.rotationDegrees(wobble[0]));
        poseStack.mulPose(Axis.XP.rotationDegrees(wobble[1]));
    }
}
