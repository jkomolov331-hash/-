package com.armorhud.mod.client.gui;

import com.armorhud.mod.common.HudConfig;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HudSettingsScreen extends Screen {

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;

    public HudSettingsScreen() {
        super(new TranslationTextComponent("armorhud.hud.settings"));
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int startY = this.height / 2 - 60;

        // Button: Armor HUD Settings
        this.addButton(new Button(
            centerX - BUTTON_WIDTH / 2,
            startY,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            new TranslationTextComponent("armorhud.hud.armor_settings"),
            btn -> this.minecraft.setScreen(new ArmorHudPositionScreen(this))
        ));

        // Button: Toggle Inventory HUD
        boolean showInv = HudConfig.SHOW_IN_INVENTORY.get();
        this.addButton(new Button(
            centerX - BUTTON_WIDTH / 2,
            startY + 25,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            new StringTextComponent(new TranslationTextComponent("armorhud.hud.show_inventory").getString()
                + (showInv ? "ON" : "OFF")),
            btn -> {
                boolean current = HudConfig.SHOW_IN_INVENTORY.get();
                HudConfig.SHOW_IN_INVENTORY.set(!current);
                btn.setMessage(new StringTextComponent(
                    new TranslationTextComponent("armorhud.hud.show_inventory").getString()
                        + (!current ? "ON" : "OFF")));
            }
        ));

        // Button: Close
        this.addButton(new Button(
            centerX - BUTTON_WIDTH / 2,
            startY + 55,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            new TranslationTextComponent("armorhud.hud.close"),
            btn -> this.onClose()
        ));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        // Title
        drawCenteredString(matrixStack, this.font,
            new TranslationTextComponent("armorhud.hud.settings").getString(),
            this.width / 2, this.height / 2 - 80, 0xFFFFFF);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
