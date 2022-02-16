package game_engine.maths

import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

class Camera {
    var position: Vector3f = Vector3f()
    var rotation: Quaternionf = Quaternionf()
    private var projection: Matrix4f = Matrix4f()
    var speed: Float = 0.2F
    var sensitivity: Float = 1f

    private var rotHorizontal = 0f
    private var rotVertical = 0f

    var rH = 0f
    var rV = 0f
    var rM = 0f

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

    fun forward(s: Float) {
        position.z -= s
    }

    fun backwards(s: Float) {
        position.z += s
    }

    fun left(s: Float) {
        position.x -= s * (rM * 2)
        position.z -= s * ((1 - rM) * 2)
    }

    fun right(s: Float) {
        position.x += s * (rM * 2)
        position.z += s * ((1 - rM) * 2)
    }

    fun up(s: Float) {
        position.y += s
    }

    fun down(s: Float) {
        position.y -= s
    }

    fun turnHorizontal(s: Float) {
        rotHorizontal -= s * sensitivity * 7
        updateRot()
    }

    fun turnVertical(s: Float) {
        rotVertical -= s * sensitivity * 7
        updateRot()
    }

    private fun clamp(value: Float, max_value: Float): Float {
        return ((value % max_value) / max_value) * 2 - 1
    }

    private fun updateRot() {
        rH = clamp(rotHorizontal, 360f)
        rV = clamp(rotVertical, 360f)
        rM = (rH + 1) / 2 + 0.5f

        println("$rH, $rV, $rM")
        val newRot = Quaternionf(rV * rM, rH, rV * (1 - rM), 1f)
        rotation.set(newRot.normalize().mul(rotation.normalize()).mul(rotation.conjugate().normalize()))
    }
}