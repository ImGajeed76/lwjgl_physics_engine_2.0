#version 400 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoord;
layout (location = 2) in vec3 normal;

out vec2 passTex;

uniform mat4 transformWorld;
uniform mat4 transformObject;
uniform mat4 cameraProjection;


void main() {
    gl_Position = cameraProjection * transformWorld * transformObject * vec4(position, 1);

    passTex = textureCoord;
}