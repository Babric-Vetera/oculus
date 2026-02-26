package com.github.qe7.oculus;

import com.github.qe7.oculus.interfaces.IGameSettings;
import com.github.qe7.oculus.mixins.AccessorMinecraft;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GameSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        }

        final GameSettings gameSettings = minecraft.gameSettings;

        if (gameSettings != null) {
            IGameSettings iGameSettings = (IGameSettings) gameSettings;
            if (iGameSettings.oculus$getOculusKeyBinding() != null) {
                LOGGER.info("Oculus key binding added successfully.");
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
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Oculus mod is initializing...");
    }
}
