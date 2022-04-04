package game_engine.graphics

import game_engine.maths.Face
import org.joml.Vector2f
import org.joml.Vector3f

class Model(
    var vertices: ArrayList<Vector3f> = arrayListOf(),
    var colors: ArrayList<Vector3f> = arrayListOf(),
    var textures: ArrayList<Vector2f> = arrayListOf(),
    var textureId: Int = 0,
    var normals: ArrayList<Vector3f> = arrayListOf(),
    var faces: ArrayList<Face> = arrayListOf(),
)