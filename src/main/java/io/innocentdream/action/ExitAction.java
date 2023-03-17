package io.innocentdream.action;

import io.innocentdream.rendering.IDClient;
import io.innocentdream.scene.Scene;
import org.lwjgl.glfw.GLFW;

public class ExitAction extends Action {

    public ExitAction() {
        super("key_exit", GLFW.GLFW_KEY_ESCAPE);
    }

    @Override
    public void doAction(IDClient client) {
        GLFW.glfwMakeContextCurrent(client.getWindowID());
        GLFW.glfwSetWindowShouldClose(client.getWindowID(), true);
    }

    @Override
    public void afterActionPerformed(IDClient client) {

    }

    @Override
    public boolean canUse(Scene scene) {
        return true;
    }
}
