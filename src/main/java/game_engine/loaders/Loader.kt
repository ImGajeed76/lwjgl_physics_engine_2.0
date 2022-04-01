package game_engine.loaders

import game_engine.graphics.Model
import game_engine.maths.Face
import org.joml.Vector2f
import org.joml.Vector3f
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader

class Loader {
    var res = "src/main/resources"

    fun loadOBJ(fileName: String): Model {
        val fr: FileReader?

        try {
            fr = FileReader(File("$res/objects/$fileName.obj"))
        } catch (e: FileNotFoundException) {
            error("Error: Could not detect obj file at $res/objects/$fileName.obj")
        }

        val reader = BufferedReader(fr)
        var line: String?

        val vertices = arrayListOf<Vector3f>()
        val textures = arrayListOf<Vector2f>()
        val normals = arrayListOf<Vector3f>()

        val faces = arrayListOf<Face>()
        val indices = arrayListOf<Int>()

        try {
            while (true) {
                line = reader.readLine()
                val currentLine = line.split(" ")

                if (line.startsWith("v ")) {
                    val vertex = Vector3f(
                        currentLine[1].toFloat(),
                        currentLine[2].toFloat(),
                        currentLine[3].toFloat()
                    )
                    vertices.add(vertex)
                } else if (line.startsWith("vt ")) {
                    val texture = Vector2f(
                        currentLine[1].toFloat(),
                        currentLine[2].toFloat()
                    )
                    textures.add(texture)
                } else if (line.startsWith("vn ")) {
                    val normal = Vector3f(
                        currentLine[1].toFloat(),
                        currentLine[2].toFloat(),
                        currentLine[3].toFloat()
                    )
                    normals.add(normal)
                } else if (line.startsWith("f ")) {
                    break
                }
            }

            while (line != null && line.startsWith("f ")) {
                val currentLine = line.split(" ").toTypedArray()
                val vertex1 = currentLine[1].split("/").toTypedArray()
                val vertex2 = currentLine[2].split("/").toTypedArray()
                val vertex3 = currentLine[3].split("/").toTypedArray()

                val vertexIndices =
                    Vector3f(vertex1[0].toFloat() - 1, vertex2[0].toFloat() - 1, vertex3[0].toFloat() - 1)
                val textureIndices =
                    Vector3f(vertex1[1].toFloat() - 1, vertex2[1].toFloat() - 1, vertex3[1].toFloat() - 1)
                val normalIndices =
                    Vector3f(vertex1[2].toFloat() - 1, vertex2[2].toFloat() - 1, vertex3[2].toFloat() - 1)

                faces.add(Face(vertexIndices, textureIndices, normalIndices))

                indices.add(vertexIndices.x.toInt())
                indices.add(vertexIndices.y.toInt())
                indices.add(vertexIndices.z.toInt())

                line = reader.readLine()
            }

            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }


        return Model(vertices, textures, normals, indices, 0f)
    }
}