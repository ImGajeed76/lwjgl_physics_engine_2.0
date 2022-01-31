#version 330

in vec3 passColor;

out vec4 fragColor;

void main() {
    fragColor = vec4(passColor, 1);
}