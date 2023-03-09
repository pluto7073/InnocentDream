package io.innocentdream.rendering;

public class WindowManager implements Runnable {

    private WindowManager() {}

    public static WindowManager create() {
        WindowManager manager = new WindowManager();
        return manager;
    }


    @Override
    public void run() {

    }
}
