package io.innocentdream.tile;

import io.innocentdream.math.TilePos;
import io.innocentdream.registry.Registries;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.Tag;

public class TileInstance {

    private final Tile tile;
    private CompoundTag nbtData;

    public TileInstance(Tile tile) {
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

    public CompoundTag serialize() {
        CompoundTag data = new CompoundTag();
        data.putString("tile", Registries.TILE.getId(tile).toString());
        data.put("nbt", nbtData);
        return data;
    }

    public Tile getTile() {
        return tile;
    }

    public boolean isOf(Tile tile) {
        return this.tile.equals(tile);
    }

}
