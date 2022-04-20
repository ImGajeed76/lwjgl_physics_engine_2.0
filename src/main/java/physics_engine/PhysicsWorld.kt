package physics_engine

import com.bulletphysics.collision.broadphase.DbvtBroadphase
import com.bulletphysics.collision.dispatch.CollisionDispatcher
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration
import com.bulletphysics.dynamics.DiscreteDynamicsWorld
import com.bulletphysics.dynamics.DynamicsWorld
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver
import javax.vecmath.Vector3f

class PhysicsWorld {
    var dynamicWorld: DynamicsWorld

    init {
        val brodphase = DbvtBroadphase()
        val collisionConfiguration = DefaultCollisionConfiguration()
        val dispatcher = CollisionDispatcher(collisionConfiguration)
        val solver = SequentialImpulseConstraintSolver()
        dynamicWorld = DiscreteDynamicsWorld(dispatcher, brodphase, solver, collisionConfiguration)
        dynamicWorld.setGravity(Vector3f(0f, -3f, 0f))
    }

    fun addRigidBody(rigidBody: RigidBody) {
        dynamicWorld.addRigidBody(rigidBody.rigidBody)
    }

    fun stepSimulation(timeStep: Float) {
        dynamicWorld.stepSimulation(timeStep)
    }
}