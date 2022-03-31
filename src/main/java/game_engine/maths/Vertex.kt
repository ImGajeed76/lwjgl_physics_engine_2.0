package game_engine.maths

import org.joml.Vector3f

class Vertex(
    private var pos: Vector3f,
    private var color: Vector3f,
) {
    fun getPosition(): Vector3f {
        return pos
    }

    fun setPosition(vector3f: Vector3f) {
        pos = vector3f
    }

    fun getColor(): Vector3f {
        return color
    }

    fun setColor(vector3f: Vector3f) {
        color = vector3f
    }
}