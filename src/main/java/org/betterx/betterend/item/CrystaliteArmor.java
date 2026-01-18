package org.betterx.betterend.item;

import org.betterx.bclib.client.render.HumanoidArmorRenderer;
import org.betterx.bclib.items.BaseArmorItem;
import org.betterx.betterend.effects.EndStatusEffects;
import org.betterx.betterend.item.material.EndArmorMaterial;
import org.betterx.betterend.item.model.CrystaliteArmorRenderer;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class CrystaliteArmor extends BaseArmorItem {

    public final static MutableComponent CHEST_DESC;
    public final static MutableComponent BOOTS_DESC;
    private boolean clientInitDone;

    public CrystaliteArmor(Type type, Properties settings) {
        super(EndArmorMaterial.CRYSTALITE, type, settings);
    }

    public static boolean hasFullSet(LivingEntity owner) {
        for (ItemStack armorStack : owner.getArmorSlots()) {
            if (!(armorStack.getItem() instanceof CrystaliteArmor)) {
                return false;
            }
        }
        return true;
    }

    public static void applySetEffect(LivingEntity owner) {
        if ((owner.tickCount & 63) == 0) {
            owner.addEffect(new MobEffectInstance(EndStatusEffects.CRYSTALITE_HEALTH_REGEN));
        }
    }

    static {
        Style descStyle = Style.EMPTY.applyFormats(ChatFormatting.DARK_AQUA, ChatFormatting.ITALIC);
        CHEST_DESC = Component.translatable("tooltip.armor.crystalite_chest");
        CHEST_DESC.setStyle(descStyle);
        BOOTS_DESC = Component.translatable("tooltip.armor.crystalite_boots");
        BOOTS_DESC.setStyle(descStyle);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        if (clientInitDone) {
            return;
        }
        clientInitDone = true;
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(
                    LivingEntity livingEntity,
                    ItemStack stack,
                    EquipmentSlot slot,
                    HumanoidModel<?> original
            ) {
                CrystaliteArmorRenderer renderer = CrystaliteArmorRenderer.getInstance();
                HumanoidModel<LivingEntity> model = renderer.modelFor(livingEntity, slot);
                if (model == null) return original;
                @SuppressWarnings("unchecked")
                HumanoidModel<LivingEntity> originalTyped = (HumanoidModel<LivingEntity>) original;
                originalTyped.copyPropertiesTo(model);
                if (model instanceof HumanoidArmorRenderer.CopyExtraState copy) {
                    copy.copyPropertiesFrom(originalTyped);
                }
                return model;
            }
        });
    }
}
