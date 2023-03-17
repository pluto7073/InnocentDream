package io.innocentdream.registry;

import io.innocentdream.action.Action;
import io.innocentdream.tile.Tile;
import io.innocentdream.utils.Identifier;
import io.innocentdream.utils.Task;

public final class Registries {

    public static final Registry<Task> LOADING_TASK;
    public static final Registry<Task> EXIT_TASK;
    public static final Registry<Action> ACTION;
    public static final Registry<Tile> TILE;

    private static <T> Registry<T> create(String name, T defaultValue) {
        return new Registry<>(name) {
            @Override
            public T getDefaultValue() {
                return defaultValue;
            }
        };
    }

    public static <T> T register(Registry<T> registry, Identifier id, T value) {
        return registry.register(id, value);
    }

    static {
        LOADING_TASK = Registries.create("loading_task", null);
        EXIT_TASK = Registries.create("exit_task", null);
        ACTION = Registries.create("action", null);
        TILE = Registries.create("tile", new Tile(new Tile.Settings()));
    }

}
