package io.innocentdream.rendering;

import io.innocentdream.InnocentDream;
import io.innocentdream.registry.Registries;
import io.innocentdream.resources.Resource;
import io.innocentdream.resources.ResourceManager;
import io.innocentdream.utils.Identifier;
import org.apache.log4j.LogManager;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public final class TextureHelper {

    private TextureHelper() { throw new UnsupportedOperationException(); }

    private static final HashMap<Identifier, Texture> LOADED_TEXTURES = new HashMap<>();

    public static Texture getTexture(Identifier id) {
        if (LOADED_TEXTURES.containsKey(id)) {
            return LOADED_TEXTURES.get(id);
        }
        Identifier resId = new Identifier(id.getNamespace(), "textures/" + id.getPath() + ".png");
        Resource resource = ResourceManager.getAsset(resId);
        if (resource == ResourceManager.MISSING_TEXTURE) {
            LogManager.getLogger(TextureHelper.class).error("Could not find texture: " + id, new FileNotFoundException(id.toString()));
        }
        Texture tex = new Texture(resource);
        LOADED_TEXTURES.put(id, tex);
        return tex;
    }

    public static Texture loadFromInputStream(Identifier resultId, InputStream stream) {
        Resource resource = new Resource(() -> stream, null);
        if (stream == null) {
            resource = ResourceManager.MISSING_TEXTURE;
        }
        Texture texture = new Texture(resource);
        LOADED_TEXTURES.put(resultId, texture);
        return texture;
    }

    public static void cleanUp() {
        for (Texture t : LOADED_TEXTURES.values()) {
            t.cleanUp();
        }
    }

    static {
        Registries.register(Registries.EXIT_TASK, new Identifier("textures"), TextureHelper::cleanUp);
    }

}
