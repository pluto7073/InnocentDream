package io.innocentdream.rendering;

import io.innocentdream.math.MathUtils;
import io.innocentdream.math.Vec2d;
import io.innocentdream.math.Vec2f;
import io.innocentdream.utils.Identifier;
import org.lwjgl.opengl.GL11;

public class GameObject {

    private static final float[] TEX_COORDS = {
            0, 0,
            1, 0,
            1, 1,
            0, 1
    };
    private static final int[] INDICES = {
            0, 1, 2, 3, 2, 0
    };

    private Vec2f position;
    private Vec2f size;
    private Vec2d observedWindow;

    private final Model model;
    private final CoordinateType type;
    public IDClient client;
    private Texture texture;

    public GameObject(Vec2f position, Vec2f size, CoordinateType type, IDClient client) {
        this(position, size, null, type, client);
    }

    public GameObject(Vec2f position, Vec2f size, Texture texture, CoordinateType type, IDClient client) {
        this.position = position;
        this.size = size;
        this.model = new Model(createVerticesArray(position, size, type, client), TEX_COORDS, INDICES, 3);
        this.texture = texture;
        this.type = type;
        this.client = client;
        this.observedWindow = new Vec2d(client.getWindowWidth(), client.getWindowHeight());
    }

    public void render() {
        if (texture == null) return;
        checkWindow();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        texture.bind(0);
        this.model.draw();
    }

    private void checkWindow() {
        Vec2d currentWindow = new Vec2d(client.getWindowWidth(), client.getWindowHeight());
        if (currentWindow.compareTo(this.observedWindow) != 0) {
            this.observedWindow = currentWindow;
            updateModel();
        }
     }

    public void setPosition(Vec2f position) {
        setPosition(position.x, position.y);
    }

    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
        this.updateModel();
    }

    public void setSize(Vec2f size) {
        setSize(size.x, size.y);
    }

    public void setSize(float width, float height) {
        this.size.x = width;
        this.size.y = height;
        this.updateModel();
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void updateModel() {
        this.model.updateVertices(createVerticesArray(position, size, type, client));
    }

    public Vec2f getPos() {
        return this.position;
    }

    public Vec2f getSize() {
        return this.size;
    }

    protected static float[] createVerticesArray(Vec2f pos, Vec2f size, CoordinateType type, IDClient client) {
        Vec2d lowerLeft = new Vec2d(pos.x - (size.x / 2), pos.y - (size.y / 2));
        Vec2d upperRight = new Vec2d(pos.x + (size.x / 2), pos.y + (size.y / 2));
        switch (type) {
            case RENDER -> {
                //We're good if it's like this, this is what we want
            }
            case GUI -> {
                assert client != null;
                lowerLeft = MathUtils.guiToRenderCoords(lowerLeft, client);
                upperRight = MathUtils.guiToRenderCoords(upperRight, client);
            }
            case WINDOW -> {
                assert client != null;
                lowerLeft = MathUtils.windowToRenderCoords(lowerLeft, client);
                upperRight = MathUtils.windowToRenderCoords(upperRight, client);
            }
            case WORLD -> {
                //TODO
            }
        }
        return new float[]{
                lowerLeft.x.floatValue(), upperRight.y.floatValue(), 0, // left top
                upperRight.x.floatValue(), upperRight.y.floatValue(), 0, // right top
                upperRight.x.floatValue(), lowerLeft.y.floatValue(), 0, // right bottom
                lowerLeft.x.floatValue(), lowerLeft.y.floatValue(), 0, // left bottom
        };
    }

    public enum CoordinateType {
        RENDER, GUI, WINDOW, WORLD
    }

}
