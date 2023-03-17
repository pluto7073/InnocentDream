package io.innocentdream.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.innocentdream.utils.Identifier;

import java.util.*;
import java.util.function.BiConsumer;

public abstract class Registry<T> {

    protected final String name;
    protected final HashMap<Identifier, T> map;

    Registry(String name) {
        this.name = name;
        this.map = new HashMap<>();
    }

    public abstract T getDefaultValue();

    public String getName() {
        return name;
    }

    T register(Identifier id, T value) {
        if (map.containsKey(id)) {
            throw new IdentifierAlreadyRegisteredException(id);
        }
        if (map.containsValue(value)) {
            throw new ValueAlreadyRegisteredException(id, this);
        }
        map.put(id, value);
        return value;
    }

    public T getValue(Identifier id) {
        if (!map.containsKey(id)) {
            return getDefaultValue();
        }
        return map.get(id);
    }

    public String getTranslationKey(Identifier id) {
        return id.toTranslationKey(name);
    }

    public String getTranslationKey(T value) {
        Identifier identifier = getId(value);
        return getTranslationKey(identifier);
    }

    public Identifier getId(T value) {
        for (Map.Entry<Identifier, T> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        throw new UnregisteredValueException();
    }

    public Collection<T> values() {
        return map.values();
    }

    public Set<Identifier> keys() {
        return map.keySet();
    }


    public void forEach(BiConsumer<? super Identifier, ? super T> consumer) {
        map.forEach(consumer);
    }

    public JsonArray toJson() {
        JsonArray array = new JsonArray();
        for (Identifier id : map.keySet()) {
            array.add(id.toString());
        }
        return array;
    }

}
