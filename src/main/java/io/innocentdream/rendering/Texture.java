package io.innocentdream.rendering;

import io.innocentdream.resources.Resource;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Texture {

    private final int width, height, id;

    Texture(Resource resource) {
        BufferedImage img;
        try {
            img = ImageIO.read(resource.getStream());
            this.width = img.getWidth();
            this.height = img.getHeight();

            int[] pixelsRaw = new int[width * height * 4];
            img.getRGB(0, 0, width, height, pixelsRaw, 0, width);

            ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int p = pixelsRaw[i * width + j];
                    pixels.put((byte) ((p >> 16) & 0xFF));
                    pixels.put((byte) ((p >> 8) & 0xFF));
                    pixels.put((byte) (p & 0xFF));
                    pixels.put((byte) ((p >> 24) & 0xFF));
                }
            }

            pixels.flip();

            this.id = glGenTextures();

            glBindTexture(GL_TEXTURE_2D, id);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void bind(int sampler) {
        if (sampler < 0 || sampler > 31) {
            return;
        }
        glActiveTexture(GL_TEXTURE0 + sampler);
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void cleanUp() {
        glDeleteTextures(id);
    }

}
