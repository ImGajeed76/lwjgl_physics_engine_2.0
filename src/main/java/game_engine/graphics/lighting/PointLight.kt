package game_engine.graphics.lighting

import org.joml.Vector3f

class PointLight {
    var colour: Vector3f = Vector3f(1f)
    var position: Vector3f = Vector3f(0f)
    var intensity: Float = 0f
    var constant: Float = 1f
    var linear: Float = 0f
    var exponent: Float = 0f

    var name: String = ""

    constructor(name: String, colour: Vector3f, position: Vector3f, intensity: Float, constant: Float, linear: Float, exponent: Float) {
        this.name = name

        this.colour = colour
        this.position = position
        this.intensity = intensity
        this.constant = constant
        this.linear = linear
        this.exponent = exponent
    }

    constructor(name: String, colour: Vector3f, position: Vector3f, intensity: Float) {
        this.name = name

        this.colour = colour
        this.position = position
        this.intensity = intensity
    }
}