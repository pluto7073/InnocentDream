package io.innocentdream.screen;

import java.awt.*;

public abstract class Screen {

    private final String name;
    protected Color background;

    public Screen(String name) {
        this.name = name;
        this.background = new Color(0);
    }

    public abstract void draw();
    public abstract void tick();

    public String getName() {
        return this.name;
    }

    public Color getBGCol() {
        return this.background;
    }

}
