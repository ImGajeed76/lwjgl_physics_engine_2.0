#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 color;

out vec3 passColor;

uniform mat4 transformWorld;
uniform mat4 transformObject;
uniform mat4 cameraProjection;


void main() {
    passColor = vec3(0.4666666666, 0.7176470, 0.9215686);
    gl_Position = cameraProjection * transformWorld * transformObject * vec4(position, 1);
}