package game_engine.graphics

import game_engine.maths.Face
import game_engine.maths.Vertex
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.memFree
import java.nio.FloatBuffer
import kotlin.collections.ArrayList

class Mesh {
    var vao: Int = 0    //vertex array object
    var vbo: Int = 0
    var cbo: Int = 0    // color buffer object
    var ibo: Int = 0

    var vertexCount = 0

    var vertices: ArrayList<Vector3f> = arrayListOf()
    var indices: ArrayList<Int> = arrayListOf()

    var colors: ArrayList<Vector3f> = arrayListOf()
    var normals: ArrayList<Vector3f> = arrayListOf()
    var textures: ArrayList<Vector2f> = arrayListOf()

    fun loadVertices(vertices: ArrayList<Vertex>, faces: ArrayList<Face>) {
        for (face in faces) {
            this.indices.add(face.vertex.x.toInt())
            this.indices.add(face.vertex.y.toInt())
            this.indices.add(face.vertex.z.toInt())
        }

        for (vertex in vertices) {
            this.vertices.add(vertex.getPosition())
        }
    }

    fun loadColors(colors: ArrayList<Vertex>) {
        for (vert in colors) {
            this.colors.add(vert.getPosition())
        }
    }

    fun create(): Boolean {
        if (vertices.size == 0) {
            println("Error: Data missing for mesh creation")
            return false
        }


        val verticesBuffer: FloatBuffer = MemoryUtil.memAllocFloat(vertices.size * 3)
        val verticesArray = FloatArray(vertices.size * 3)

        for (i in 0 until vertices.size) {
            verticesArray[i * 3] = vertices[i].x
            verticesArray[i * 3 + 1] = vertices[i].y
            verticesArray[i * 3 + 2] = vertices[i].z
        }

        println("Mesh: ${verticesArray.size} vertices; ${verticesArray.size / 3} triangles -> ${verticesArray.toPrintable()}")
        verticesBuffer.put(verticesArray).flip()

        vao = glGenVertexArrays()
        glBindVertexArray(vao)

        vbo = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)

        GL20.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)

        memFree(verticesBuffer)
        vertexCount = indices.size

        // Indices
        println(indices)
        ibo = glGenBuffers()
        val indicesBuffer = MemoryUtil.memAllocInt(indices.size)
        indicesBuffer.put(indices.toIntArray()).flip()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo)
        GL15.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)
        memFree(indicesBuffer)

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
        glEnableVertexAttribArray(1)

        glBindBuffer(ibo, GL_ARRAY_BUFFER)

        GL11.glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0)

        glBindBuffer(0, GL_ARRAY_BUFFER)

        glDisableVertexAttribArray(1)
        glDisableVertexAttribArray(0)
        glBindVertexArray(0)
    }
}

private fun FloatArray.toPrintable(): String {
    val out = arrayListOf<Float>()

    for (f in this) {
        out.add(f)
    }

    return out.toString()
}
