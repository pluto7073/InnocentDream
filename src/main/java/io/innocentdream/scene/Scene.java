package io.innocentdream.scene;

import io.innocentdream.rendering.IDClient;
import io.innocentdream.screen.Screen;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Stack;

public abstract class Scene {

    public final String name;

    public IDClient client;

    protected final Stack<Screen> screens;

    public Scene(String name) {
        this(name, null);
    }

    public Scene(String name, IDClient client) {
        this.name = name;
        this.client = client;
        this.screens = new Stack<>();
    }

    public void addScreen(Screen screen) {
        this.screens.add(screen);
    }

    public void drawGUI() {
        for (Screen s : screens) {
            s.draw();
        }
    }

    public void tick() {
        for (Screen s : screens) {
            s.tick();
        }
    }

    public abstract void draw();

    public void drawBackground() {
        Color c;
        if (screens.size() > 0) {
            c = screens.get(0).getBGCol();
        } else {
            c = new Color(0);
        }
        GL11.glClearColor(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

}
