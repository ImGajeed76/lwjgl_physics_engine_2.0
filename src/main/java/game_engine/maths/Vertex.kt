package game_engine.maths

import org.joml.Vector2f
import org.joml.Vector3f

class Vertex(
    private var pos: Vector3f,
    private var color: Vector3f,
    private var textureCoords: Vector2f,
    private var normal: Vector3f = Vector3f(0f)
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

    fun getTextureCoord(): Vector2f {
        return textureCoords
    }

    fun setTextureCoord(vector2f: Vector2f) {
        textureCoords = vector2f
    }

    fun getNormal(): Vector3f {
        return normal
    }

    fun setNormal(vector3f: Vector3f) {
        normal = vector3f
    }
}