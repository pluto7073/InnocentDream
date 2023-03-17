package io.innocentdream.resources;

public enum ResourceType {

    ASSETS("assets"), DATA("data");

    private final String dir;
    ResourceType(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }

}
