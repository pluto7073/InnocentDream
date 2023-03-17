package io.innocentdream.resources;

import io.innocentdream.utils.Identifier;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BeforeLoadedProvider implements ResourceProvider {

    public static final BeforeLoadedProvider ASSETS = new BeforeLoadedProvider("assets");
    public static final BeforeLoadedProvider DATA = new BeforeLoadedProvider("data");

    private final String base;

    private BeforeLoadedProvider(String base) {
        this.base = base;
    }

    @Override
    public Resource get(Identifier id) {
        return new Resource(new DefaultStreamSupplier(getClass().getClassLoader().getResourceAsStream(idToPath(id))), null);
    }

    @Override
    public Set<String> getNamespaces() {
        return Set.of("innocentdream");
    }

    private String idToPath(Identifier id) {
        return base + "/" + id.getNamespace() + "/" + id.getPath();
    }

    private record DefaultStreamSupplier(InputStream stream) implements Supplier<InputStream> {
        @Override
        public InputStream get() {
            return stream;
        }
    }

}
