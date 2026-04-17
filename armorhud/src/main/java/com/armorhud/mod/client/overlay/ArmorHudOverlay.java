package com.armorhud.mod.client.overlay;

import com.armorhud.mod.ArmorHudMod;
import com.armorhud.mod.common.ArmorDurabilityHelper;
import com.armorhud.mod.common.HudConfig;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = ArmorHudMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ArmorHudOverlay {

    // Slots in display order: helmet, chestplate, leggings, boots
    private static final EquipmentSlotType[] ARMOR_SLOTS = {
        EquipmentSlotType.HEAD,
        EquipmentSlotType.CHEST,
        EquipmentSlotType.LEGS,
        EquipmentSlotType.FEET
    };

    private static final int ICON_SIZE = 16;
    private static final int PADDING = 2;
    private static final int TEXT_OFFSET_X = 18;

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        // Don't render if inventory is open and config says so
        if (mc.screen != null && !HudConfig.SHOW_IN_INVENTORY.get()) return;

        PlayerEntity player = mc.player;
        MatrixStack matrixStack = event.getMatrixStack();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        // Calculate base position from config
        int baseX = getBaseX(screenWidth);
        int baseY = getBaseY(screenHeight);

        renderArmorHud(mc, matrixStack, player, baseX, baseY);
    }

    private static int getBaseX(int screenWidth) {
        int position = HudConfig.HUD_POSITION.get();
        int offsetX = HudConfig.HUD_OFFSET_X.get();
        // right side positions
        if (position == 1 || position == 3) {
            return screenWidth - ICON_SIZE - TEXT_OFFSET_X - 20 + offsetX;
        }
        // left side
        return offsetX;
    }

    private static int getBaseY(int screenHeight) {
        int position = HudConfig.HUD_POSITION.get();
        int offsetY = HudConfig.HUD_OFFSET_Y.get();
        int totalHeight = ARMOR_SLOTS.length * (ICON_SIZE + PADDING);
        // top positions
        if (position == 2 || position == 3) {
            return offsetY;
        }
        // bottom positions
        return screenHeight - totalHeight - 39 + offsetY; // 39 to sit above hotbar
    }

    private static void renderArmorHud(Minecraft mc, MatrixStack matrixStack, PlayerEntity player, int baseX, int baseY) {
        ItemRenderer itemRenderer = mc.getItemRenderer();
        FontRenderer font = mc.font;

        for (int i = 0; i < ARMOR_SLOTS.length; i++) {
            EquipmentSlotType slot = ARMOR_SLOTS[i];
            ItemStack stack = player.getItemBySlot(slot);

            int x = baseX;
            int y = baseY + i * (ICON_SIZE + PADDING);

            if (stack.isEmpty()) continue;

            // Determine color tint based on durability stage
            int color = ArmorDurabilityHelper.getArmorColor(stack);
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;

            // Render item with color tint overlay
            renderItemWithTint(mc, matrixStack, itemRenderer, stack, x, y, r, g, b);

            // Render durability number
            if (HudConfig.SHOW_DURABILITY_NUMBERS.get()) {
                int durability = ArmorDurabilityHelper.getRemainingDurability(stack);
                if (durability >= 0) {
                    String durText = String.valueOf(durability);
                    int textColor = 0xFFFFFF; // white text
                    // Shadow for readability
                    matrixStack.pushPose();
                    font.drawShadow(matrixStack, durText, x + TEXT_OFFSET_X, y + 4, textColor);
                    matrixStack.popPose();
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private static void renderItemWithTint(Minecraft mc, MatrixStack matrixStack, ItemRenderer itemRenderer,
                                            ItemStack stack, int x, int y, int r, int g, int b) {
        // Render the item normally first
        RenderSystem.enableBlend();
        itemRenderer.renderAndDecorateItem(stack, x, y);
        itemRenderer.renderGuiItemDecorations(mc.font, stack, x, y, null);

        // If not white (stage 0 = full), draw a color overlay rectangle on top
        if (r != 0xFF || g != 0xFF || b != 0xFF) {
            drawColoredRect(matrixStack, x, y, ICON_SIZE, ICON_SIZE, r, g, b, 80); // 80 alpha = semi-transparent tint
        }
        RenderSystem.disableBlend();
    }

    /**
     * Draws a semi-transparent colored rectangle to tint the armor icon.
     */
    private static void drawColoredRect(MatrixStack matrixStack, int x, int y, int width, int height,
                                         int r, int g, int b, int alpha) {
        com.mojang.blaze3d.systems.RenderSystem.disableTexture();
        com.mojang.blaze3d.systems.RenderSystem.enableBlend();
        com.mojang.blaze3d.systems.RenderSystem.defaultBlendFunc();

        net.minecraft.client.renderer.Tessellator tessellator = net.minecraft.client.renderer.Tessellator.getInstance();
        net.minecraft.client.renderer.BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(7, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_COLOR);

        com.mojang.blaze3d.matrix.MatrixStack.Entry entry = matrixStack.last();
        com.mojang.blaze3d.vertex.IVertexBuilder vb = buffer;

        float fr = r / 255f;
        float fg = g / 255f;
        float fb = b / 255f;
        float fa = alpha / 255f;

        buffer.vertex(entry.pose(), x, y + height, 0).color(fr, fg, fb, fa).endVertex();
        buffer.vertex(entry.pose(), x + width, y + height, 0).color(fr, fg, fb, fa).endVertex();
        buffer.vertex(entry.pose(), x + width, y, 0).color(fr, fg, fb, fa).endVertex();
        buffer.vertex(entry.pose(), x, y, 0).color(fr, fg, fb, fa).endVertex();

        tessellator.end();
        com.mojang.blaze3d.systems.RenderSystem.enableTexture();
        com.mojang.blaze3d.systems.RenderSystem.disableBlend();
    }
}
