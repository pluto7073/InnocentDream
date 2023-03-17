#version 400 core

in vec3 position;
in vec2 texture_coordinates;

uniform mat4 transformationMatrix;

out vec2 pass_texture_coordinates;

void main() {

    gl_Position = vec4(position, 1.0);
    pass_texture_coordinates = texture_coordinates;

}