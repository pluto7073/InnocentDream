package io.innocentdream.resources;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Supplier;

public class Resource {

    private final Supplier<InputStream> supplier;
    private final ResourcePack resourcePack;

    public Resource(Supplier<InputStream> supplier, @Nullable ResourcePack resourcePack) {
        this.supplier = supplier;
        this.resourcePack = resourcePack;
    }

    public ResourcePack getResourcePack() {
        return resourcePack;
    }

    public InputStream getStream() throws IOException {
        try {
            return this.supplier.get();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getStream()));
    }

}
