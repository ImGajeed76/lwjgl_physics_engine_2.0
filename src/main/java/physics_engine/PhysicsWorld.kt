package physics_engine

import com.bulletphysics.collision.broadphase.DbvtBroadphase
import com.bulletphysics.collision.dispatch.CollisionDispatcher
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration
import com.bulletphysics.dynamics.DiscreteDynamicsWorld
import com.bulletphysics.dynamics.DynamicsWorld
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver
import game_engine.graphics.objects.GameObject
import game_engine.graphics.objects.OBJ
import javax.vecmath.Vector3f

class PhysicsWorld {
    var dynamicWorld: DynamicsWorld

    init {
        val brodphase = DbvtBroadphase()
        val collisionConfiguration = DefaultCollisionConfiguration()
        val dispatcher = CollisionDispatcher(collisionConfiguration)
        val solver = SequentialImpulseConstraintSolver()
        dynamicWorld = DiscreteDynamicsWorld(dispatcher, brodphase, solver, collisionConfiguration)
        dynamicWorld.setGravity(Vector3f(0f, -9.81f, 0f))
    }

    fun addRigidBody(rigidBody: RigidBody) {
        dynamicWorld.addRigidBody(rigidBody.rigidBody)
    }

    fun addRigidBody(obj: OBJ) {
        dynamicWorld.addRigidBody(obj.obj.rigidBody!!.rigidBody)
    }

    fun addRigidBody(gameObject: GameObject) {
        addRigidBody(gameObject.rigidBody!!)
    }

    fun addRigidBody(rigidBody: ArrayList<RigidBody>) {
        for (rb in rigidBody) {
            addRigidBody(rb)
        }
    }

    fun addGameObjects(rigidBody: ArrayList<GameObject>) {
        for (rb in rigidBody) {
            addRigidBody(rb.rigidBody!!)
        }
    }

    fun stepSimulation(timeStep: Float) {
        dynamicWorld.stepSimulation(timeStep)
    }

    fun updateAABBs() {
        dynamicWorld.updateAabbs()
    }
}