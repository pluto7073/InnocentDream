package io.innocentdream.registry;

import io.innocentdream.utils.Identifier;

public class ValueAlreadyRegisteredException extends RuntimeException {

    public ValueAlreadyRegisteredException(Identifier id, Registry<?> registry) {
        super(String.format("Tried to register value under %s to Registry: %s", id.toString(), registry.getName()));
    }

}
