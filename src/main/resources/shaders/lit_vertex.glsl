#version 400 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 color;
layout (location = 2) in vec2 textureCoord;
layout (location = 3) in vec3 vertexNormal;

out vec3 passColor;
out vec2 passTexCoord;

out vec3 fragNormal;
out vec3 fragPos;

uniform mat4 transformWorld;
uniform mat4 transformObject;
uniform mat4 cameraProjection;


void main() {
    passColor = color;
    passTexCoord = textureCoord;

    mat4 modelViewMatrix = transformWorld * transformObject;
    vec4 mvPos = modelViewMatrix * vec4(position, 1);
    gl_Position = cameraProjection * mvPos;

    fragNormal = normalize(modelViewMatrix * vec4(vertexNormal, 0.0)).xyz;
    fragPos = mvPos.xyz;
}