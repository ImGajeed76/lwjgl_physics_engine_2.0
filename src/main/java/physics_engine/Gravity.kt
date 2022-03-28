package physics_engine

import GAMEWINDOW
import org.joml.Math.sqrt
import org.joml.Vector3f

// gravity: 9.7639f

class Gravity(var gravity: Float = 9.7639f) : Physic() {

    override fun update(ob: PhysicsObject): PhysicsObject {
        val timeStep = GAMEWINDOW.physDT.toFloat()

        // apply gravity
        ob.force.add(Vector3f(0f, -gravity, 0f))

        // newtons second law (apply acceleration)
        ob.acceleration = ob.force.div(ob.mass)

        // --------------------------- Not Eulers methode ---------------------------
        // calc new position
        // position += timestep * (velocity + timestep * acceleration / 2)
        // - ob.position.add(ob.velocity.add(ob.acceleration.mul(timeStep).div(2f)).mul(timeStep))

        // newAcceleration = force / mass
        // - val newAcceleration = ob.force.div(ob.mass)

        // velocity += timestep * (acceleration * newAcceleration) / 2
        // - ob.velocity.add(ob.acceleration.add(newAcceleration).mul(timeStep).div(2f))
        // --------------------------------------------------------------------------

        // --------------------------- Eulers methode ---------------------------

        ob.velocity.add(ob.acceleration.mul(timeStep))
        ob.position.add(ob.velocity)

        // ----------------------------------------------------------------------

        ob.force = Vector3f(0f)
        return ob
    }
}