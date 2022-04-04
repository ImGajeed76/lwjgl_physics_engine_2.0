package game_engine.loaders

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.matthiasmann.twl.utils.PNGDecoder
import game_engine.graphics.Model
import game_engine.maths.Face
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.ARBFramebufferObject.glGenerateMipmap
import org.lwjgl.opengl.GL11.*
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.nio.ByteBuffer
import kotlin.system.measureTimeMillis

class Loader(var res: String = "src/main/resources") {
    fun loadOBJ(fileName: String): Model {
        val jsonFile = File("$res/objects/imports/$fileName.model")
        if (jsonFile.exists()) {
            println("Loading $fileName.model")
            var model: Model
            var last: String

            val duration = measureTimeMillis {
                val mapper = jacksonObjectMapper()
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

                val data: Map<String, Model> = mapper.readValue(jsonFile)

                last = data.keys.first()
                model = data.values.first()
                model.textureId = 0
            }

            val current = File("$res/objects/$fileName.obj").readText()
            if (current == last) {
                println("Loaded $fileName.model in $duration milliseconds")
                return model
            }

            println("Changes were made. Reimporting $fileName.obj")
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
            var textureId = 0

            try {
                while (true) {
                    line = reader.readLine()
                    val currentLine = deleteEmptys(line.split(" "))

                    if (line == null || line == "") {
                        continue
                    }

                    if (line.startsWith("v ")) {
                        val vertex = Vector3f(
                            currentLine[1].toFloat(), currentLine[2].toFloat(), currentLine[3].toFloat()
                        )
                        vertices.add(vertex)
                    } else if (line.startsWith("vt ")) {
                        val texture = Vector2f(
                            currentLine[1].toFloat(), currentLine[2].toFloat()
                        )
                        textures.add(texture)
                    } else if (line.startsWith("vn ")) {
                        val normal = Vector3f(
                            currentLine[1].toFloat(), currentLine[2].toFloat(), currentLine[3].toFloat()
                        )
                        normals.add(normal)
                    } else if (line.startsWith("usemtl ")) {
                        textureId = loadTexture("${currentLine[1]}.png")
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

                        val vertexIndices = Vector3f(
                            vertex1[0].toFloat() - 1, vertex2[0].toFloat() - 1, vertex3[0].toFloat() - 1
                        )

                        val textureIndices = Vector3f(
                            vertex1[1].toFloat() - 1, vertex2[1].toFloat() - 1, vertex3[1].toFloat() - 1
                        )

                        var normalIndices = Vector3f(0f)
                        if (vertex1.size == 3) {
                            normalIndices = Vector3f(
                                vertex1[2].toFloat() - 1, vertex2[2].toFloat() - 1, vertex3[2].toFloat() - 1
                            )
                        }

                        faces.add(Face(vertexIndices, textureIndices, normalIndices))
                    } else if (currentLine.size == 5) {
                        val vertex1 = currentLine[1].split("/").toTypedArray()
                        val vertex2 = currentLine[2].split("/").toTypedArray()
                        val vertex3 = currentLine[3].split("/").toTypedArray()
                        val vertex4 = currentLine[4].split("/").toTypedArray()

                        val vertexIndices = Vector4f(
                            vertex1[0].toFloat() - 1,
                            vertex2[0].toFloat() - 1,
                            vertex3[0].toFloat() - 1,
                            vertex4[0].toFloat() - 1
                        )
                        val textureIndices = Vector4f(
                            vertex1[1].toFloat() - 1,
                            vertex2[1].toFloat() - 1,
                            vertex3[1].toFloat() - 1,
                            vertex4[1].toFloat() - 1
                        )
                        var normalIndices = Vector4f(0f)

                        if (vertex1.size == 3) {
                            normalIndices = Vector4f(
                                vertex1[2].toFloat() - 1,
                                vertex2[2].toFloat() - 1,
                                vertex3[2].toFloat() - 1,
                                vertex4[2].toFloat() - 1
                            )
                        }

                        val vertexTris = quadToTri(vertexIndices)
                        val textureTris = quadToTri(textureIndices)
                        val normalTris = quadToTri(normalIndices)

                        faces.add(Face(vertexTris[0], textureTris[0], normalTris[0]))
                        faces.add(Face(vertexTris[1], textureTris[1], normalTris[1]))
                    }

                    line = reader.readLine()
                }

                reader.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            model = Model(vertices, arrayListOf(), textures, textureId, normals, faces)
            val text = File("$res/objects/$fileName.obj").readText()
            val modelAsString = jsonMapper().writeValueAsString(mapOf(text to model))

            jsonFile.createNewFile()
            jsonFile.writeText(modelAsString)
        }

        println("Imported $fileName.obj in $duration milliseconds")

        return model
    }

    private fun deleteEmptys(x: List<String>): List<String> {
        val result = arrayListOf<String>()

        for (s in x) {
            if (s != "" && s != " ") {
                result.add(s)
            }
        }

        return result.toList()
    }

    private fun quadToTri(quad: Vector4f): List<Vector3f> {
        val v1 = Vector3f(quad.x, quad.y, quad.z)
        val v2 = Vector3f(quad.x, quad.z, quad.w)

        return listOf(v1, v2)
    }

    fun loadTexture(fileName: String): Int {
        val file = File("$res/textures/$fileName")
        val decoder = PNGDecoder(file.inputStream())

        val buf = ByteBuffer.allocateDirect(4 * decoder.width * decoder.height)
        decoder.decode(buf, decoder.width * 4, PNGDecoder.Format.RGBA)
        buf.flip()

        val textureId = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, textureId)

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.width, decoder.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf)

        glGenerateMipmap(GL_TEXTURE_2D)

        return textureId
    }
}