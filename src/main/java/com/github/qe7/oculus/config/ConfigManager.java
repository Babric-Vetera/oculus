package com.github.qe7.oculus.config;

import com.github.qe7.oculus.OculusMod;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.nio.file.Files;

public class ConfigManager {

    private final String OCULUS_KEY_BINDING_KEY = "oculusKeyBinding";

    private final File minecraftConfigDir;

    public ConfigManager(File minecraftConfigDir) {
        this.minecraftConfigDir = minecraftConfigDir;
    }

    private File getConfigFile() {
        String CONFIG_FILE_NAME = "oculus-config.txt";
        return new File(minecraftConfigDir, CONFIG_FILE_NAME);
    }

    public void saveConfig(int oculusKeyBinding) {
        try {
            File configFile = getConfigFile();

            if (configFile.getParentFile().mkdirs()) {
                OculusMod.LOGGER.info("Created config directory: {}", configFile.getParent());
            }

            String content = OCULUS_KEY_BINDING_KEY + "=" + oculusKeyBinding;
            Files.write(configFile.toPath(), content.getBytes());
            OculusMod.LOGGER.info("Config saved successfully to {}", configFile.getAbsolutePath());
        } catch (Exception e) {
            OculusMod.LOGGER.error("Failed to save config: {}", e.getMessage(), e);
        }
    }

    public int loadOculusKeyBinding() {
        try {
            File configFile = getConfigFile();

            if (!configFile.exists()) {
                OculusMod.LOGGER.info("Config file does not exist, using default key binding.");
                return Keyboard.KEY_C;
            }

            String content = new String(Files.readAllBytes(configFile.toPath()));
            String[] lines = content.split("\n");
            for (String line : lines) {
                if (line.startsWith(OCULUS_KEY_BINDING_KEY + "=")) {
                    String value = line.substring((OCULUS_KEY_BINDING_KEY + "=").length()).trim();
                    int keyCode = Integer.parseInt(value);
                    OculusMod.LOGGER.info("Loaded oculusKeyBinding from config: {}", keyCode);
                    return keyCode;
                }
            }

            OculusMod.LOGGER.warn("oculusKeyBinding not found in config, using default.");
            return Keyboard.KEY_C;
        } catch (Exception e) {
            OculusMod.LOGGER.error("Failed to load config: {}", e.getMessage(), e);
            return Keyboard.KEY_C;
        }
    }
}


