package com.armorhud.mod.common;

import net.minecraft.item.ItemStack;

/**
 * Helper to compute armor wear stage (1–5) and color based on durability %.
 *
 * Stage colors (matching the screenshot):
 *  Stage 1 (>80%):  Green   0x55FF55
 *  Stage 2 (>60%):  Yellow  0xFFFF55
 *  Stage 3 (>40%):  Red     0xFF5555
 *  Stage 4 (>20%):  Dark Red 0xAA0000
 *  Stage 5 (<=20%): Purple  0xAA00AA
 */
public class ArmorDurabilityHelper {

    public static final int COLOR_STAGE_1 = 0x55FF55; // Green
    public static final int COLOR_STAGE_2 = 0xFFFF55; // Yellow
    public static final int COLOR_STAGE_3 = 0xFF5555; // Red
    public static final int COLOR_STAGE_4 = 0xAA0000; // Dark Red
    public static final int COLOR_STAGE_5 = 0xAA00AA; // Purple
    public static final int COLOR_FULL    = 0xFFFFFF; // White (no tint)

    public static int getStage(ItemStack stack) {
        if (stack.isEmpty() || !stack.isDamageableItem()) return 0;
        float pct = getDurabilityPercent(stack);
        if (pct > 0.80f) return 1;
        if (pct > 0.60f) return 2;
        if (pct > 0.40f) return 3;
        if (pct > 0.20f) return 4;
        return 5;
    }

    public static float getDurabilityPercent(ItemStack stack) {
        if (stack.isEmpty() || !stack.isDamageableItem()) return 1.0f;
        int maxDur = stack.getMaxDamage();
        if (maxDur <= 0) return 1.0f;
        int remaining = maxDur - stack.getDamageValue();
        return (float) remaining / (float) maxDur;
    }

    public static int getRemainingDurability(ItemStack stack) {
        if (stack.isEmpty() || !stack.isDamageableItem()) return -1;
        return stack.getMaxDamage() - stack.getDamageValue();
    }

    /**
     * Returns an ARGB color tint for the armor icon based on wear stage.
     * Full durability → white (no tint), as durability drops → color shifts.
     */
    public static int getArmorColor(ItemStack stack) {
        int stage = getStage(stack);
        switch (stage) {
            case 1: return COLOR_STAGE_1;
            case 2: return COLOR_STAGE_2;
            case 3: return COLOR_STAGE_3;
            case 4: return COLOR_STAGE_4;
            case 5: return COLOR_STAGE_5;
            default: return COLOR_FULL;
        }
    }

    /**
     * ARGB with alpha baked in (0xFF______).
     */
    public static int getArmorColorARGB(ItemStack stack) {
        return 0xFF000000 | getArmorColor(stack);
    }
}
