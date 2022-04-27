package game_engine.graphics.lighting

import org.joml.Vector4f

class Material {
    var ambientColour = Vector4f(1f)
    var diffuseColour = Vector4f(1f)
    var specularColour = Vector4f(1f)
    var reflectance = 0f

    constructor(ambientColour: Vector4f, diffuseColour: Vector4f, specularColour: Vector4f, reflectance: Float) {
        this.ambientColour = ambientColour
        this.diffuseColour = diffuseColour
        this.specularColour = specularColour
        this.reflectance = reflectance
    }

    constructor(colour: Vector4f, reflectance: Float) {
        ambientColour = colour
        diffuseColour = colour
        specularColour = colour
        this.reflectance = reflectance
    }

    constructor() {
        ambientColour = Vector4f(1f)
        diffuseColour = Vector4f(1f)
        specularColour = Vector4f(1f)
        reflectance = 0f
    }
}