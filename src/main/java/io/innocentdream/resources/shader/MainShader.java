package io.innocentdream.resources.shader;

import org.joml.Matrix4f;

public class MainShader extends Shader {

    private int locationTransformationMatrix;

    public MainShader() {
        super("main");
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "texture_coordinates");
    }

    @Override
    protected void getAllUniformLocations() {
        this.locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    public void setTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(locationTransformationMatrix, matrix);
    }
}
