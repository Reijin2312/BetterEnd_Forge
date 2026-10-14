package org.betterx.betterend.network;

import org.betterx.bclib.api.v2.dataexchange.BaseDataHandler;
import org.betterx.bclib.api.v2.dataexchange.DataHandler;
import org.betterx.bclib.api.v2.dataexchange.DataHandlerDescriptor;
import org.betterx.bclib.api.v2.dataexchange.PacketSender;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.rituals.EternalRitual;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RitualUpdate extends DataHandler.FromServer {
    public static final DataHandlerDescriptor DESCRIPTOR = new DataHandlerDescriptor(
            BetterEnd.makeID("ritual_update"),
            RitualUpdate::new,
            false,
            false
    );

    public RitualUpdate() {
        super(DESCRIPTOR.IDENTIFIER);
    }

    private static final byte ACTIVE_FLAG = 1;
    private static final byte WILL_ACTIVATE_FLAG = 2;

    public RitualUpdate(EternalRitual ritual) {
        this();
        this.center = ritual.getCenter();
        this.axis = ritual.getAxis();

        if (ritual.isActive()) {
            this.flags |= ACTIVE_FLAG;
        }
        if (ritual.willActivate()) {
            this.flags |= WILL_ACTIVATE_FLAG;
        }
        Item keyItem = ritual.getKeyItem();
        if (keyItem != null) {
            this.keyItemId = BuiltInRegistries.ITEM.getKey(keyItem);
        }
        this.pedestalItemMask = ritual.getPedestalItemMask();
    }

    byte flags;
    BlockPos center;
    Direction.Axis axis;
    ResourceLocation keyItemId;
    int pedestalItemMask;

    @Override
    protected void serializeDataOnServer(FriendlyByteBuf buf) {
        buf.writeBlockPos(center);
        BaseDataHandler.writeString(buf, axis.getName());
        buf.writeByte(flags);
        buf.writeBoolean(keyItemId != null);
        if (keyItemId != null) {
            buf.writeResourceLocation(keyItemId);
        }
        buf.writeByte(pedestalItemMask);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void deserializeIncomingDataOnClient(FriendlyByteBuf buf, PacketSender responseSender) {
        center = buf.readBlockPos();
        axis = Direction.Axis.byName(BaseDataHandler.readString(buf));
        flags = buf.readByte();
        keyItemId = buf.readBoolean() ? buf.readResourceLocation() : null;
        pedestalItemMask = buf.readUnsignedByte();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void runOnClientGameThread(Minecraft client) {
        EternalRitual.updateActiveStateOnPedestals(
                center,
                axis,
                (flags & ACTIVE_FLAG) != 0,
                (flags & WILL_ACTIVATE_FLAG) != 0,
                client.level,
                null,
                keyItemId == null ? null : BuiltInRegistries.ITEM.get(keyItemId),
                pedestalItemMask
        );
    }
}
