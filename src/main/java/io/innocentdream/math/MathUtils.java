package io.innocentdream.math;

import io.innocentdream.rendering.IDClient;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.nio.FloatBuffer;

public final class MathUtils {

    private static final Vec2d GUI_SIZE = new Vec2d(1024, 576);
    private static final Vec2d RENDER_RANGE = new Vec2d(-1, 1);

    private MathUtils() { throw new UnsupportedOperationException(); }

    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.translate(new Vector3f(translation, 0));
        matrix.scale(new Vector3f(scale, 1f));
        return matrix;
    }

    public static void storeMatrixInFloatBuffer(Matrix4f matrix, FloatBuffer buffer) {
        buffer.put(matrix.m00()).put(matrix.m01()).put(matrix.m02()).put(matrix.m03())
                .put(matrix.m10()).put(matrix.m11()).put(matrix.m12()).put(matrix.m13())
                .put(matrix.m20()).put(matrix.m21()).put(matrix.m22()).put(matrix.m23())
                .put(matrix.m30()).put(matrix.m31()).put(matrix.m32()).put(matrix.m33());
    }

    /**
     * Scales a value to a target range<br><br>
     * Example: <br>
     * If the value is 10, the <code>currentRange</code> is 0 to 20, and the <code>targetRange</code> is 0 to 100,
     * then the resulting value will be 50
     * @param value the value to scale
     * @param currentRange the current range of the value, can be any range that contains the specified value
     * @param targetRange the range to scale the value to, doesn't have to contain the specified value
     * @return The scaled value
     */
    public static double scale(double value, Vec2d currentRange, Vec2d targetRange) {
        double currentSize = currentRange.y - currentRange.x;
        double targetSize = targetRange.y - targetRange.x;
        double scale = targetSize / currentSize;
        return (scale * (value - currentRange.x)) + targetRange.x;
    }

    /**
     * Converts GUI coordinates (X -512 to 512, Y -288 to 288) to Render Coordinates (-1 to 1)
     * @param coords The coordinates to convert
     * @param client The current instance of <code>IDClient</code>
     * @return The converted coordinates
     */
    public static Vec2d guiToRenderCoords(Vec2d coords, IDClient client) {
        Vec2d windowSize = findDesiredSize(client);
        Vec2d windowXRange = new Vec2d(-(windowSize.x) / 2., windowSize.x / 2.);
        Vec2d windowYRange = new Vec2d(-(windowXRange.y) / 2., windowXRange.y / 2.);
        double windowScaledX = scale(coords.x,
                new Vec2d(-(GUI_SIZE.x) / 2.0, GUI_SIZE.x / 2.0), windowXRange);
        double windowScaledY = scale(coords.y,
                new Vec2d(-(GUI_SIZE.y) / 2.0, GUI_SIZE.y / 2.0), windowYRange);
        return windowToRenderCoords(new Vec2d(windowScaledX, windowScaledY), client);
    }

    public static Vec2d windowToRenderCoords(Vec2d coords, IDClient client) {
        double renderX = scale(coords.x, new Vec2d(-client.getWindowWidth() / 2., client.getWindowWidth() / 2.), RENDER_RANGE);
        double renderY = scale(coords.y, new Vec2d(-client.getWindowHeight() / 2., client.getWindowHeight() / 2.), RENDER_RANGE);
        return new Vec2d(renderX, renderY);
    }


    public static Vec2d windowToGuiCoords(Vec2d coords, IDClient client) {
        Vec2d windowSize = findDesiredSize(client);
        Vec2d windowXRange = new Vec2d(-(windowSize.x) / 2., windowSize.x / 2.);
        Vec2d windowYRange = new Vec2d(-(windowXRange.y) / 2., windowXRange.y / 2.);
        double x = scale(coords.x, windowXRange, new Vec2d(-GUI_SIZE.x / 2, GUI_SIZE.x / 2));
        double y = scale(coords.y, windowYRange, new Vec2d(-GUI_SIZE.y / 2, GUI_SIZE.y / 2));
        return new Vec2d(x, y);
    }

    private static Vec2d findDesiredSize(IDClient client) {
        double width = client.getWindowWidth();
        double height = client.getWindowHeight();
        if (width / height > 16.0/9.0) {
            double desiredWidth = height * (16.0/9.0);
            return new Vec2d(desiredWidth, height);
        } else if (width / height < 16.0/9.0) {
            double desiredHeight = width * (9.0/16.0);
            return new Vec2d(width, desiredHeight);
        }
        return new Vec2d(width, height);
    }

    /**
     * Creates a vector that represents the coordinates of an angle
     * @param angle the angle in degrees
     * @return a vector with values from 0.0 to 1.0
     */
    public static Vec2d vectorFromAngle(double angle) {
        double x = Math.cos(Math.toRadians(angle));
        if (x <= 1E-10) x = 0.0;
        double y = Math.sin(Math.toRadians(angle));
        if (y <= 1E-10) y = 0.0;
        return new Vec2d(x, y);
    }

}
