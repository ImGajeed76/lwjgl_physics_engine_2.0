#version 400 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 color;
layout (location = 2) in vec2 textureCoord;
layout (location = 3) in vec3 vertexNormal;

out vec3 passColor;
out vec2 passTexCoord;

out vec3 mvVertexNormal;
out vec3 mvVertexPos;

uniform mat4 transformWorld;
uniform mat4 transformObject;
uniform mat4 cameraProjection;


void main() {
    passColor = color;

    mat4 modelViewMatrix = transformWorld * transformObject;
    vec4 mvPos = modelViewMatrix * vec4(position, 1);
    gl_Position = cameraProjection * mvPos;

    mvVertexNormal = normalize(modelViewMatrix * vec4(vertexNormal, 0.0)).xyz;
    mvVertexPos = mvPos.xyz;
}