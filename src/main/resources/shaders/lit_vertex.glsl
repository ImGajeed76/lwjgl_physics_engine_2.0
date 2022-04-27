#version 400 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoord;
layout (location = 2) in vec3 vertexNormal;

out vec2 passTexCoord;

out vec3 fragNormal;
out vec3 fragPos;

uniform mat4 transformWorld;
uniform mat4 transformObject;
uniform mat4 cameraProjection;


void main() {
    vec4 worldPos = transformObject * vec4(position, 1);
    gl_Position = cameraProjection * transformWorld * worldPos;

    fragNormal = normalize(worldPos).xyz;
    fragPos = worldPos.xyz;

    passTexCoord = textureCoord;
}