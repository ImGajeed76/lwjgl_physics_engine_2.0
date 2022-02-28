#version 400 core

in vec3 passColor;
in vec3 fragNormal;
in vec3 fragPos;

out vec4 fragColor;

struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflection;
};

struct DirectionalLight {
    vec3 colour;
    vec3 direction;
    float intensity;
};

vec4 ambientC;
vec4 diffuseC;
vec4 specularC;

float lightAngle = -90f;

Material material = Material(vec4(1), vec4(1), vec4(1), 1, 1f);
vec3 ambientLight = vec3(0.3f);

DirectionalLight directionalLight = DirectionalLight(vec3(1), vec3(0, -10, 0), 50f);
float specularPower = 10f;


void setupColours(Material material, vec2 textCoords) {
    if(material.hasTexture == 1) {
        ambientC = vec4(passColor, 1);
        diffuseC = ambientC;
        specularC = ambientC;
    } else {
        ambientC = material.ambient;
        diffuseC = material.diffuse;
        specularC = material.specular;
    }
}

vec4 calcLightColour(vec3 light_colour, float light_intensity, vec3 position, vec3 to_light_dir, vec3 normal) {
    vec4 diffuseColour = vec4(0);
    vec4 specColour = vec4(0);

    float diffuseFactor = max(dot(normal, to_light_dir), 0.0);
    diffuseColour = diffuseC * vec4(light_colour, 1.0) * light_intensity * diffuseFactor;

    vec3 camera_direction = normalize(-position);
    vec3 from_light_dir = -to_light_dir;
    vec3 reflectedLight = normalize(reflect(from_light_dir, normal));
    float specularFactor = max(dot(camera_direction, reflectedLight), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specColour = specularC * light_intensity * specularFactor * material.reflection * vec4(light_colour, 1.0);

    return (diffuseColour + specColour);
}

vec4 calcDirectionLight(DirectionalLight light, vec3 position, vec3 normal) {
    return calcLightColour(light.colour, light.intensity, position, normalize(light.direction), normal);
}

void main() {

    setupColours(material, vec2(1));

    vec4 diffuseSpecularComp = calcDirectionLight(directionalLight, fragPos, fragNormal);

    fragColor = ambientC * vec4(ambientLight, 1) * diffuseSpecularComp;
    //fragColor = vec4(passColor, 1);
    //fragColor = vec4(0.4666666666, 0.7176470, 0.9215686, 1);
}