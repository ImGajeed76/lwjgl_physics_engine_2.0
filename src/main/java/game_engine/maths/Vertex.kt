package game_engine.maths

import org.joml.Vector2f
import org.joml.Vector3f

class Vertex(private var pos: Vector3f, private var color: Vector3f, private var textureCoord: Vector2f) {
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
        return textureCoord
    }

    fun setTextureCoord(vector2f: Vector2f) {
        textureCoord = vector2f
    }
}