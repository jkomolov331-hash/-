package com.armorhud.mod;

import com.armorhud.mod.client.KeyBindings;
import com.armorhud.mod.client.overlay.ArmorHudOverlay;
import com.armorhud.mod.common.HudConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ArmorHudMod.MOD_ID)
public class ArmorHudMod {

    public static final String MOD_ID = "armorhud";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public ArmorHudMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::clientSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, HudConfig.SPEC, "armorhud-client.toml");

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(ArmorHudOverlay.class);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        KeyBindings.register();
        LOGGER.info("Armor HUD Mod initialized!");
    }
}
