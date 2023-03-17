package io.innocentdream.scene;

import io.innocentdream.rendering.IDClient;
import io.innocentdream.screen.Screen;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class ScreenScene extends Scene {

    private final Screen screen;

    public ScreenScene(Screen screen) {
        super(screen.getName());
        this.screen = screen;
    }

    @Override
    public void tick() {
        this.screen.tick();
    }

    @Override
    public void drawBackground() {
        glClearColor(this.screen.getBGCol().getRed() / 255f,
                this.screen.getBGCol().getGreen() / 255f,
                this.screen.getBGCol().getBlue() / 255f,
                this.screen.getBGCol().getAlpha() / 255f);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void drawGUI() {
        this.screen.draw();
    }

    @Override
    public void draw() {}

}
