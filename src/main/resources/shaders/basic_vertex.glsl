#version 400 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 color;
layout (location = 2) in vec2 textureCoord;

out vec3 passColor;

uniform mat4 transformWorld;
uniform mat4 transformObject;
uniform mat4 cameraProjection;


void main() {
    passColor = color;
    gl_Position = cameraProjection * transformWorld * transformObject * vec4(position, 1);
}