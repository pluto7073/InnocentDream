package io.innocentdream.utils;

import io.innocentdream.InnocentDream;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.util.Collections;
import java.util.function.Supplier;

import static org.lwjgl.BufferUtils.createByteBuffer;

public class Utils {

    public static <T> T make(Supplier<T> supplier) {
        return supplier.get();
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    public static ByteBuffer inputStreamToByteBuffer(InputStream stream) throws IOException {
        ByteBuffer buffer;

        ReadableByteChannel rbc = Channels.newChannel(stream);
        buffer = createByteBuffer(1);

        while (true) {
            int bytes = rbc.read(buffer);
            if ( bytes == -1 ) {
                break;
            }
            if (buffer.remaining() == 0) {
                buffer = resizeBuffer(buffer, buffer.capacity() * 2);
            }
        }

        buffer.flip();
        return buffer;
    }

    @SuppressWarnings("DuplicateExpressions")
    public static Path getPath(URI uri) throws IOException {
        try {
            return Paths.get(uri);
        } catch (FileSystemNotFoundException ignored) {
        } catch (Throwable e) {
            InnocentDream.LOGGER.warn("Unable to get path for: " + uri, e);
        }

        try {
            FileSystems.newFileSystem(uri, Collections.emptyMap());
        } catch (FileSystemAlreadyExistsException ignored) {
        }
        return Paths.get(uri);
    }

    public static String getNamespaceFromPath(String path) {
        StringBuilder namespaceBuilder = new StringBuilder();
        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            if (c == '/') {
                break;
            }
            namespaceBuilder.append(c);
        }
        return namespaceBuilder.toString();
    }

    public static <T> boolean contains(T[] array, T object) {
        for (T t : array) {
            if (t.equals(object)) return true;
        }
        return false;
    }

}
