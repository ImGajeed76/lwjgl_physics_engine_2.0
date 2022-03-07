package physics_engine

import game_engine.maths.Transform
import org.joml.Vector3f

abstract class Physic {
    abstract fun update(ob: PhysicsObject) : PhysicsObject
}