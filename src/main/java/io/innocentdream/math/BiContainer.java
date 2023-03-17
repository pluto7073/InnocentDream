package io.innocentdream.math;

public class BiContainer<X,Y> extends Container<X> {

    public Y y;

    public BiContainer(X x, Y y) {
        super(x);
        this.y = y;
    }

    public BiContainer() {}

    @Override
    public String toString() {
        return "(" + super.toString() + "," + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof BiContainer<?, ?> container)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        return this.x.equals(container.x) && this.y.equals(container.y);
    }
}
