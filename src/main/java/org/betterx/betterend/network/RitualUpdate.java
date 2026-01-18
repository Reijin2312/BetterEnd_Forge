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
import net.minecraft.network.FriendlyByteBuf;

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
    }

    byte flags;
    BlockPos center;
    Direction.Axis axis;

    @Override
    protected void serializeDataOnServer(FriendlyByteBuf buf) {
        buf.writeBlockPos(center);
        BaseDataHandler.writeString(buf, axis.getName());
        buf.writeByte(flags);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void deserializeIncomingDataOnClient(FriendlyByteBuf buf, PacketSender responseSender) {
        center = buf.readBlockPos();
        axis = Direction.Axis.byName(BaseDataHandler.readString(buf));
        flags = buf.readByte();
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
                null
        );
    }
}

