package io.innocentdream.math;

public class AABB {

    public Vec2f pos, size;

    public AABB(int x, int y, int w, int h) {
        this(new Vec2f(x, y), new Vec2f(w, h));
    }

    public AABB(Vec2f pos, Vec2f size) {
        this.pos = pos;
        this.size = size;
    }

    public boolean test(AABB object) {
        return this.pos.x < object.pos.x + object.size.x &&
                this.pos.x + this.size.x > object.size.x &&
                this.pos.y < object.pos.y + object.size.y &&
                this.pos.y + this.size.y > object.pos.y;
    }

}
