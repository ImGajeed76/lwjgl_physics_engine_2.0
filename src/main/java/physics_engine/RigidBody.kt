package physics_engine

import com.bulletphysics.collision.shapes.SphereShape
import com.bulletphysics.collision.shapes.StaticPlaneShape
import com.bulletphysics.dynamics.RigidBody
import com.bulletphysics.dynamics.RigidBodyConstructionInfo
import com.bulletphysics.linearmath.DefaultMotionState
import com.bulletphysics.linearmath.MotionState
import com.bulletphysics.linearmath.Transform
import javax.vecmath.Matrix4f
import javax.vecmath.Quat4f
import javax.vecmath.Vector3f

class RigidBody {
    private val defaultShape = SphereShape(3f)
    private val defaultMotionState: MotionState =
        DefaultMotionState(Transform(Matrix4f(Quat4f(0f, 0f, 0f, 1f), Vector3f(0f, 0f, 0f), 1f)))
    private val defaultInertia = Vector3f(36f, 36f, 36f)
    private val defaultConstructionInfo: RigidBodyConstructionInfo =
        RigidBodyConstructionInfo(10f, defaultMotionState, defaultShape, defaultInertia)

    var rigidBody: RigidBody = RigidBody(defaultConstructionInfo)

    fun createSphere(
        radius: Float, mass: Float, position: Vector3f = Vector3f(0f, 0f, 0f), rotation: Quat4f = Quat4f(0f, 0f, 0f, 1f)
    ): physics_engine.RigidBody {
        val shape = SphereShape(radius)
        val motionState = DefaultMotionState(Transform(Matrix4f(rotation, position, 1f)))
        val inertia = Vector3f(0f, 0f, 0f)
        shape.calculateLocalInertia(mass, inertia)
        val constructionInfo = RigidBodyConstructionInfo(mass, motionState, shape, inertia)

        rigidBody = RigidBody(constructionInfo)

        return this
    }

    fun createPlane(
        mass: Float,
        position: Vector3f = Vector3f(0f, 0f, 0f),
        rotation: Quat4f = Quat4f(0f, 0f, 0f, 1f),
        normal: Vector3f = Vector3f(0f, 1f, 0f),
        planeConstant: Float = 0.25f
    ): physics_engine.RigidBody {
        val shape = StaticPlaneShape(normal, planeConstant)
        val motionState = DefaultMotionState(Transform(Matrix4f(rotation, position, 1f)))

        val constructionInfo = RigidBodyConstructionInfo(mass, motionState, shape)
        constructionInfo.restitution = 0.25f

        rigidBody = RigidBody(constructionInfo)

        return this
    }

    fun getPosition(): org.joml.Vector3f {
        val out = org.joml.Vector3f(0f)
        val pos = rigidBody.getWorldTransform(Transform()).origin

        out.x = pos.x
        out.y = pos.y
        out.z = pos.z

        return out
    }
}