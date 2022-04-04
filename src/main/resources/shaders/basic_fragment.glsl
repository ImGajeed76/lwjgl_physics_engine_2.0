#version 400 core

in vec3 passColor;
in vec2 passTex;

out vec4 fragColor;

uniform sampler2D texture_sampler;

void main() {
    fragColor = texture(texture_sampler, passTex);
    //fragColor = vec4(passColor, 1);
    //fragColor = vec4(0.4666666666, 0.7176470, 0.9215686, 1);
}