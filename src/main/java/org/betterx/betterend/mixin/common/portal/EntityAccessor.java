package org.betterx.betterend.mixin.common.portal;

import net.minecraft.world.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("isInsidePortal")
    void be_setInsidePortal(boolean insidePortal);

    @Invoker("unsetRemoved")
    void be_unsetRemoved();

    @Invoker("removeAfterChangingDimensions")
    void be_removeAfterChangingDimensions();
}
