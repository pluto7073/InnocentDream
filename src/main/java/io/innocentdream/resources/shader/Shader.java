package io.innocentdream.resources.shader;

import io.innocentdream.math.MathUtils;
import io.innocentdream.resources.BeforeLoadedProvider;
import io.innocentdream.resources.Resource;
import io.innocentdream.utils.Identifier;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public abstract class Shader {

    protected static final Logger LOGGER = LogManager.getLogger(Shader.class);

    private final int programId, vertexId, fragmentId;
    protected final String name;

    public Shader(String name) {
        this(name, "innocentdream");
    }

    public Shader(String name, String namespace) {
        this.name = name;
        this.vertexId = loadShader(new Identifier(namespace, String.format("shaders/%s/%s.vsh", name, name)), GL_VERTEX_SHADER);
        this.fragmentId = loadShader(new Identifier(namespace, String.format("shaders/%s/%s.fsh", name, name)), GL_FRAGMENT_SHADER);
        programId = glCreateProgram();
        glAttachShader(programId, vertexId);
        glAttachShader(programId, fragmentId);
        bindAttributes();
        glLinkProgram(programId);
        glValidateProgram(programId);
        getAllUniformLocations();
    }

    protected abstract void bindAttributes();
    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String name) {
        return glGetUniformLocation(programId, name);
    }

    protected void loadMatrix(int location, Matrix4f matrix) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        MathUtils.storeMatrixInFloatBuffer(matrix, buffer);
        buffer.flip();
        glUniformMatrix4fv(location, false, buffer);
    }

    protected void bindAttribute(int attribute, String name) {
        glBindAttribLocation(programId, attribute, name);
    }

    public void start() {
        glUseProgram(programId);
    }

    public void stop() {
        glUseProgram(0);
    }

    public void cleanUp() {
        stop();
        glDetachShader(programId, vertexId);
        glDetachShader(programId, fragmentId);
        glDeleteShader(vertexId);
        glDeleteShader(fragmentId);
        glDeleteProgram(programId);
    }

    private static int loadShader(Identifier file, int type) {
        StringBuilder source = new StringBuilder();
        try {
            Resource shaderRes = BeforeLoadedProvider.ASSETS.get(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(shaderRes.getStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                source.append(line).append('\n');
            }
            reader.close();
        } catch (IOException e) {
            LOGGER.error("Could not load shader: " + file.toString(), e);
        }
        int shaderId = glCreateShader(type);
        glShaderSource(shaderId, source.toString());
        glCompileShader(shaderId);
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            LOGGER.error("Could not compile shader: " + file.toString());
            LOGGER.error(glGetShaderInfoLog(shaderId, 500));
        }
        return shaderId;
    }

}
