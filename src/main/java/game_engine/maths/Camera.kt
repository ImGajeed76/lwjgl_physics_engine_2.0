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

    fun moveRotation(sH: Double, sV: Double) {
        offsetRot.y = (sH.toFloat() * sensitivity * 7f)
        offsetRot.x = (sV.toFloat() * sensitivity * 7f)
        rotation.add(offsetRot)

        rotation.x = (rotation.x + 360) % 360
        rotation.y = (rotation.y + 360) % 360

        rotationRad = toRadians(rotation)

        if (rotation.x > 90 && rotation.x < 181) {
            rotation.x = 90f
        }

        if (rotation.x < 270 && rotation.x > 180) {
            rotation.x = 270f
        }
    }

    fun getForward(): Vector3f {
        val result = Vector3f(0f)

        result.x = sin(rotationRad.y)
        result.z = -cos(rotationRad.y)
        result.y = -sin(rotationRad.x)

        return result
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