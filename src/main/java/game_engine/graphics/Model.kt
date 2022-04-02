package game_engine.graphics

import org.joml.Vector2f
import org.joml.Vector3f

class Model(
    var vertices: ArrayList<Vector3f>,
    var textures: ArrayList<Vector2f>,
    var normals: ArrayList<Vector3f>,
    var indices: ArrayList<Int>
)