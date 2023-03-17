package io.innocentdream.scene;

import io.innocentdream.math.Vec2f;
import io.innocentdream.rendering.GameObject;
import io.innocentdream.rendering.IDClient;
import io.innocentdream.rendering.TextureHelper;
import io.innocentdream.tile.Tiles;
import io.innocentdream.utils.Identifier;

public class WorldScene extends Scene {
    private final GameObject testObject;
    private final GameObject testObject2;
    private final GameObject testObject3;

    public WorldScene(IDClient client) {
        super("World", client);
        Identifier id = Tiles.GRASS.getTextureIdentifier();
        testObject = new GameObject(new Vec2f(0, 0), new Vec2f(50, 50), TextureHelper.getTexture(id), GameObject.CoordinateType.GUI, client);
        testObject2 = new GameObject(new Vec2f(50, 0), new Vec2f(50, 50), TextureHelper.getTexture(id), GameObject.CoordinateType.GUI, client);
        testObject3 = new GameObject(new Vec2f(0, -50), new Vec2f(50, 50), TextureHelper.getTexture(Tiles.STONE.getTextureIdentifier()), GameObject.CoordinateType.GUI, client);
    }

    @Override
    public void draw() {
        testObject.render();
        testObject2.render();
        testObject3.render();
    }
}
