package io.innocentdream.resources.shader;

public class GUIShader extends Shader {

    public GUIShader() {
        super("gui");
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "texture_coordinates");
    }

    @Override
    protected void getAllUniformLocations() {

    }

}
