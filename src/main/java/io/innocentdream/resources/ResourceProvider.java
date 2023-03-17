package io.innocentdream.resources;

import io.innocentdream.utils.Identifier;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public interface ResourceProvider {

    Resource get(Identifier id);
    Set<String> getNamespaces();

}
