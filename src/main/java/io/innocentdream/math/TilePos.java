package io.innocentdream.math;

import org.jetbrains.annotations.NotNull;

public class TilePos extends Vec2i {

    public int level;

    /**
     * Creates a new Tile Position
     * @param x the x value
     * @param y the y value
     * @param level the level of the position, one of:<br>
     *              -1 - The floor level<br>
     *              0+ - The object level<br>
     */
    public TilePos(int x, int y, int level) {
        super(x, y);
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof TilePos pos)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        return this.x.equals(pos.x) && this.y.equals(pos.y) && pos.level == this.level;
    }

    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + "," + this.level + ")";
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 31 + Integer.hashCode(this.level);
    }

    @Override
    public int compareTo(@NotNull Vec2<?> o) {
        int i = super.compareTo(o);
        if (o instanceof TilePos pos && i == 0) {
            i = Integer.compare(level, pos.level);
        }
        return i;
    }
}
