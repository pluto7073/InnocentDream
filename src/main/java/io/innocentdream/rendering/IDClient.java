package io.innocentdream.rendering;

import io.innocentdream.InnocentDream;
import io.innocentdream.action.Actions;
import io.innocentdream.config.GamePropertyManager;
import io.innocentdream.math.Vec2d;
import io.innocentdream.registry.Registries;
import io.innocentdream.resources.BeforeLoadedProvider;
import io.innocentdream.resources.Resource;
import io.innocentdream.resources.ResourceManager;
import io.innocentdream.resources.shader.MainShader;
import io.innocentdream.scene.Scene;
import io.innocentdream.scene.WorldScene;
import io.innocentdream.utils.Identifier;
import io.innocentdream.utils.Utils;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.Stack;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

public class IDClient implements Runnable {

    private static IDClient INSTANCE = null;
    public static final int DEFAULT_WIDTH = 1024;
    public static final int DEFAULT_HEIGHT = 576;

    private final Thread thread;

    private long win = 0L;
    private int width, height;

    private long currentDisplayTick;
    private Stack<Scene> scenes;
    private MainShader mainShader;

    private IDClient() {
        this.thread = new Thread(this, "RenderThread");
        this.thread.start();
        Registries.register(Registries.EXIT_TASK, new Identifier("window"), this::cleanUp);
    }

    public static void create() {
        if (INSTANCE != null) {
            throw new IllegalStateException();
        }
        INSTANCE = new IDClient();
    }

    public int getWindowWidth() {
        return width;
    }

    public int getWindowHeight() {
        return height;
    }


    @Override
    public void run() {
        glfwSetErrorCallback((error, description) -> {
            InnocentDream.LOGGER.fatal(GLFWErrorCallback.getDescription(description) + ", Error Code: " + error);
            System.exit(error);
        });
        if (!glfwInit()) {
            int code = glfwGetError(null);
            InnocentDream.LOGGER.fatal("Failed To Initialize GLFW. Error Code: " + code);
            System.exit(code);
        }

        //TODO Loader & Renderer

        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode mode = glfwGetVideoMode(monitor);
        String windowTitle = "Innocent Dream - " + InnocentDream.VERSION;
        if (GamePropertyManager.getFullscreenProperty() && mode != null) {
            glfwWindowHint(GLFW_RED_BITS, mode.redBits());
            glfwWindowHint(GLFW_GREEN_BITS, mode.greenBits());
            glfwWindowHint(GLFW_BLUE_BITS, mode.blueBits());
            glfwWindowHint(GLFW_REFRESH_RATE, mode.refreshRate());
            this.width = mode.width();
            this.height = mode.height();
            win = glfwCreateWindow(this.width, this.height, windowTitle, monitor, 0);
        } else {
            this.width = DEFAULT_WIDTH;
            this.height = DEFAULT_HEIGHT;
            win = glfwCreateWindow(this.width, this.height, windowTitle, 0, 0);
        }

        ByteBuffer icon16, icon32;
        try {
            icon16 = Utils.inputStreamToByteBuffer(BeforeLoadedProvider.ASSETS.get(new Identifier("textures/gui/icon/icon-16.png")).getStream());
            icon32 = Utils.inputStreamToByteBuffer(BeforeLoadedProvider.ASSETS.get(new Identifier("textures/gui/icon/icon-32.png")).getStream());
        } catch (IOException e) {
            InnocentDream.LOGGER.fatal("Error occurred in loading icon", e);
            System.exit(1);
            return;
        }
        IntBuffer w = MemoryUtil.memAllocInt(1);
        IntBuffer h = MemoryUtil.memAllocInt(1);
        IntBuffer comp = MemoryUtil.memAllocInt(1);
        try (GLFWImage.Buffer icons = GLFWImage.malloc(2)) {
            ByteBuffer pixels16 = stbi_load_from_memory(icon16, w, h, comp, 4);
            icons.position(0)
                    .width(w.get(0))
                    .height(h.get(0))
                    .pixels(pixels16);
            ByteBuffer pixels32 = stbi_load_from_memory(icon32, w, h, comp, 4);
            icons.position(1)
                    .width(w.get(0))
                    .height(h.get(0))
                    .pixels(pixels32);
            icons.position(0);
            glfwSetWindowIcon(win, icons);
            stbi_image_free(pixels32);
            stbi_image_free(pixels16);
        }
        int width, height;
        float multiplier;
        ByteBuffer iconBuffer;
        try {
            Resource resource = BeforeLoadedProvider.ASSETS.get(new Identifier("textures/gui/mouse_pointer.png"));
            InputStream stream = resource.getStream();
            BufferedImage img = ImageIO.read(stream);
            width = img.getWidth();
            height = img.getHeight();
            //noinspection ConstantValue
            if (false) {
                float scale = mode.width() / 3840f;
                multiplier = scale * 8;
                BufferedImage after = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                AffineTransform at = new AffineTransform();
                at.scale(scale, scale);
                AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                after = op.filter(img, after);
                width = after.getWidth();
                height = after.getHeight();
                img = after;
            } else {
                multiplier = 8;
            }
            int[] pixels = new int[width * height];
            img.getRGB(0, 0, width, height, pixels, 0, width);

            iconBuffer = BufferUtils.createByteBuffer(width * height * 4);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = pixels[y * width + x];

                    iconBuffer.put((byte) ((pixel >> 16) & 0xFF));
                    iconBuffer.put((byte) ((pixel >> 8) & 0xFF));
                    iconBuffer.put((byte) (pixel & 0xFF));
                    iconBuffer.put((byte) ((pixel >> 24) & 0xFF));
                }
            }
            iconBuffer.flip();
        } catch (IOException e) {
            InnocentDream.LOGGER.fatal("Error in loading the mouse pointer", e);
            System.exit(1);
            return;
        }

        GLFWImage cursor = GLFWImage.create();
        cursor.width(width);
        cursor.height(height);
        cursor.pixels(iconBuffer);

        long cursorId = glfwCreateCursor(cursor, 3 * 8, 2 * 8);
        glfwSetCursor(win, cursorId);

        glfwSetWindowSizeCallback(win, (win, wi, hi) -> {
            glViewport(0, 0, wi, hi);
            IDClient.this.width = wi;
            IDClient.this.height = hi;
        });

        glfwMakeContextCurrent(win);
        GL.createCapabilities();
        glfwMakeContextCurrent(win);
        glfwShowWindow(win);
        mainShader = new MainShader();
        glClearColor(0, 0, 0, 0);
        glClear(GL_COLOR_BUFFER_BIT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        ResourceManager.init();
        this.currentDisplayTick = 0L;
        this.scenes = new Stack<>();
        this.scenes.add(new WorldScene(this));
        loop();
    }

    public void goFullscreen() {
        glfwMakeContextCurrent(win);
        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode mode = glfwGetVideoMode(monitor);
        if (mode == null) {
            return;
        }
        width = mode.width();
        height = mode.height();
        glfwSetWindowMonitor(win, monitor, 0, 0, width, height, mode.refreshRate());
    }

    public void goWindowed() {
        glfwMakeContextCurrent(win);
        long monitor = glfwGetWindowMonitor(win);
        GLFWVidMode mode = glfwGetVideoMode(monitor);
        double scale = GamePropertyManager.getScaleProperty();
        Dimension dim = new Dimension(mode.width(), mode.height());
        width = (int) (DEFAULT_WIDTH * scale);
        height = (int) (678 * scale);
        int x = dim.width / 2 - width / 2;
        int y = dim.height / 2 - height / 2;
        glfwSetWindowMonitor(win, 0, x, y, width, height, 1);
    }

    public void toggleFullscreen() {
        boolean fullscreen = GamePropertyManager.getFullscreenProperty();
        if (fullscreen) {
            goWindowed();
        } else {
            goFullscreen();
        }
        GamePropertyManager.saveProperty("fullscreen", String.valueOf(!fullscreen));
    }

    public void cleanUp() {
        mainShader.cleanUp();
    }

    public void loop() {
        Timer.init();
        while (!glfwWindowShouldClose(win)){
            glfwMakeContextCurrent(win);

            scenes.peek().drawBackground();

            glfwPollEvents();

            try {
                scenes.peek().tick();
            } catch (Exception e) {
                InnocentDream.LOGGER.error("Exception in ticking scene " + scenes.peek().name, e);
            }

            //TODO prepare renderer
            mainShader.start();

            try {
                scenes.peek().draw();
            } catch (Exception e) {
                InnocentDream.LOGGER.error("Exception in drawing scene " + scenes.peek().name, e);
            }

            Actions.pollActions(scenes.peek(), this);
            mainShader.stop();
            //TODO start gui shader

            try {
                scenes.peek().drawGUI();
            } catch (Exception e) {
                InnocentDream.LOGGER.error("Exception in drawing gui for scene " + scenes.peek().name, e);
            }

            //TODO stop gui shader

            //InnocentDream.LOGGER.debug(getWindowMousePosition().toString());

            glfwSwapBuffers(win);

            Timer.updateTime();
            currentDisplayTick++;
        }
        InnocentDream.IS_RUNNING = false;
        InnocentDream.cleanUp(this);
    }

    /**
     * @return The current position of the mouse relative to the window in GUI coordinates
     */
    public Vec2d getWindowMousePosition() {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        glfwGetCursorPos(win, x, y);
        glfwGetWindowSize(win, width, height);
        double xx = x.get();
        double yy = y.get();
        return new Vec2d(xx, yy);
    }

    public long getWindowID() {
        return this.win;
    }

    public static IDClient getInstance() {
        return INSTANCE;
    }
}
