package io.innocentdream.action;

import io.innocentdream.config.GamePropertyManager;
import io.innocentdream.registry.Registries;
import io.innocentdream.rendering.IDClient;
import io.innocentdream.scene.Scene;
import io.innocentdream.utils.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.function.BiConsumer;

public final class Actions {

    public static final Action FULLSCREEN_ACTION;
    public static final Action EXIT_ACTION;

    private static Action register(String id, Action action) {
        return Registries.register(Registries.ACTION, new Identifier(id), action);
    }

    public static void forEach(BiConsumer<Identifier, Action> consumer) {
        Registries.ACTION.forEach(consumer);
    }

    public static void pollActions(Scene scene, IDClient client) {
        Registries.ACTION.forEach((id, action ) -> {
            if (GLFW.glfwGetKey(client.getWindowID(), GamePropertyManager.getKeyCode(action.getPropertiesKey(), action.getDefaultKey())) == GLFW.GLFW_TRUE) {
                if (!action.canUse(scene)) {
                    return;
                }
                action.doAction(client);
                action.active = true;
            } else {
                if (action.active) {
                    action.afterActionPerformed(client);
                }
                action.active = false;
            }
        });
    }

    static {
        FULLSCREEN_ACTION = register("fullscreen", new FullscreenAction());
        EXIT_ACTION = register("exit", new ExitAction());
    }

}
