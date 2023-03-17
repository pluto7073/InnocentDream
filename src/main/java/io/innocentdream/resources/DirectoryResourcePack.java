package io.innocentdream.resources;

import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import io.innocentdream.InnocentDream;
import io.innocentdream.utils.Identifier;
import io.innocentdream.utils.JsonHelper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DirectoryResourcePack implements ResourcePack {

    private final File source;
    private HashMap<Identifier, Resource> assets;
    private HashMap<Identifier, Resource> data;
    private int targetVersion;
    private String description;

    public DirectoryResourcePack(File source) {
        this.source = source;
        this.targetVersion = 0;
        this.description = "";
        File pack = new File(source, "pack-info.id");
        if (!pack.exists()) {
            InnocentDream.LOGGER.error(pack.getPath() + " could not be found");
            return;
        }
        try {
            JsonObject packData = Streams.parse(new JsonReader(new FileReader(pack))).getAsJsonObject();
            this.targetVersion = JsonHelper.getInt(packData, "version");
            this.description = JsonHelper.getString(packData, "description");
        } catch (FileNotFoundException e) {
            InnocentDream.LOGGER.error(pack.getPath() + " could not be found", e);
        }
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
        HashMap<Identifier, Resource> resources = switch (type) {
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

    @Override
    public void reload() {
        File pack = new File(source, "pack-info.id");
        if (!pack.exists()) {
            InnocentDream.LOGGER.error(pack.getPath() + " could not be found");
            return;
        }
        try {
            JsonObject packData = Streams.parse(new JsonReader(new FileReader(pack))).getAsJsonObject();
            this.targetVersion = JsonHelper.getInt(packData, "version");
            this.description = JsonHelper.getString(packData, "description");
        } catch (FileNotFoundException e) {
            InnocentDream.LOGGER.error(pack.getPath() + " could not be found", e);
            return;
        }
        File assets = new File(source, "assets");
        if (assets.exists() && assets.isDirectory()) {
            try (Stream<Path> packFiles = Files.walk(assets.toPath())) {
                this.assets = new HashMap<>();
                Stream<Identifier> ids = packFiles.filter(p -> !p.endsWith(".idaccess") && Files.isRegularFile(p)).mapMulti((p, c) -> {
                    String relative = assets.toPath().relativize(p).toString().replace('\\', '/');
                    int i = relative.indexOf('/');
                    String namespace = relative.substring(0, i);
                    String path = relative.substring(i + 1);
                    c.accept(new Identifier(namespace, path));
                });
                ids.forEach(id -> {
                    File file = new File(source, "assets/" + id.getNamespace() + "/" + id.getPath());
                    Resource resource = new Resource(new DirectoryInputStreamProvider(file), this);
                    this.assets.put(id, resource);
                });
            } catch (IOException e) {
                InnocentDream.LOGGER.error("Failed to load assets from Resource Pack: " + source.getName(), e);
            }
        }
        File data = new File(source, "data");
        if (data.exists() && data.isDirectory()) {
            try (Stream<Path> packFiles = Files.walk(data.toPath())) {
                this.data = new HashMap<>();
                Stream<Identifier> ids = packFiles.filter(p -> !p.endsWith(".idaccess") && Files.isRegularFile(p)).mapMulti((p, c) -> {
                    String relative = data.toPath().relativize(p).toString().replace('\\', '/');
                    int i = relative.indexOf('/');
                    String namespace = relative.substring(0, i);
                    String path = relative.substring(i + 1);
                    c.accept(new Identifier(namespace, path));
                });
                ids.forEach(id -> {
                    File file = new File(source, "data/" + id.getNamespace() + "/" + id.getPath());
                    Resource resource = new Resource(new DirectoryInputStreamProvider(file), this);
                    this.data.put(id, resource);
                });
            } catch (IOException e) {
                InnocentDream.LOGGER.error("Failed to load data files from Resource Pack: " + source.getName());
            }
        }
    }

    public int getVersionDifference() {
        return this.targetVersion - InnocentDream.VERSION_NUMBER;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public Resource get(Identifier id) {
        return assets.containsKey(id) ? assets.get(id) : data.getOrDefault(id, null);
    }

    @Override
    public Set<String> getNamespaces() {
        ArrayList<String> namespaces = new ArrayList<>();
        for (Identifier i : assets.keySet()) {
            if (!namespaces.contains(i.getNamespace())) {
                namespaces.add(i.getNamespace());
            }
        }
        for (Identifier i : data.keySet()) {
            if (!namespaces.contains(i.getNamespace())) {
                namespaces.add(i.getNamespace());
            }
        }
        return Set.of(namespaces.toArray(new String[0]));
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

    public static boolean isValidResourcePack(File source) {
        return new File(source, "pack-info.id").exists();
    }

    private record DirectoryInputStreamProvider(File source) implements Supplier<InputStream> {
        @Override
        public InputStream get() {
            try {
                return new FileInputStream(source);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
