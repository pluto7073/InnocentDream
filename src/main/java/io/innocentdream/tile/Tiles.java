package io.innocentdream.tile;

import io.innocentdream.registry.Registries;
import io.innocentdream.utils.Identifier;

public class Tiles {

    public static final Tile GRASS;
    public static final Tile STONE;
    
    private static Tile register(String id, Tile tile) {
        return Registries.register(Registries.TILE, new Identifier(id), tile);
    }

    static {
        GRASS = register("grass", new Tile(new Tile.Settings()));
        STONE = register("stone", new Tile(new Tile.Settings().setBreakTime(3.0f)));
    }

}
