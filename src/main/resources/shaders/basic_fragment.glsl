#version 400 core

in vec3 passColor;
in vec2 passTex;

out vec4 fragColor;

uniform sampler2D texture_sampler;

void main() {
    vec4 tex = texture(texture_sampler, passTex);
    vec4 col = vec4(passColor, 1);
    fragColor = tex;
    //fragColor = vec4(0.4666666666, 0.7176470, 0.9215686, 1);
}