package io.innocentdream.world;

import io.innocentdream.math.TilePos;
import io.innocentdream.math.Vec2i;
import io.innocentdream.tile.Tile;
import io.innocentdream.tile.TileInstance;
import io.innocentdream.tile.Tiles;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class WorldData {

    public static final int DEFAULT_WORLD_WIDTH = 64;
    public static final int DEFAULT_WORLD_HEIGHT = 64;

    private final HashMap<TilePos, TileInstance> tileMap;

    /**
     * Creates a new WorldData object using the data contained in the specified file
     * @param loadFrom The world file to load from
     */
    public WorldData(File loadFrom) {
        this();
        //TODO
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public WorldData(int width, int height) {
        tileMap = new HashMap<>();
        Vec2i xRange = new Vec2i(-width / 2, width / 2);
        Vec2i yRange = new Vec2i(-height / 2, height / 2);
        for (int y = yRange.x; y < yRange.y; y++) {
            for (int x = xRange.x; x < xRange.y; x++) {
                TilePos floor = new TilePos(x, y, -1);
                TileInstance instance = new TileInstance(Tiles.STONE);
                tileMap.put(floor, instance);
            }
        }
    }

    public TileInstance getTileInstance(TilePos pos) {
        return tileMap.get(pos);
    }

    public TilePos getPositionOfInstance(TileInstance instance) {
        TilePos pos = null;
        for (Map.Entry<TilePos, TileInstance> e : tileMap.entrySet()) {
            if (instance.equals(e.getValue())) {
                pos = e.getKey();
            }
        }
        return pos;
    }

    /**
     * Creates an empty WorldData object
     */
    public WorldData() {
        this(DEFAULT_WORLD_WIDTH, DEFAULT_WORLD_HEIGHT);
    }

}
