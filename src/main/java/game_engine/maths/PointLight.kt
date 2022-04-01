package game_engine.maths

import org.joml.Vector3f

class PointLight {
    class Attenuation {
        var constant = 0f
        var linear = 0f
        var exponent = 0f
    }

    var colour: Vector3f = Vector3f(1f)
    var position: Vector3f = Vector3f(0f)
    var intensity: Float = 1f
    var attenuation: Attenuation = Attenuation()
}