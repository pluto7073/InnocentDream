package io.innocentdream.rendering;

import io.innocentdream.registry.Registries;
import io.innocentdream.utils.Identifier;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;

public class Model {

    private static final List<Model> MODELS = new ArrayList<>();

    private final int drawCount, verticesId, textureId, indicesId, vertexSize;

    private float[] vertices;

    public Model(float[] vertices, float[] textureCoordinates, int[] indices, int vertexSize) {
        this.drawCount = indices.length;

        this.verticesId = glGenBuffers();
        this.textureId = glGenBuffers();
        this.indicesId = glGenBuffers();
        this.vertexSize = vertexSize;

        this.vertices = vertices;

        glBindBuffer(GL_ARRAY_BUFFER, this.verticesId);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(vertices), GL_DYNAMIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, this.textureId);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(textureCoordinates), GL_STATIC_DRAW);

        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.indicesId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        MODELS.add(this);
    }

    public void updateVertices(float[] newVertices) {
        if (newVertices.length != vertices.length) {
            throw new IllegalArgumentException();
        }
        if (Arrays.equals(vertices, newVertices)) {
            return;
        }
        this.vertices = newVertices;
        glBindBuffer(GL_ARRAY_BUFFER, this.verticesId);
        glBufferSubData(GL_ARRAY_BUFFER, 0, createFloatBuffer(newVertices));
    }

    public void draw() {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, verticesId);
        glVertexAttribPointer(0, vertexSize, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, textureId);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesId);
        glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesId);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }

    private static FloatBuffer createFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public static void cleanUp() {
        for (Model m : MODELS) {
            glDeleteBuffers(m.textureId);
            glDeleteBuffers(m.verticesId);
        }
    }

    static {
        Registries.register(Registries.EXIT_TASK, new Identifier("models"), Model::cleanUp);
    }

}
