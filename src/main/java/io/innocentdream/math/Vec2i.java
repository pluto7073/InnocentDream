package io.innocentdream.math;

public class Vec2i extends Vec2<Integer> {

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void multiply(Vec2<Integer> vector) {
        this.x *= vector.x;
        this.y *= vector.y;
    }

    @Override
    public void add(Vec2<Integer> vector) {
        this.x += vector.x;
        this.y += vector.y;
    }

    @Override
    public void subtract(Vec2<Integer> vector) {
        this.x -= vector.x;
        this.y -= vector.y;
    }

    @Override
    public void divide(Vec2<Integer> vector) {
        this.x /= vector.x;
        this.y /= vector.y;
    }
}
