package org.betterx.betterend.registry;

import org.betterx.bclib.recipes.SmithingTemplates;
import org.betterx.betterend.BetterEnd;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class EndTemplates {
    static final ResourceLocation EMPTY_SLOT_STICK = BetterEnd.makeID("item/empty_slot_stick");
    static final ResourceLocation EMPTY_SLOT_HANDLE = BetterEnd.makeID("item/empty_slot_handle");
    static final ResourceLocation EMPTY_SLOT_SWORD_HANDLE = BetterEnd.makeID("item/empty_slot_sword_handle");
    static final ResourceLocation EMPTY_SLOT_SWORD_BLADE = BetterEnd.makeID("item/empty_slot_sword_blade");
    static final ResourceLocation EMPTY_SLOT_PLATE = BetterEnd.makeID("item/empty_slot_plate");
    static final ResourceLocation EMPTY_SLOT_HAMMER = BetterEnd.makeID("item/empty_slot_hammer");

    static final ResourceLocation EMPTY_SLOT_HAMMER_HEAD = BetterEnd.makeID("item/empty_slot_hammer_head");
    static final ResourceLocation EMPTY_SLOT_PICKAXE_HEAD = BetterEnd.makeID("item/empty_slot_pickaxe_head");
    static final ResourceLocation EMPTY_SLOT_AXE_HEAD = BetterEnd.makeID("item/empty_slot_axe_head");
    static final ResourceLocation EMPTY_SLOT_HOE_HEAD = BetterEnd.makeID("item/empty_slot_hoe_head");
    static final ResourceLocation EMPTY_SLOT_SHOVEL_HEAD = BetterEnd.makeID("item/empty_slot_shovel_head");
    static final ResourceLocation EMPTY_SLOT_ANVIL = BetterEnd.makeID("item/empty_slot_anvil");
    static final ResourceLocation EMPTY_SLOT_ELYTRA = BetterEnd.makeID("item/empty_slot_elytra");


    public static final SmithingTemplateItem HANDLE_ATTACHMENT = EndItems
            .getItemRegistry()
            .registerSmithingTemplateItem(
                    BetterEnd.makeID("handle_attachment"),
                    List.of(
                            EMPTY_SLOT_HAMMER_HEAD,
                            EMPTY_SLOT_PICKAXE_HEAD,
                            EMPTY_SLOT_AXE_HEAD,
                            EMPTY_SLOT_HOE_HEAD,
                            EMPTY_SLOT_SHOVEL_HEAD
                    ),
                    List.of(EMPTY_SLOT_STICK)
            );
    public static final SmithingTemplateItem LEATHER_HANDLE_ATTACHMENT = EndItems
            .getItemRegistry()
            .registerSmithingTemplateItem(
                    BetterEnd.makeID("leather_handle_attachment"),
                    List.of(
                            EMPTY_SLOT_HAMMER_HEAD,
                            EMPTY_SLOT_PICKAXE_HEAD,
                            EMPTY_SLOT_AXE_HEAD,
                            EMPTY_SLOT_HOE_HEAD,
                            EMPTY_SLOT_SHOVEL_HEAD
                    ),
                    List.of(EMPTY_SLOT_HANDLE)
            );

    public static final SmithingTemplateItem TOOL_ASSEMBLY = EndItems
            .getItemRegistry()
            .registerSmithingTemplateItem(
                    BetterEnd.makeID("tool_assembly"),
                    List.of(
                            EMPTY_SLOT_SWORD_BLADE
                    ),
                    List.of(EMPTY_SLOT_SWORD_HANDLE)
            );

    public static final SmithingTemplateItem PLATE_UPGRADE = EndItems
            .getItemRegistry()
            .registerSmithingTemplateItem(
                    BetterEnd.makeID("plate_upgrade"),
                    SmithingTemplates.ARMOR,
                    List.of(EMPTY_SLOT_PLATE)
            );

    public static final SmithingTemplateItem THALLASIUM_UPGRADE = EndItems
            .getItemRegistry()
            .registerSmithingTemplateItem(
                    BetterEnd.makeID("thallasium_upgrade"),
                    List.of(EMPTY_SLOT_STICK),
                    List.of(SmithingTemplates.EMPTY_SLOT_INGOT)
            );

    public static final SmithingTemplateItem TERMINITE_UPGRADE = EndItems
            .getItemRegistry()
            .registerSmithingTemplateItem(
                    BetterEnd.makeID("terminite_upgrade"),
                    List.of(EMPTY_SLOT_ANVIL, EMPTY_SLOT_STICK),
                    List.of(SmithingTemplates.EMPTY_SLOT_INGOT)
            );

    public static final SmithingTemplateItem AETERNIUM_UPGRADE = EndItems
            .getItemRegistry()
            .registerSmithingTemplateItem(
                    BetterEnd.makeID("aeternium_upgrade"),
                    List.of(EMPTY_SLOT_ANVIL, EMPTY_SLOT_ELYTRA),
                    List.of(SmithingTemplates.EMPTY_SLOT_INGOT)
            );


    public static final SmithingTemplateItem NETHERITE_UPGRADE = EndItems
            .getItemRegistry()
            .registerSmithingTemplateItem(
                    BetterEnd.makeID("netherite_upgrade"),
                    List.of(EMPTY_SLOT_HAMMER),
                    List.of(SmithingTemplates.EMPTY_SLOT_INGOT)
            );

    public static void ensureStaticallyLoaded() {
    }
}
