package io.innocentdream;

import io.innocentdream.config.GamePropertyManager;
import io.innocentdream.rendering.WindowManager;
import io.innocentdream.utils.OS;
import org.apache.log4j.Logger;

import java.io.File;

public class InnocentDream {

    public static final String VERSION = "dev-2023.0.2";
    public static volatile boolean IS_RUNNING = false;
    public static final Logger LOGGER = Logger.getLogger("Innocent Dream");
    public static WindowManager WINDOW_MANAGER;

    public static void main(String[] args) {
        LOGGER.info("Starting Innocent Dream Version %s...".formatted(VERSION));
        new File(OS.PATH).mkdirs();
        OS.verifyLibraries();
        GamePropertyManager.start();
    }

}
