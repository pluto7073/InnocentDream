package io.innocentdream.utils;

import io.innocentdream.InnocentDream;
import io.innocentdream.registry.Registries;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;

import java.time.Instant;

public class DiscordUtil implements Runnable, ReadyCallback {

    public static DiscordUtil INSTANCE = null;

    private Thread thread;
    private DiscordUser user;
    private DiscordRichPresence presence;

    private DiscordUtil() {
        DiscordRPC.discordInitialize("837415091812171806",
                new DiscordEventHandlers.Builder().setReadyEventHandler(this).build(), true);
        this.thread = new Thread(this, "DiscordRPCThread");
        this.thread.start();
    }

    @Override
    public void run() {
        presence = new DiscordRichPresence.Builder("Loading...")
                .setBigImage("icon-512", "Innocent Dream")
                .setStartTimestamps(Instant.now().toEpochMilli())
                .build();
        DiscordRPC.discordUpdatePresence(presence);
        while (InnocentDream.IS_RUNNING) {
            DiscordRPC.discordRunCallbacks();

            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
        }
    }

    public static void setRPCState(String state) {
        INSTANCE.presence.state = state;
        DiscordRPC.discordUpdatePresence(INSTANCE.presence);
    }

    public static void cleanUp() {
        DiscordRPC.discordShutdown();
    }

    public static void init() {
        if (INSTANCE != null) {
            throw new IllegalStateException();
        }
        INSTANCE = new DiscordUtil();
        Registries.register(Registries.EXIT_TASK, new Identifier("discord"), DiscordUtil::cleanUp);
    }

    @Override
    public void apply(DiscordUser user) {
        this.user = user;
        InnocentDream.LOGGER.info(String.format("Loaded discordRPC with user %s#%s", user.username, user.discriminator));
    }
}
