package game_engine.loaders

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import game_engine.graphics.Model
import game_engine.maths.Face
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import kotlin.system.measureTimeMillis

class Loader {
    var res = "src/main/resources"

    fun loadOBJ(fileName: String): Model {
        val jsonFile = File("$res/objects/imports/$fileName.model")
        if (jsonFile.exists()) {
            println("Loading $fileName.model")
            var model: Model

            val duration = measureTimeMillis {
                val mapper = jacksonObjectMapper()
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

                model = mapper.readValue(jsonFile)
            }

            println("Loaded $fileName.model in $duration milliseconds")
            return model
        }

        val fr: FileReader?

        try {
            fr = FileReader(File("$res/objects/$fileName.obj"))
        } catch (e: FileNotFoundException) {
            error("Error: Could not detect obj file at $res/objects/$fileName.obj")
        }

        println("Importing $fileName.obj")

        var model: Model
        val duration = measureTimeMillis {
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
                    val currentLine = deleteEmptys(line.split(" "))

                    if (line == null || line == "") {
                        continue
                    }

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

                    if (currentLine.size == 4) {
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
                    } else if (currentLine.size == 5) {
                        val vertex1 = currentLine[1].split("/").toTypedArray()
                        val vertex2 = currentLine[2].split("/").toTypedArray()
                        val vertex3 = currentLine[3].split("/").toTypedArray()
                        val vertex4 = currentLine[4].split("/").toTypedArray()

                        val vertexIndices =
                            Vector4f(
                                vertex1[0].toFloat() - 1,
                                vertex2[0].toFloat() - 1,
                                vertex3[0].toFloat() - 1,
                                vertex4[0].toFloat() - 1
                            )
                        val textureIndices =
                            Vector4f(
                                vertex1[1].toFloat() - 1,
                                vertex2[1].toFloat() - 1,
                                vertex3[1].toFloat() - 1,
                                vertex4[1].toFloat() - 1
                            )
                        //val normalIndices =
                        //    Vector4f(vertex1[2].toFloat() - 1, vertex2[2].toFloat() - 1, vertex3[2].toFloat() - 1, vertex4[2].toFloat() - 1)
                        val normalIndices = Vector4f(0f)

                        val vertexTris = quadToTri(vertexIndices)
                        val textureTris = quadToTri(textureIndices)
                        val normalTris = quadToTri(normalIndices)

                        faces.add(Face(vertexTris[0], textureTris[0], normalTris[0]))
                        faces.add(Face(vertexTris[1], textureTris[1], normalTris[1]))

                        indices.addAll(
                            arrayListOf(
                                vertexTris[0].x.toInt(),
                                vertexTris[0].y.toInt(),
                                vertexTris[0].z.toInt()
                            )
                        )
                        indices.addAll(
                            arrayListOf(
                                vertexTris[1].x.toInt(),
                                vertexTris[1].y.toInt(),
                                vertexTris[1].z.toInt()
                            )
                        )
                    }

                    line = reader.readLine()
                }

                reader.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            model = Model(vertices, textures, normals, indices)
            val modelAsString = jsonMapper().writeValueAsString(model)

            jsonFile.createNewFile()
            jsonFile.writeText(modelAsString)
        }

        println("Imported $fileName.obj in $duration milliseconds")

        return model
    }

    fun deleteEmptys(x: List<String>): List<String> {
        val result = arrayListOf<String>()

        for (s in x) {
            if (s != "" && s != " ") {
                result.add(s)
            }
        }

        return result.toList()
    }

    fun quadToTri(quad: Vector4f): List<Vector3f> {
        val v1 = Vector3f(quad.x, quad.y, quad.z)
        val v2 = Vector3f(quad.x, quad.z, quad.w)

        return listOf(v1, v2)
    }
}