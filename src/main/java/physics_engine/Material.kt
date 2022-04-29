package physics_engine

import org.joml.Vector3f
import kotlin.math.PI
import kotlin.math.pow

val D_HYDROGEN = 0.09f
val D_HELIUM = 0.18f
val D_LITHIUM = 0.53f
val D_ARGON = 0.86f
val D_NEON = 0.9f
val D_SODIUM = 0.97f
val D_AIR = 1.225f
val D_NITROGEN = 1.25f
val D_OXYGE = 1.43f
val D_CALCIUM = 1.55f
val D_RUBIDIUM = 1.63f
val D_FLUORINE = 1.7f
val D_MAGNESIUM = 1.74f
val D_POTASSIUM = 1.78f
val D_PHOSPHORUS = 1.82f
val D_BERYLLIUM = 1.85f
val D_CESIUM = 1.87f
val D_SULFUR = 2.07f
val D_CARBON = 2.26f
val D_SILICON = 2.33f
val D_BORON = 2.34f
val D_STRONTIUM = 2.54f
val D_ALUMINUM = 2.7f
val D_SCANDIUM = 3f
val D_BROMINE = 3.12f
val D_CHLORINE = 3.21f
val D_BARIUM = 3.59f
val D_KRYPTON = 3.75f
val D_YTTRIUM = 4.47f
val D_TITANIUM = 4.54f
val D_SELENIUM = 4.79f
val D_TELLURIUM = 4.93f
val D_EUROPIUM = 5.24f
val D_GERMANIUM = 5.32f
val D_RADIUM = 5.5f
val D_ARSENIC = 5.72f
val D_XENON = 5.9f
val D_GALIIUM = 5.91f
val D_VANADIUM = 6.11f
val D_LANTHANUM = 6.15f
val D_IODINE = 6.24f
val D_ZIRCONIUM = 6.51f
val D_ANTIMONY = 6.68f
val D_CERIUM = 6.77f
val D_PRASEODYMIUM = 6.77f
val D_YTTERBIUM = 6.9f
val D_TIN = 7.31f
val D_MANGANESE = 7.43f
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
