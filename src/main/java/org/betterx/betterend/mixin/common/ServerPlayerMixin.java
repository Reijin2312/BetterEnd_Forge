package org.betterx.betterend.mixin.common;

import org.betterx.betterend.portal.TravelerState;
import org.betterx.betterend.portal.TravelingEntity;
import org.betterx.betterend.world.generator.GeneratorOptions;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }
    
    @Inject(method = "createEndPlatform", at = @At("HEAD"), cancellable = true)
    private void be_createEndSpawnPlatform(ServerLevel world, BlockPos centerPos, CallbackInfo info) {
        if (!GeneratorOptions.generateObsidianPlatform()) {
            info.cancel();
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void be_portalTick(CallbackInfo info) {
        TravelerState state = ((TravelingEntity) this).be_getTravelerState();
        if (state != null) {
            state.portalTick();
        }
    }

    @Inject(method = "findDimensionEntryPoint", at = @At("HEAD"), cancellable = true)
    protected void be_getTeleportTarget(ServerLevel destination, CallbackInfoReturnable<PortalInfo> info) {
        if (GeneratorOptions.changeSpawn() && destination.dimension() == Level.END) {
            BlockPos spawn = GeneratorOptions.getSpawn();
            Vec3 pos = new Vec3(spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5);
            info.setReturnValue(new PortalInfo(pos, Vec3.ZERO, 90.0F, 0.0F));
        }
    }

    @Shadow
    @Override
    protected abstract PortalInfo findDimensionEntryPoint(ServerLevel destination);

}
