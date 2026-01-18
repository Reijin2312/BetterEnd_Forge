package org.betterx.betterend.particle;

import org.betterx.betterend.registry.EndParticles;
import org.betterx.ui.ColorUtil;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class InfusionParticleType extends ParticleType<InfusionParticleType> implements ParticleOptions {
    public static final Codec<InfusionParticleType> CODEC = ItemStack.CODEC.xmap(itemStack -> new InfusionParticleType(
            EndParticles.INFUSION,
            itemStack
    ), infusionParticleType -> infusionParticleType.itemStack);

    @SuppressWarnings("deprecation")
    public static final ParticleOptions.Deserializer<InfusionParticleType> PARAMETERS_FACTORY = new ParticleOptions.Deserializer<InfusionParticleType>() {
        public InfusionParticleType fromCommand(
                ParticleType<InfusionParticleType> particleType,
                StringReader stringReader
        ) throws CommandSyntaxException {
            stringReader.expect(' ');
            ItemParser.ItemResult itemResult = ItemParser.parseForItem(
                    BuiltInRegistries.ITEM.asLookup(),
                    stringReader
            );
            ItemStack itemStack = new ItemInput(itemResult.item(), itemResult.nbt()).createItemStack(1, false);

            return new InfusionParticleType(particleType, itemStack);
        }

        public InfusionParticleType fromNetwork(
                ParticleType<InfusionParticleType> particleType,
                FriendlyByteBuf packetByteBuf
        ) {
            return new InfusionParticleType(particleType, packetByteBuf.readItem());
        }
    };

    private final ParticleType<InfusionParticleType> type;
    private final ItemStack itemStack;

    public InfusionParticleType(ParticleType<InfusionParticleType> particleType, ItemStack stack) {
        super(true, PARAMETERS_FACTORY);
        this.type = particleType;
        this.itemStack = stack;
    }

    public InfusionParticleType(ItemStack stack) {
        this(EndParticles.INFUSION, stack);
    }

    @OnlyIn(Dist.CLIENT)
    public float[] getPalette() {
        int color = ColorUtil.extractColor(itemStack.getItem());
        return ColorUtil.toFloatArray(color);
    }

    @Override
    public ParticleType<?> getType() {
        return this.type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeItem(itemStack);
    }

    @Override
    public String writeToString() {
        return BuiltInRegistries.PARTICLE_TYPE.getKey(this).toString();
    }

    @Override
    public Codec<InfusionParticleType> codec() {
        return CODEC;
    }
}

