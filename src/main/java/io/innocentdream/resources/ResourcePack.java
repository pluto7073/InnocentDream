package io.innocentdream.resources;

import io.innocentdream.utils.Identifier;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

public interface ResourcePack extends ResourceProvider {

    Resource getAsset(Identifier id);
    Resource getData(Identifier id);
    Map<Identifier, Resource> findResources(ResourceType type, String basePath, Predicate<Identifier> allowed);
    Collection<Resource> getAllResources(ResourceType type);
    void reload();
    boolean hasResource(Identifier id);
    boolean hasAsset(Identifier id);
    boolean hasData(Identifier id);

}
