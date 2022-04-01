package game_engine.maths

import org.joml.Matrix4f
import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.sin

class Camera {
    var position: Vector3f = Vector3f()
    var rotation: Vector3f = Vector3f()
    var rotationRad: Vector3f = Vector3f()

    private var projection: Matrix4f = Matrix4f()
    var speed: Float = 0.009F
    var sensitivity: Float = 2f

    private var rotHorizontal = 540f - (1170 - 540)
    private var rotVertical = 540f - (1170 - 540)

    var rH = 0f
    var rV = 0f
    var rM = 0f

    var fixCam = false

    private var offsetPos = Vector3f(0f)
    private var offsetRot = Vector3f(0f)

    fun setOrthographic(left: Float, right: Float, top: Float, bottom: Float) {
        projection.setOrtho2D(left, right, bottom, top)
    }

    fun setPerspective(fov: Float, aspectRatio: Float, zNear: Float, zFar: Float) {
        projection.setPerspective(fov, aspectRatio, zNear, zFar)
    }

    fun getTransformation(): Matrix4f {
        val result = Matrix4f()

        result.rotate(Math.toRadians(rotation.x.toDouble()).toFloat(), Vector3f(1f, 0f, 0f))
        result.rotate(Math.toRadians(rotation.y.toDouble()).toFloat(), Vector3f(0f, 1f, 0f))

        result.translate(position.mul(-1f, Vector3f()))

        return result
    }

    fun getProjection(): Matrix4f {
        return projection
    }

    fun forward(s: Double) {
        position.x += sin(rotationRad.y) * s.toFloat()
        position.z += -cos(rotationRad.y) * s.toFloat()
        position.y += -sin(rotationRad.x) * s.toFloat()
    }

    fun backwards(s: Double) {
        position.x -= sin(rotationRad.y) * s.toFloat()
        position.z -= -cos(rotationRad.y) * s.toFloat()
        position.y -= -sin(rotationRad.x) * s.toFloat()
    }

    fun left(s: Double) {
        position.z -= sin(rotationRad.y) * s.toFloat()
        position.x -= cos(rotationRad.y) * s.toFloat()
    }

    fun right(s: Double) {
        position.z += sin(rotationRad.y) * s.toFloat()
        position.x += cos(rotationRad.y) * s.toFloat()
    }

    fun up(s: Double) {
        position.y += s.toFloat()
    }

    fun down(s: Double) {
        position.y += -s.toFloat()
    }

    fun movePosition() {
        if (offsetPos.z != 0f) {
            position.x += sin(Math.toRadians(rotation.y.toDouble())).toFloat() * -1.0f * offsetPos.z
            position.z += cos(Math.toRadians(rotation.y.toDouble())).toFloat() * offsetPos.z
        }

        if (offsetPos.x != 0f) {
            position.x += sin(Math.toRadians((rotation.y - 90).toDouble())).toFloat() * -1.0f * offsetPos.x
            position.z += cos(Math.toRadians((rotation.y - 90).toDouble())).toFloat() * offsetPos.x
        }

        position.y += offsetPos.y
    }

    fun moveRotation(sH: Double, sV: Double) {
        offsetRot.y = (sH.toFloat() * sensitivity * 7)
        offsetRot.x = (sV.toFloat() * sensitivity * 7)
        rotation.add(offsetRot)

        val rot = (rotation.x % 360) * -1

        if (rot < 280 && rot > 181) {
            rotation.x = 280f * -1
        }

        if (rot > 80 && rot < 180) {
            rotation.x = 80f * -1
        }

        rotationRad = toRadians(rotation)
    }

    fun toRadians(deg: Vector3f): Vector3f {
        val result = Vector3f(0f)

        result.x = Math.toRadians(deg.x.toDouble()).toFloat()
        result.y = Math.toRadians(deg.y.toDouble()).toFloat()
        result.z = Math.toRadians(deg.z.toDouble()).toFloat()

        return result
    }

    fun toRadians(deg: Float): Float {
        return Math.toRadians(deg.toDouble()).toFloat()
    }
}