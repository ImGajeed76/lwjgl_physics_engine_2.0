#version 400 core

in vec3 passColor;

out vec4 fragColor;

void main() {
    //fragColor = vec4(passColor, 1);
    fragColor = vec4(0.4666666666, 0.7176470, 0.9215686, 1);
}