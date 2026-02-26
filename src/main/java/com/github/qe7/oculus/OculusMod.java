package com.github.qe7.oculus;

import com.github.qe7.oculus.config.ConfigManager;
import com.github.qe7.oculus.interfaces.IGameSettings;
import com.github.qe7.oculus.mixins.AccessorMinecraft;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GameSettings;
import net.minecraft.src.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class OculusMod implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("oculus");

    public static OculusMod INSTANCE;

    public boolean failedInitialization = false;

    public OculusMod() {
        INSTANCE = this;
    }

    public void verifyInitialization() {
        final Minecraft minecraft = AccessorMinecraft.getMinecraft();

        LOGGER.info("Verifying Oculus mod initialization...");

        if (minecraft != null) {
            LOGGER.info("Minecraft instance accessed successfully.");
        } else {
            LOGGER.error("Failed to access Minecraft instance.");
            failedInitialization = true;
            return;
        }

        final GameSettings gameSettings = minecraft.gameSettings;

        if (gameSettings != null) {
            IGameSettings iGameSettings = (IGameSettings) gameSettings;
            if (iGameSettings.oculus$getOculusKeyBinding() != null) {
                LOGGER.info("Oculus key binding added successfully.");

                ConfigManager configManager;
                try {
                    File mcDir = null;
                    try {
                        mcDir = Minecraft.getMinecraftDir();
                    } catch (Throwable ignored) {
                    }

                    if (mcDir != null) {
                        configManager = new ConfigManager(mcDir);
                    } else {
                        configManager = new ConfigManager(new File(System.getProperty("user.home"), ".minecraft"));
                    }

                    try {
                        int loaded = configManager.loadOculusKeyBinding();
                        iGameSettings.oculus$getOculusKeyBinding().keyCode = loaded;
                        LOGGER.info("Applied saved Oculus key code: {}", loaded);
                    } catch (Throwable t) {
                        LOGGER.debug("Could not load/apply saved Oculus key code, keeping current binding", t);
                    }
                } catch (Throwable t) {
                    LOGGER.error("Failed to initialize ConfigManager; continuing without persistent config", t);
                    failedInitialization = false;
                }
            } else {
                LOGGER.error("Failed to add Oculus key binding.");
                failedInitialization = true;
            }
        } else {
            LOGGER.error("Failed to access GameSettings instance.");
            failedInitialization = true;
        }

        if (failedInitialization) {
            LOGGER.error("Oculus mod failed to initialize properly. Please check the logs for details.");
        } else {
            LOGGER.info("Oculus mod initialized successfully.");
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Minecraft mc = AccessorMinecraft.getMinecraft();
                if (mc == null) return;
                if (!(mc.gameSettings instanceof IGameSettings)) return;

                KeyBinding oculus = ((IGameSettings) mc.gameSettings).oculus$getOculusKeyBinding();
                if (oculus == null) return;

                LOGGER.info("Saving Oculus key binding... code={} name={}", oculus.keyCode, Keyboard.getKeyName(oculus.keyCode));

                try {
                    File mcDir = null;
                    try {
                        mcDir = Minecraft.getMinecraftDir();
                    } catch (Throwable ignored) {
                    }

                    ConfigManager cfg = (mcDir != null)
                            ? new ConfigManager(mcDir)
                            : new ConfigManager(new File(System.getProperty("user.home"), ".minecraft"));

                    cfg.saveConfig(oculus.keyCode);
                    LOGGER.info("Oculus config saved successfully.");
                } catch (Throwable e) {
                    LOGGER.error("Failed to save Oculus config at shutdown", e);
                }
            } catch (Throwable t) {
                LOGGER.warn("Exception in Oculus shutdown hook", t);
            }
        }));
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Oculus mod is initializing...");
    }
}
