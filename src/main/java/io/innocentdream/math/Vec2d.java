package io.innocentdream.math;

public class Vec2d extends Vec2<Double> {

    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public void multiply(Vec2<Double> vector) {
        this.x *= vector.x;
        this.y *= vector.y;
    }

    @Override
    public void add(Vec2<Double> vector) {
        this.x += vector.x;
        this.y += vector.y;
    }

    @Override
    public void subtract(Vec2<Double> vector) {
        this.x -= vector.x;
        this.y -= vector.y;
    }

    @Override
    public void divide(Vec2<Double> vector) {
        this.x /= vector.x;
        this.y /= vector.y;
    }
}
