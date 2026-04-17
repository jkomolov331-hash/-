package com.armorhud.mod.common;

import net.minecraftforge.common.ForgeConfigSpec;

public class HudConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // HUD Position: 0=bottom-left, 1=bottom-right, 2=top-left, 3=top-right
    public static final ForgeConfigSpec.IntValue HUD_POSITION;
    public static final ForgeConfigSpec.IntValue HUD_OFFSET_X;
    public static final ForgeConfigSpec.IntValue HUD_OFFSET_Y;
    public static final ForgeConfigSpec.BooleanValue SHOW_IN_INVENTORY;
    public static final ForgeConfigSpec.BooleanValue SHOW_DURABILITY_NUMBERS;

    static {
        BUILDER.push("armor_hud");

        HUD_POSITION = BUILDER
            .comment("HUD position: 0=Bottom Left, 1=Bottom Right, 2=Top Left, 3=Top Right")
            .defineInRange("hudPosition", 0, 0, 3);

        HUD_OFFSET_X = BUILDER
            .comment("Custom X offset from the anchor position")
            .defineInRange("hudOffsetX", 2, -500, 500);

        HUD_OFFSET_Y = BUILDER
            .comment("Custom Y offset from the anchor position")
            .defineInRange("hudOffsetY", 2, -500, 500);

        SHOW_IN_INVENTORY = BUILDER
            .comment("Show armor HUD when inventory is open")
            .define("showInInventory", true);

        SHOW_DURABILITY_NUMBERS = BUILDER
            .comment("Show durability numbers next to armor icons")
            .define("showDurabilityNumbers", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
