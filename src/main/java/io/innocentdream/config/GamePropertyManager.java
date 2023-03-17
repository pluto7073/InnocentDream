package io.innocentdream.config;

import io.innocentdream.InnocentDream;
import io.innocentdream.action.Action;
import io.innocentdream.action.Actions;
import io.innocentdream.registry.Registries;
import io.innocentdream.utils.Identifier;
import io.innocentdream.utils.OS;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class GamePropertyManager {

    private static final Properties PROPERTIES;

    static {
        PROPERTIES = new Properties();
        Registries.register(Registries.EXIT_TASK, new Identifier("game_properties"), GamePropertyManager::stop);
    }

    public static void start() {
        resetProperties();
        File settings = new File(OS.PATH, "game.properties");
        if (settings.exists()) {
            try (FileReader reader = new FileReader(settings)) {
                PROPERTIES.load(reader);
            } catch (IOException e) {
                InnocentDream.LOGGER.error("Error in loading options: " + e.getMessage(), e);
            }
        } else {
            try {
                settings.createNewFile();
                resetProperties();
            } catch (IOException e) {
                InnocentDream.LOGGER.error("Error in loading options: " + e.getMessage(), e);
            }
        }
    }

    public static void stop() {
        File settings = new File(OS.PATH, "game.properties");
        try (FileWriter writer = new FileWriter(settings)) {
            PROPERTIES.store(writer, "Innocent Dream's Config");
        } catch (IOException e) {
            InnocentDream.LOGGER.error("Error in saving options: " + e.getMessage(), e);
        }
    }

    public static void resetProperties() {
        //MISC
        PROPERTIES.put("fullscreen", "true");
        PROPERTIES.put("scale", "1.0");
        PROPERTIES.put("lang", "en_us");

        //KEYS
        Actions.forEach((identifier, action) -> PROPERTIES.put(action.getPropertiesKey(), String.valueOf(action.getDefaultKey())));
    }

    public static void saveProperty(String property, Object value) {
        PROPERTIES.put(property, value);
    }

    public static Object getSetting(String property) {
        return PROPERTIES.get(property);
    }

    public static Object getOrDefault(String property, Object defaultValue) {
        Object value = getSetting(property);
        if (value == null) {
            value = defaultValue;
            saveProperty(property, defaultValue);
        }
        return value;
    }

    //MISC PROPERTIES

    public static boolean getFullscreenProperty() {
        return Boolean.parseBoolean((String) getOrDefault("fullscreen", true));
    }

    public static double getScaleProperty() {
        return Double.parseDouble((String) getOrDefault("scale", 1.0));
    }

    public static String getLanguageProperty() {
        return (String) getOrDefault("lang", "en_us");
    }

    //KEY CODES

    public static int getKeyCode(String key, int defaultKey) {
        return Integer.parseInt((String) getOrDefault(key, defaultKey));
    }

}
