package io.innocentdream.math;

import org.jetbrains.annotations.NotNull;

public abstract class Vec2<T extends Number> extends BiContainer<T, T> implements Comparable<Vec2<?>> {

    public Vec2(T x, T y) {
        super(x, y);
    }

    public Vec2() {}

    public abstract void multiply(Vec2<T> vector);
    public abstract void add(Vec2<T> vector);
    public abstract void subtract(Vec2<T> vector);
    public abstract void divide(Vec2<T> vector);



    public static <T extends Number, R extends Number> void convert(Vec2<T> source, Vec2<R> target) {
        target.x = (R) source.x;
        target.y = (R) source.y;
    }

    @Override
    public int compareTo(@NotNull Vec2<?> o) {
        int i = Float.compare(this.x.floatValue(), o.x.floatValue());
        if (i == 0) {
            i = Float.compare(this.y.floatValue(), o.y.floatValue());
        }
        return i;
    }
}
