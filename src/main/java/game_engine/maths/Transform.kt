package game_engine.maths

import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

class Transform() {
    var position: Vector3f = Vector3f()
    var rotation: Quaternionf = Quaternionf()
    var scale: Vector3f = Vector3f(1f)

    fun getTransformation(): Matrix4f {
        val result = Matrix4f()

        result.translate(position)
        result.rotate(rotation)
        result.scale(scale)

        return result
    }
}