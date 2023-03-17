package io.innocentdream.math;

public class Vec2f extends Vec2<Float> {

    public Vec2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void multiply(Vec2<Float> vector) {
        this.x *= vector.x;
        this.y *= vector.y;
    }

    @Override
    public void add(Vec2<Float> vector) {
        this.x += vector.x;
        this.y += vector.y;
    }

    @Override
    public void subtract(Vec2<Float> vector) {
        this.x -= vector.x;
        this.y -= vector.y;
    }

    @Override
    public void divide(Vec2<Float> vector) {
        this.x /= vector.x;
        this.y /= vector.y;
    }

}
