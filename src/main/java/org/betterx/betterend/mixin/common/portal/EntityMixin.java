package org.betterx.betterend.mixin.common.portal;

import org.betterx.betterend.portal.TravelerState;
import org.betterx.betterend.portal.TravelingEntity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin implements TravelingEntity {

    private final TravelerState be_travelerState = TravelerState.init((net.minecraft.world.entity.Entity) (Object) this);

    public TravelerState be_getTravelerState() {
        return be_travelerState;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    void be_tick(CallbackInfo ci) {
        if (be_travelerState != null) be_travelerState.portalTick();
    }

    @Inject(method = "findDimensionEntryPoint", at = @At("HEAD"), cancellable = true)
    void be_findDimensionEntryPoint(ServerLevel serverLevel, CallbackInfoReturnable<PortalInfo> cir) {
//        if (be_travelerState != null) {
//            PortalInfo pi = be_travelerState.findDimensionEntryPoint(serverLevel);
//            if (pi != null) cir.setReturnValue(pi);
//        }
    }
}
