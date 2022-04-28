package physics_engine

import org.joml.Vector3f
import kotlin.math.PI
import kotlin.math.pow

val D_HYDROGEN = 0.09f
val D_HELIUM = 0.18f
val D_AIR = 1.225f
val D_IRON = 7.87f

class Material {
    var simulatesAir = false
    var mass: Float = 0f
    var volume: Vector3f = Vector3f(0f)
    var dense: Float = 0f

    constructor(mass: Float, dense: Float, simAir: Boolean = false) {
        this.mass = mass
        this.dense = dense
        this.volume = Vector3f((mass / dense).pow(1f / 3f))

        simulatesAir = simAir
        if (simulatesAir) {
            this.mass -= Material(volume, D_AIR).mass
        }
    }

    constructor(volume: Vector3f, dense: Float, simAir: Boolean = false) {
        this.dense = dense
        this.volume = volume
        this.mass = dense * volume.added()

        simulatesAir = simAir
        if (simulatesAir) {
            this.mass -= Material(volume, D_AIR).mass
        }
    }

    fun getVolume(): Float {
        return volume.added()
    }

    fun getRadius(): Float {
        val vol = volume.added()

        return ((3f / 4f) * vol * PI).toFloat().pow(1f / 3f)
    }
}

private fun Vector3f.added(): Float {
    return this.x * this.y * this.z
}
