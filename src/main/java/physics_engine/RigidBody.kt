package physics_engine

import com.bulletphysics.collision.shapes.BoxShape
import com.bulletphysics.collision.shapes.CollisionShape
import com.bulletphysics.collision.shapes.SphereShape
import com.bulletphysics.collision.shapes.StaticPlaneShape
import com.bulletphysics.dynamics.RigidBody
import com.bulletphysics.dynamics.RigidBodyConstructionInfo
import com.bulletphysics.linearmath.DefaultMotionState
import com.bulletphysics.linearmath.MotionState
import com.bulletphysics.linearmath.Transform
import org.joml.Quaternionf
import javax.vecmath.Matrix4f
import javax.vecmath.Quat4f
import javax.vecmath.Vector3f


val RB_PLANE = 0
val RB_CUBE = 1
val RB_SPHERE = 2

class RigidBody {
    private val defaultShape = SphereShape(3f)
    private val defaultMotionState: MotionState =
        DefaultMotionState(Transform(Matrix4f(Quat4f(0f, 0f, 0f, 1f), Vector3f(0f, 0f, 0f), 1f)))
    private val defaultInertia = Vector3f(36f, 36f, 36f)
    private val defaultConstructionInfo: RigidBodyConstructionInfo =
        RigidBodyConstructionInfo(10f, defaultMotionState, defaultShape, defaultInertia)

    var shape: CollisionShape = defaultShape
    var motionState: MotionState = defaultMotionState
    var constructionInfo: RigidBodyConstructionInfo = defaultConstructionInfo
    var rigidBody: RigidBody = RigidBody(constructionInfo)

    fun createSphere(
        radius: Float, mass: Float, position: Vector3f = Vector3f(0f, 0f, 0f), rotation: Quat4f = Quat4f(0f, 0f, 0f, 1f)
    ): physics_engine.RigidBody {
        shape = SphereShape(1f)
        shape.setLocalScaling(Vector3f(radius, radius, radius))
        motionState = DefaultMotionState(Transform(Matrix4f(rotation, position, 1f)))

        val inertia = Vector3f(0f, 0f, 0f)
        shape.calculateLocalInertia(mass, inertia)

        constructionInfo = RigidBodyConstructionInfo(mass, motionState, shape, inertia)
        constructionInfo.restitution = 1.5f

        rigidBody = RigidBody(constructionInfo)

        return this
    }

    fun createSphere(
        mat: Material, position: Vector3f = Vector3f(0f, 0f, 0f), rotation: Quat4f = Quat4f(0f, 0f, 0f, 1f)
    ): physics_engine.RigidBody {
        val radius = mat.getRadius()
        val mass = mat.mass

        val rb = createSphere(radius, mass, position, rotation)

        return rb
    }

    fun createPlane(
        mass: Float,
        position: Vector3f = Vector3f(0f, 0f, 0f),
        rotation: Quat4f = Quat4f(0f, 0f, 0f, 1f),
        normal: Vector3f = Vector3f(0f, 1f, 0f),
        planeConstant: Float = 0f,
        restitution: Float = 0.25f,
        scale: Vector3f = Vector3f(100f, 100f, 100f),
        friction: Float = 0.9f
    ): physics_engine.RigidBody {
        shape = StaticPlaneShape(normal, planeConstant)
        shape.setLocalScaling(scale)
        motionState = DefaultMotionState(Transform(Matrix4f(rotation, position, 1f)))

        constructionInfo = RigidBodyConstructionInfo(mass, motionState, shape)
        constructionInfo.restitution = restitution
        constructionInfo.friction = friction

        rigidBody = RigidBody(constructionInfo)

        return this
    }

    fun createCube(
        mass: Float,
        position: Vector3f = Vector3f(0f, 0f, 0f),
        rotation: Quat4f = Quat4f(0f, 0f, 0f, 1f),
        scale: Vector3f = Vector3f(1f, 1f, 1f),
    ): physics_engine.RigidBody {
        shape = BoxShape(Vector3f(1f, 1f, 1f))
        shape.setLocalScaling(scale)
        motionState = DefaultMotionState(Transform(Matrix4f(rotation, position, 1f)))

        val inertia = Vector3f()
        shape.calculateLocalInertia(mass, inertia)

        constructionInfo = RigidBodyConstructionInfo(mass, motionState, shape, inertia)

        rigidBody = RigidBody(constructionInfo)

        return this
    }

    fun createCube(
        mat: Material,
        position: Vector3f = Vector3f(0f, 0f, 0f),
        rotation: Quat4f = Quat4f(0f, 0f, 0f, 1f),
    ): physics_engine.RigidBody {
        val mass = mat.mass
        val scale = mat.volume.toVecMath()

        val rb = createCube(mass, position, rotation, scale)

        return rb
    }

    fun create(
        type: Int,
        mat: Material,
        position: org.joml.Vector3f = org.joml.Vector3f(0f),
        rotation: Quat4f = Quat4f(0f, 0f, 0f, 1f)
    ): physics_engine.RigidBody {
        var rb = RigidBody()

        if (type == RB_CUBE) {
            rb = createCube(mat, position.toVecMath(), rotation)
        }

        else if (type == RB_SPHERE) {
            rb = createSphere(mat, position.toVecMath(), rotation)
        }

        return rb
    }

    fun getPosition(): org.joml.Vector3f {
        val pos = rigidBody.getWorldTransform(Transform()).origin
        return vec3fToVec3f(pos)
    }

    fun getRotation(): Quaternionf {
        val out = Quaternionf()
        val rot = Quat4f()
        rigidBody.getWorldTransform(Transform()).getRotation(rot)

        out.x = rot.x
        out.y = rot.y
        out.z = rot.z
        out.w = rot.w

        return out
    }

    fun getScale(): org.joml.Vector3f {
        val scale = Vector3f()
        shape.getLocalScaling(scale)

        return vec3fToVec3f(scale)
    }

    private fun vec3fToVec3f(v1: Vector3f): org.joml.Vector3f {
        return org.joml.Vector3f(v1.x, v1.y, v1.z)
    }
}

private fun org.joml.Vector3f.toVecMath(): javax.vecmath.Vector3f {
    return javax.vecmath.Vector3f(this.x, this.y, this.z)
}

private fun javax.vecmath.Vector3f.toJoml(): org.joml.Vector3f {
    return org.joml.Vector3f(this.x, this.y, this.z)
}

private fun org.joml.Vector3f.copy(): org.joml.Vector3f {
    return org.joml.Vector3f(this.x, this.y, this.z)
}