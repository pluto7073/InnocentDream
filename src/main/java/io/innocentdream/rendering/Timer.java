package io.innocentdream.rendering;

public class Timer {

    private static long lastVal, difference;

    public static void init() {
        lastVal = System.currentTimeMillis();
    }

    public static void updateTime() {
        long now = System.currentTimeMillis();
        difference = now - lastVal;
        lastVal = now;
    }

    public static long getDifference() {
        return difference;
    }

    /**
     * Finds the rate in tiles per tick to move using the current tick length so that the desired rate specified is reached
     * @param rate The preferred rate in Tiles per Second
     * @return The rate in tiles per tick
     */
    public static double rateFromTickLength(double rate) {
        double seconds = difference / 1000.0;
        return rate * seconds;
    }

}
