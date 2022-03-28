package game_engine.maths

import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

class Camera {
    var position: Vector3f = Vector3f()
    var rotation: Quaternionf = Quaternionf(-1.160E-2, -6.004E-3, -3.494E-5, 9.999E-1)
    private var projection: Matrix4f = Matrix4f()
    var speed: Float = 0.009F
    var sensitivity: Float = 2f

    private var rotHorizontal = 540f - (1170 - 540)
    private var rotVertical = 540f - (1170 - 540)

    var rH = 0f
    var rV = 0f
    var rM = 0f

    var fixCam = false

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

    fun forward(s: Double) {
        position.z -= s.toFloat()
    }

    fun backwards(s: Double) {
        position.z += s.toFloat()
    }

    fun left(s: Double) {
        var r = 0f

        if (rH > 1){
            r = (1-rH)
        }
        else {
            r = (1+rH)
        }

        position.x -= s.toFloat() * r
        position.z -= s.toFloat() * rH
    }

    fun right(s: Double) {
        var r = 0f

        if (rH > 1){
            r = (1-rH)
        }
        else {
            r = (1+rH)
        }

        position.x += s.toFloat() * r
        position.z += s.toFloat() * rH
    }

    fun up(s: Double) {
        position.y += s.toFloat()
    }

    fun down(s: Double) {
        position.y -= s.toFloat()
    }

    fun turnHorizontal(s: Double) {
        updateRot()
        rotHorizontal -= s.toFloat() * sensitivity * 7
    }

    fun turnVertical(s: Double) {
        updateRot()
        rotVertical -= s.toFloat() * sensitivity * 7
    }

    private fun clamp(value: Float, max_value: Float): Float {
        return ((value % max_value) / max_value) * 2 - 1
    }

    private fun updateRot() {
        if (!fixCam) {
            // println("$rotHorizontal, $rotVertical")

            rH = clamp(rotHorizontal, 360f)
            rV = clamp(rotVertical, 360f)
            rM = (rH + 1) / 2 + 0.5f

            // println("$rH, $rV, $rM")
            val newRot = Quaternionf(rV * rM, rH, rV * (1 - rM), 1f)
            rotation.set(newRot.normalize().mul(rotation.normalize()).mul(rotation.conjugate().normalize()))
        }
    }
}