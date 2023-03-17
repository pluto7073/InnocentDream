package io.innocentdream.math;

public class Container<T> {

    public T x;

    public Container(T value) {
        this.x = value;
    }

    public Container() {
        this.x = null;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Container<?> container)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        return this.x.equals(container.x);
    }

    @Override
    public String toString() {
        return x.toString();
    }
}
