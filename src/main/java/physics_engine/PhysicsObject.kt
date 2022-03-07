package physics_engine

import org.joml.Vector3f

class PhysicsObject {
    var position: Vector3f = Vector3f()
    var velocity: Vector3f = Vector3f()
    var force: Vector3f = Vector3f()
    var acceleration: Vector3f = Vector3f()
    var mass: Float = 1f
}