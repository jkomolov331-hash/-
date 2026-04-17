package com.armorhud.mod.client.gui;

import com.armorhud.mod.common.ArmorDurabilityHelper;
import com.armorhud.mod.common.HudConfig;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ArmorHudPositionScreen extends Screen {

    private static final String[] POSITION_NAMES = {
        "Bottom Left", "Bottom Right", "Top Left", "Top Right"
    };

    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 20;

    private final Screen parent;

    // Preview dragging
    private boolean dragging = false;
    private int dragStartX, dragStartY;
    private int origOffsetX, origOffsetY;

    // Color stage preview buttons
    private static final int[] STAGE_COLORS = {
        ArmorDurabilityHelper.COLOR_STAGE_1,
        ArmorDurabilityHelper.COLOR_STAGE_2,
        ArmorDurabilityHelper.COLOR_STAGE_3,
        ArmorDurabilityHelper.COLOR_STAGE_4,
        ArmorDurabilityHelper.COLOR_STAGE_5
    };

    public ArmorHudPositionScreen(Screen parent) {
        super(new TranslationTextComponent("armorhud.hud.armor_settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        int leftX = 10;
        int startY = 30;

        // Position selector buttons (2x2 grid)
        String[] labels = {"Top Left", "Top Right", "Bottom Left", "Bottom Right"};
        int[] posValues = {2, 3, 0, 1};
        for (int i = 0; i < 4; i++) {
            final int posVal = posValues[i];
            final String lbl = labels[i];
            int bx = leftX + (i % 2) * (BUTTON_WIDTH + 5);
            int by = startY + (i / 2) * (BUTTON_HEIGHT + 5);
            this.addButton(new Button(bx, by, BUTTON_WIDTH, BUTTON_HEIGHT,
                new StringTextComponent(lbl),
                btn -> {
                    HudConfig.HUD_POSITION.set(posVal);
                    HudConfig.HUD_OFFSET_X.set(2);
                    HudConfig.HUD_OFFSET_Y.set(2);
                }
            ));
        }

        // Offset fine-tune buttons
        int tuneY = startY + 55;
        this.addButton(new Button(leftX, tuneY, 30, BUTTON_HEIGHT,
            new StringTextComponent("X-"), btn -> nudge(-2, 0)));
        this.addButton(new Button(leftX + 35, tuneY, 30, BUTTON_HEIGHT,
            new StringTextComponent("X+"), btn -> nudge(2, 0)));
        this.addButton(new Button(leftX + 75, tuneY, 30, BUTTON_HEIGHT,
            new StringTextComponent("Y-"), btn -> nudge(0, -2)));
        this.addButton(new Button(leftX + 110, tuneY, 30, BUTTON_HEIGHT,
            new StringTextComponent("Y+"), btn -> nudge(0, 2)));

        // Show durability numbers toggle
        int toggleY = tuneY + 30;
        this.addButton(new Button(leftX, toggleY, BUTTON_WIDTH * 2 + 5, BUTTON_HEIGHT,
            new StringTextComponent("Durability Numbers: " + (HudConfig.SHOW_DURABILITY_NUMBERS.get() ? "ON" : "OFF")),
            btn -> {
                boolean cur = HudConfig.SHOW_DURABILITY_NUMBERS.get();
                HudConfig.SHOW_DURABILITY_NUMBERS.set(!cur);
                btn.setMessage(new StringTextComponent("Durability Numbers: " + (!cur ? "ON" : "OFF")));
            }
        ));

        // Back button
        this.addButton(new Button(
            this.width / 2 - 75, this.height - 30,
            150, BUTTON_HEIGHT,
            new StringTextComponent("Back"),
            btn -> this.minecraft.setScreen(this.parent)
        ));
    }

    private void nudge(int dx, int dy) {
        HudConfig.HUD_OFFSET_X.set(HudConfig.HUD_OFFSET_X.get() + dx);
        HudConfig.HUD_OFFSET_Y.set(HudConfig.HUD_OFFSET_Y.get() + dy);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        // Title
        drawCenteredString(matrixStack, this.font, "Armor HUD Position Settings",
            this.width / 2, 10, 0xFFFFFF);

        // Info text
        int pos = HudConfig.HUD_POSITION.get();
        drawString(matrixStack, this.font,
            "Current: " + POSITION_NAMES[pos] + "  Offset: " + HudConfig.HUD_OFFSET_X.get() + ", " + HudConfig.HUD_OFFSET_Y.get(),
            10, this.height - 50, 0xAAAAAA);

        // Draw color stage legend
        int legendX = this.width - 160;
        int legendY = 30;
        drawString(matrixStack, this.font, "Durability Stages:", legendX, legendY - 12, 0xFFFFFF);
        String[] stageLabels = {">80% (Full)", ">60%", ">40%", ">20%", "<=20% (Critical)"};
        for (int i = 0; i < STAGE_COLORS.length; i++) {
            int c = STAGE_COLORS[i];
            int sc = 0xFF000000 | c;
            // Draw color swatch
            fill(matrixStack, legendX, legendY + i * 14, legendX + 12, legendY + i * 14 + 10, sc);
            drawString(matrixStack, this.font, stageLabels[i], legendX + 15, legendY + i * 14, c);
        }

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
