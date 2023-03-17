package io.innocentdream.tile;

import io.innocentdream.registry.Registries;
import io.innocentdream.rendering.IDClient;
import io.innocentdream.utils.Identifier;

public class Tile {

    protected final Settings settings;

    public Tile(Settings settings) {
        this.settings = settings;
    }

    public void onUse(TileInstance instance, IDClient client) {}

    public Identifier getTextureIdentifier() {
        Identifier tileId = Registries.TILE.getId(this);
        return new Identifier(tileId.getNamespace(), "tile/" + tileId.getPath());
    }

    public static class Settings {
        private boolean canMoveThrough = false;
        private boolean breakable = true;
        private float breakTime = 1.0F;

        public Settings canMoveThrough() {
            this.canMoveThrough = true;
            return this;
        }

        public Settings unbreakable() {
            this.breakable = false;
            return this;
        }

        public Settings setBreakTime(float breakTime) {
            this.breakTime = breakTime;
            return this;
        }

        public static Settings of(Settings source) {
            Settings settings = new Settings();
            settings.breakTime = source.breakTime;
            settings.breakable = source.breakable;
            settings.canMoveThrough = source.canMoveThrough;
            return settings;
        }

        public static Settings copy(Tile tile) {
            return of(tile.settings);
        }
    }

}
