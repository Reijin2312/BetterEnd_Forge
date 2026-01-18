package org.betterx.betterend.mixin.common.portal;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayer.class)
public interface ServerPlayerAccessor {
    @Accessor("isChangingDimension")
    void be_setChangingDimension(boolean changing);

    @Accessor("lastSentExp")
    void be_setLastSentExp(int value);

    @Accessor("lastSentHealth")
    void be_setLastSentHealth(float value);

    @Accessor("lastSentFood")
    void be_setLastSentFood(int value);

    @Invoker("triggerDimensionChangeTriggers")
    void be_triggerDimensionChangeTriggers(ServerLevel level);
}
