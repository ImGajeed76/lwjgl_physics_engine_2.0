package game_engine.graphics

import game_engine.maths.Face
import game_engine.maths.Vertex
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.memFree
import java.nio.FloatBuffer
import java.util.*
import kotlin.collections.ArrayList

class Mesh {
    var vao: Int = 0    //vertex array object
    var vbo: Int = 0

    var vertexCount = 0

    var vertices: ArrayList<Vector3f> = arrayListOf()

    var colors: ArrayList<Vector3f> = arrayListOf()
    var normals: ArrayList<Vector3f> = arrayListOf()
    var textures: ArrayList<Vector2f> = arrayListOf()

    fun loadVertices(vertices: ArrayList<Vertex>, faces: ArrayList<Face>) {
        for (i in 0 until faces.size) {
            this.vertices.add(vertices[faces[i].vertex.x.toInt()].getPosition())
            this.vertices.add(vertices[faces[i].vertex.y.toInt()].getPosition())
            this.vertices.add(vertices[faces[i].vertex.z.toInt()].getPosition())
        }
    }

    fun create(): Boolean {
        if (vertices.size == 0) {
            println("Error: Data missing for mesh creation")
            return false
        }


        val verticesBuffer: FloatBuffer = MemoryUtil.memAllocFloat(vertices.size * 3)
        val verticesArray: FloatArray = FloatArray(vertices.size * 3)

        for (i in 0 until vertices.size) {
            verticesArray[i * 3] = vertices[i].x
            verticesArray[i * 3 + 1] = vertices[i].y
            verticesArray[i * 3 + 2] = vertices[i].z
        }

        verticesBuffer.put(verticesArray).flip()

        vao = glGenVertexArrays()
        glBindVertexArray(vao)

        vbo = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)
        memFree(verticesBuffer)

        GL20.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)

        memFree(verticesBuffer)

        return true
    }

    fun destroy() {
        glBindBuffer(vbo, 0)
        GL15.glDeleteBuffers(vbo)

        glBindVertexArray(vao)
        glDeleteVertexArrays(vao)
    }

    fun draw() {
        glBindVertexArray(vao)
        glEnableVertexAttribArray(0)

        glDrawArrays(GL_TRIANGLES, 0, 3)

        glDisableVertexAttribArray(0)
        glBindVertexArray(0)
    }
}