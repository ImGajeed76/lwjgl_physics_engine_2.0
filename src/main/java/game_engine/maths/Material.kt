package game_engine.maths

import org.joml.Vector3f
import org.joml.Vector4f

class Material {
    var ambient: Vector4f = Vector4f(0f)
    var diffuse: Vector4f = Vector4f(0f)
    var specular: Vector4f = Vector4f(0f)
    var hasTexture = false
    var reflectance = 0f
}