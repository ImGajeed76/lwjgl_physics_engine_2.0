package game_engine.maths

import org.joml.Vector3f

class Face(
    var vertex: Vector3f = Vector3f(0f),
    var texture: Vector3f = Vector3f(0f),
    var normal: Vector3f = Vector3f(0f)
) {
    fun addVertexIndices(faces: ArrayList<Face>, indices: ArrayList<Int>): ArrayList<Face> {

        for (i in 0 until indices.size/3) {
            try {
                faces[i].vertex =
                    Vector3f(indices[i * 3].toFloat(), indices[i * 3 + 1].toFloat(), indices[i * 3 + 2].toFloat())
            } catch (e: Exception) {
                val face = Face()
                face.vertex =
                    Vector3f(indices[i * 3].toFloat(), indices[i * 3 + 1].toFloat(), indices[i * 3 + 2].toFloat())

                faces.add(face)
            }
        }

        return faces
    }

    fun addTextureIndices(faces: ArrayList<Face>, indices: ArrayList<Int>): ArrayList<Face> {
        for (i in 0 until indices.size/3) {
            try {
                faces[i].texture =
                    Vector3f(indices[i * 3].toFloat(), indices[i * 3 + 1].toFloat(), indices[i * 3 + 2].toFloat())
            } catch (e: Exception) {
                val face = Face()
                face.texture =
                    Vector3f(indices[i * 3].toFloat(), indices[i * 3 + 1].toFloat(), indices[i * 3 + 2].toFloat())

                faces.add(face)
            }
        }

        return faces
    }

    fun addNormalIndices(faces: ArrayList<Face>, indices: ArrayList<Int>): ArrayList<Face> {
        for (i in 0 until indices.size/3) {
            try {
                faces[i].normal =
                    Vector3f(indices[i * 3].toFloat(), indices[i * 3 + 1].toFloat(), indices[i * 3 + 2].toFloat())
            } catch (e: Exception) {
                val face = Face()
                face.normal =
                    Vector3f(indices[i * 3].toFloat(), indices[i * 3 + 1].toFloat(), indices[i * 3 + 2].toFloat())

                faces.add(face)
            }

        }

        return faces
    }
}

fun ArrayList<Face>.addVertexIndices(indices: ArrayList<Int>) {
    for (i in 0 until indices.size/3) {
        try {
            this[i].vertex =
                Vector3f(indices[i * 3].toFloat(), indices[i * 3 + 1].toFloat(), indices[i * 3 + 2].toFloat())
        } catch (e: Exception) {
            val face = Face()
            face.vertex =
                Vector3f(indices[i * 3].toFloat(), indices[i * 3 + 1].toFloat(), indices[i * 3 + 2].toFloat())

            this.add(face)
        }
    }
}

fun ArrayList<Face>.addTextureIndices(indices: ArrayList<Int>) {
    for (i in 0 until indices.size/3) {
        try {
            this[i].texture =
                Vector3f(indices[i * 3].toFloat(), indices[i * 3 + 1].toFloat(), indices[i * 3 + 2].toFloat())
        } catch (e: Exception) {
            val face = Face()
            face.texture =
                Vector3f(indices[i * 3].toFloat(), indices[i * 3 + 1].toFloat(), indices[i * 3 + 2].toFloat())

            this.add(face)
        }
    }
}

fun ArrayList<Face>.addNormalIndices(indices: ArrayList<Int>) {
    for (i in 0 until indices.size/3) {
        try {
            this[i].normal =
                Vector3f(indices[i * 3].toFloat(), indices[i * 3 + 1].toFloat(), indices[i * 3 + 2].toFloat())
        } catch (e: Exception) {
            val face = Face()
            face.normal =
                Vector3f(indices[i * 3].toFloat(), indices[i * 3 + 1].toFloat(), indices[i * 3 + 2].toFloat())

            this.add(face)
        }
    }
}
