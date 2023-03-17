package io.innocentdream;

import io.innocentdream.config.GamePropertyManager;
import io.innocentdream.registry.Registries;
import io.innocentdream.rendering.IDClient;
import io.innocentdream.utils.DiscordUtil;
import io.innocentdream.utils.OS;
import io.innocentdream.utils.Task;
import io.innocentdream.utils.Utils;
import net.querz.nbt.io.NBTSerializer;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class InnocentDream {

    public static final String VERSION = "dev-2023.0.2";
    public static final int VERSION_NUMBER = 4;
    public static volatile boolean IS_RUNNING = false;
    public static final Logger LOGGER = Logger.getLogger("Innocent Dream");

    public static void cleanUp(IDClient client) {
        LOGGER.info("Stopping");
        for (Task t : Registries.EXIT_TASK.values()) {
            t.doTask();
        }
        glfwMakeContextCurrent(client.getWindowID());
        glfwTerminate();
    }

    public static void main(String[] args) {
        if (!Utils.contains(args, "--server")) {
            LOGGER.info("Starting Innocent Dream Client Version %s...".formatted(VERSION));
            IS_RUNNING = true;
            new File(OS.PATH).mkdirs();
            OS.verifyLibraries();
            GamePropertyManager.start();
            DiscordUtil.init();
            IDClient.create();
        }
    }

}
