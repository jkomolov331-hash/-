package com.armorhud.mod.client;

import com.armorhud.mod.ArmorHudMod;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = ArmorHudMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KeyBindings {

    public static KeyBinding OPEN_HUD_SETTINGS;

    public static void register() {
        OPEN_HUD_SETTINGS = new KeyBinding(
            "key.armorhud.openHud",
            KeyConflictContext.IN_GAME,
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "key.categories.misc"
        );
        ClientRegistry.registerKeyBinding(OPEN_HUD_SETTINGS);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (OPEN_HUD_SETTINGS != null && OPEN_HUD_SETTINGS.consumeClick()) {
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
            if (mc.screen == null) {
                mc.setScreen(new com.armorhud.mod.client.gui.HudSettingsScreen());
            }
        }
    }
}
