package io.innocentdream.action;

import io.innocentdream.rendering.IDClient;
import io.innocentdream.scene.Scene;

public abstract class Action {

    private final String propertiesKey;
    private final int defaultKey;
    public boolean active;

    public Action(String propertiesKey, int defaultKey) {
        this.propertiesKey = propertiesKey;
        this.defaultKey = defaultKey;
        this.active = false;
    }

    public abstract void doAction(IDClient client);
    public abstract void afterActionPerformed(IDClient client);
    public abstract boolean canUse(Scene scene);

    public String getPropertiesKey() {
        return propertiesKey;
    }
    public int getDefaultKey() {
        return defaultKey;
    }

}
