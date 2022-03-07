package physics_engine

import GAMEWINDOW
import org.joml.Math.sqrt
import org.joml.Vector3f

class Gravity(var gravity: Float = 9.7639f) : Physic() {

    override fun update(ob: PhysicsObject): PhysicsObject {
        // apply gravity
        ob.force.add(Vector3f(0f, -gravity, 0f))

        // newtons second law (apply acceleration)
        ob.acceleration = ob.force.div(ob.mass)

        // calc new position
        ob.velocity.add(ob.acceleration.mul(GAMEWINDOW.deltaTime))
        ob.position.add(ob.velocity.mul(GAMEWINDOW.deltaTime))
        ob.force = Vector3f(0f)
        return ob
    }
}