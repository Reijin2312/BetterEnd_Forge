package org.betterx.betterend.registry;

import org.betterx.betterend.item.model.CrystaliteArmorRenderer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EndModelProviders {
    public final static void register() {
        CrystaliteArmorRenderer.getInstance();
    }
}
