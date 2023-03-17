package io.innocentdream.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.innocentdream.InnocentDream;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

public class OS {

    private static final String BASE_VERSION_INFO_LINK = "https://innocent-dream.web.app/cdn/versions/%s/%s.json";
    private static volatile boolean CONNECTED = true;

    public static final String OS = System.getProperty("os.name").toLowerCase(Locale.ROOT);
    public static final String ARCH = System.getProperty("os.arch");
    public static final String VERSION = System.getProperty("os.version").toLowerCase(Locale.ROOT);
    public static final String PATH = System.getenv("innocentdream.runDir") != null ? System.getenv("innocentdream.runDir") : "run/";
    public static final Logger LIBRARY_UTIL_LOGGER = LogManager.getLogger("LibraryUtil");

    public static void verifyLibraries() {
        File nativesDir = new File(PATH, "libs/natives");
        nativesDir.mkdirs();
        System.setProperty("org.lwjgl.librarypath", nativesDir.getAbsolutePath());
        if (!isConnected()) {
            LIBRARY_UTIL_LOGGER.warn("No internet connection available, assuming libraries are okay");
            return;
        }
        String version = InnocentDream.VERSION.toLowerCase(Locale.ROOT);
        String download = String.format(BASE_VERSION_INFO_LINK, version, version);
        File versionFile = new File(PATH, "versions/" + version + "/" + version + ".json");
        versionFile.getParentFile().mkdirs();
        try {
            URL url = new URL(download);
            InputStream stream = url.openStream();
            Files.copy(stream, versionFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            JsonObject data = JsonHelper.streamToObject(new FileInputStream(versionFile));
            JsonObject natives = JsonHelper.getObject(JsonHelper.getObject(data, "libraries"), "natives");
            JsonArray os = JsonHelper.getArray(natives, getOsString());
            os.forEach(e -> {
                if (!(e instanceof JsonObject lib)) return;
                String link = JsonHelper.getString(lib, "download");
                long size = JsonHelper.getLong(lib, "size");
                String name = JsonHelper.getString(lib, "name");
                File target = new File(nativesDir, name);
                if (!target.exists() || target.length() != size) {
                    downloadLib(link, target);
                }
            });
        } catch (IOException e) {
            LIBRARY_UTIL_LOGGER.fatal("Error in loading libraries", e);
            System.exit(1);
        }
    }

    private static void downloadLib(String url, File target) {
        try {
            InputStream is = new URL(url).openStream();
            Files.copy(is, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            LIBRARY_UTIL_LOGGER.info("Downloaded %s to %s".formatted(target.getName(), target.getAbsolutePath()));
        } catch (IOException e) {
            LIBRARY_UTIL_LOGGER.error("Failed to download %s from %s".formatted(target.getName(), url), e);
        }
    }

    public static File initDiscordLibs() {
        File discordFolder = new File(PATH, "libs/natives/discord");
        if (!discordFolder.exists()) {
            try {
                InputStream is = new URL("https://innocent-dream.web.app/cdn/libs/natives/discord.zip").openStream();
            } catch (IOException e) {
                LIBRARY_UTIL_LOGGER.error("Couldn't download discord libraries", e);
            }
        }
        if (isWindows()) {
            return new File(discordFolder, "lib/x86_64/discord_game_sdk.dll");
        } else if (isMac()) {
            return new File(discordFolder, "lib/x86_64/discord_game_sdk.dylib");
        } else {
            return new File(discordFolder, "lib/x86_64/discord_game_sdk.so");
        }
    }

    public static boolean isWindows() {
        return OS.contains("win");
    }

    public static boolean isMac() {
        return OS.contains("mac");
    }

    public static boolean isLinux() {
        return OS.contains("nix") || OS.contains("nux") || OS.contains("aix");
    }

    /**
     * Tries to connect to the ID servers
     * @return <code>true</code> if the test connection is successful, <code>false</code> if not or if any exceptions are thrown
     */
    public static boolean isConnected() {
        try {
            URLConnection connection = new URL("https://innocent-dream.web.app/").openConnection();
            connection.connect();
            return CONNECTED = true;
        } catch (IOException e) {
            return CONNECTED = false;
        }
    }

    /**
     * Unlike <code>isConnected()</code>, this method doesn't try to connect to
     * the internet, it just returns the last value returned by <code>isConnected()</code>
     * @return the last value returned by <code>isConnected()</code>, <code>true</code> if that method hasn't been run
     */
    public static boolean getConnected() {
        return CONNECTED;
    }

    public static String getOsString() {
        if (isLinux()) {
            return "linux";
        } else if (isMac()) {
            return "macos";
        } else if (isWindows()) {
            return "windows";
        } else {
            return "other";
        }
    }

}
