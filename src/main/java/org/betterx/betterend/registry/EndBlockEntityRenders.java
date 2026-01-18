package org.betterx.betterend.registry;

import org.betterx.betterend.client.render.PedestalItemRenderer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;

@OnlyIn(Dist.CLIENT)
public class EndBlockEntityRenders {
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(EndBlockEntities.PEDESTAL, PedestalItemRenderer::new);
        event.registerBlockEntityRenderer(EndBlockEntities.ETERNAL_PEDESTAL, PedestalItemRenderer::new);
        event.registerBlockEntityRenderer(EndBlockEntities.INFUSION_PEDESTAL, PedestalItemRenderer::new);
    }
}
