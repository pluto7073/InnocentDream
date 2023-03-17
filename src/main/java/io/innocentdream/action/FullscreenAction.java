package io.innocentdream.action;

import io.innocentdream.rendering.IDClient;
import io.innocentdream.scene.Scene;
import org.lwjgl.glfw.GLFW;

public class FullscreenAction extends Action {

    public FullscreenAction() {
        super("key_fullscreen", GLFW.GLFW_KEY_F5);
    }

    @Override
    public void doAction(IDClient client) {
        if (!active) {
            client.toggleFullscreen();
        }
    }

    @Override
    public void afterActionPerformed(IDClient client) {}

    @Override
    public boolean canUse(Scene scene) {
        return true;
    }

}
