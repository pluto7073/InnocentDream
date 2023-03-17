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

    /**
     * Tests if the two BiContainers are equal. Two BiContainers are equal if their
     * two contained values are equal and are stored in the same order
     * @param o The object to test against
     * @return If they are equal
     */
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

    @Override
    public int hashCode() {
        return 31 * this.x.hashCode() + this.y.hashCode();
    }
}
