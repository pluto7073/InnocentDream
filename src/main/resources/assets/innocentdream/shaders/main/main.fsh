#version 400 core

in vec2 pass_texture_coordinates;

out vec4 out_Colour;

uniform sampler2D texture_sampler;

void main() {

    vec4 textureColour = texture(texture_sampler, pass_texture_coordinates);

    if (textureColour.a == 0.0) {
        discard;
    }

    out_Colour = textureColour;

}
