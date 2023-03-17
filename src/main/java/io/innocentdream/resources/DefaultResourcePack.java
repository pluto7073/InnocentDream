package io.innocentdream.resources;

import io.innocentdream.InnocentDream;
import io.innocentdream.utils.Identifier;
import io.innocentdream.utils.Utils;
import com.google.common.collect.ImmutableMap;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DefaultResourcePack implements ResourcePack {

    private static final Logger LOGGER = LogManager.getLogger(DefaultResourcePack.class);
    private static final Map<ResourceType, Path> RESOURCE_TYPE_PATHS = Utils.make(() -> {
        synchronized (DefaultResourcePack.class) {
            ImmutableMap.Builder<ResourceType, Path> builder = ImmutableMap.builder();

            for (ResourceType type : ResourceType.values()) {
                String path = "/" + type.getDir() + "/.idaccess";
                URL url = DefaultResourcePack.class.getResource(path);
                if (url == null) {
                    LOGGER.error(path + " does not exist");
                } else {
                    try {
                        URI uri = url.toURI();
                        String scheme = uri.getScheme();
                        if (!"jar".equals(scheme) && !"file".equals(scheme)) {
                            LOGGER.warn("URL '" + uri + "' uses unexpected schema");
                        }
                        Path p = Utils.getPath(uri);
                        builder.put(type, p.getParent());
                    } catch (URISyntaxException | IOException e) {
                        LOGGER.error("Could not load path of default resources");
                    }
                }
            }
            return builder.build();
        }
    });
    public static final DefaultResourcePack INSTANCE = new DefaultResourcePack();

    private Map<Identifier, Resource> assets;
    private Map<Identifier, Resource> data;

    private DefaultResourcePack() {
        reload();
    }

    @Override
    public Resource getAsset(Identifier id) {
        return assets.get(id);
    }

    @Override
    public Resource getData(Identifier id) {
        return data.get(id);
    }

    @Override
    public Map<Identifier, Resource> findResources(ResourceType type, String basePath, Predicate<Identifier> allowed) {
        Map<Identifier, Resource> resources = switch (type) {
            case ASSETS -> assets;
            case DATA -> data;
        };
        Map<Identifier, Resource> results = new HashMap<>();
        for (Map.Entry<Identifier, Resource> e : resources.entrySet()) {
            if (!e.getKey().getPath().startsWith(basePath)) {
                continue;
            }
            if (allowed.test(e.getKey())) {
                results.put(e.getKey(), e.getValue());
            }
        }
        return results;
    }

    @Override
    public Collection<Resource> getAllResources(ResourceType type) {
        return switch (type) {
            case ASSETS -> assets.values();
            case DATA -> data.values();
        };
    }

    private Map<Identifier, Resource> getMapForResType(ResourceType type) {
        return switch (type) {
            case ASSETS -> this.assets;
            case DATA -> this.data;
        };
    }

    @Override
    public void reload() {
        this.assets = new HashMap<>();
        this.data = new HashMap<>();
        for (Map.Entry<ResourceType, Path> entry : RESOURCE_TYPE_PATHS.entrySet()) {
            try (Stream<Path> stream = Files.walk(entry.getValue())) {
                Stream<Identifier> ids = stream.filter(path -> !path.endsWith(".idaccess") && Files.isRegularFile(path)).mapMulti((path, consumer) -> {
                    String relative = entry.getValue().relativize(path).toString().replace('\\', '/');
                    int i = relative.indexOf('/');
                    String namespace = relative.substring(0, i);
                    String pathAfter = relative.substring(i + 1);
                    consumer.accept(new Identifier(namespace, pathAfter));
                });
                ids.forEach(id -> {
                    Resource r = new Resource(new DefaultStreamGetter(entry.getKey().getDir() + "/"
                            + id.getNamespace() + "/" + id.getPath()), this);
                    getMapForResType(entry.getKey()).put(id, r);
                });
            } catch (IOException e) {
                LOGGER.error("Could not load resources", e);
            }
        }
    }

    @Override
    public Resource get(Identifier id) {
        if (assets.containsKey(id)) {
            return assets.get(id);
        } else return data.getOrDefault(id, null);
    }

    @Override
    public Set<String> getNamespaces() {
        return Set.of("innocentdream");
    }

    @Override
    public boolean hasResource(Identifier id) {
        return assets.containsKey(id) || data.containsKey(id);
    }

    @Override
    public boolean hasAsset(Identifier id) {
        return assets.containsKey(id);
    }

    @Override
    public boolean hasData(Identifier id) {
        return data.containsKey(id);
    }

    private record DefaultStreamGetter(String path) implements Supplier<InputStream> {
        @Override
        public InputStream get() {
            return getClass().getClassLoader().getResourceAsStream(path);
        }
    }
}
