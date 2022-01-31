package game_engine.maths

import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

class Camera {
    var position: Vector3f = Vector3f()
    var rotation: Quaternionf = Quaternionf()
    private var projection: Matrix4f = Matrix4f()
    var speed: Float = 0.0002F

    fun setOrthographic(left: Float, right: Float, top: Float, bottom: Float) {
        projection.setOrtho2D(left, right, bottom, top)
    }

    fun setPerspective(fov: Float, aspectRatio: Float, zNear: Float, zFar: Float) {
        projection.setPerspective(fov, aspectRatio, zNear, zFar)
    }

    fun getTransformation(): Matrix4f {
        val result = Matrix4f()

        result.rotate(rotation.conjugate(Quaternionf()))
        result.translate(position.mul(-1f, Vector3f()))

        return result
    }

    fun getProjection(): Matrix4f {
        return projection
    }
}