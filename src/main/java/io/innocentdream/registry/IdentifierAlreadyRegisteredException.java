package io.innocentdream.registry;

import io.innocentdream.utils.Identifier;

public class IdentifierAlreadyRegisteredException extends RuntimeException {

    public IdentifierAlreadyRegisteredException(Identifier id) {
        super(id.toString());
    }

}
