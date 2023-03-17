#version 400 core

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D texture_sampler;

void main() {

    vec4 textureColour = texture(texture_sampler, textureCoords);

    if (textureColour.a == 0.0) {
        discard;
    }

    out_Color = texture(texture_sampler, textureCoords);

}