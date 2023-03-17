package io.innocentdream.tile;

import io.innocentdream.math.TilePos;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.Tag;

public class TileInstance {

    private final TilePos position;
    private final Tile tile;
    private CompoundTag nbtData;

    public TileInstance(TilePos position, Tile tile) {
        this.position = position;
        this.tile = tile;
        this.nbtData = null;
    }

    public CompoundTag getOrCreateData() {
        if (nbtData == null) {
            nbtData = new CompoundTag();
        }
        return nbtData;
    }

    public CompoundTag getOrCreateSubData(String key) {
        getOrCreateData();
        CompoundTag subData = nbtData.getCompoundTag(key);
        if (subData == null) {
            subData = new CompoundTag();
            nbtData.put(key, subData);
        }
        return subData;
    }

    public TilePos getPosition() {
        return position;
    }

    public Tile getTile() {
        return tile;
    }

    public boolean isOf(Tile tile) {
        return this.tile.equals(tile);
    }

}
