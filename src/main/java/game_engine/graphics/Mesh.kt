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
    var vao: Int = 0    // vertex array object
    var vbo: Int = 0    // vertex buffer object
    var cbo: Int = 0    // color buffer object
    var ibo: Int = 0    // indices buffer object
    var nbo: Int = 0    // normal buffer object

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
            this.colors.add(vertex.getPosition())
        }
    }

    fun create(): Boolean {
        if (vertices.size == 0) {
            println("Error: Data missing for mesh creation")
            return false
        }

        vertexCount = indices.size
        vao = glGenVertexArrays()
        glBindVertexArray(vao)

        // Vertices
        val verticesBuffer: FloatBuffer = MemoryUtil.memAllocFloat(vertices.size * 3)
        val verticesArray = VecArrayToFloatArray(vertices)

        println("Mesh: ${verticesArray.size} vertices; ${verticesArray.size / 3} triangles -> ${verticesArray.toPrintable()}")
        verticesBuffer.put(verticesArray).flip()

        vbo = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)
        GL20.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
        memFree(verticesBuffer)

        // Indices
        println("Indices: ${indices.size} -> ${indices.toPrintable()}")
        val indicesBuffer = MemoryUtil.memAllocInt(indices.size)
        indicesBuffer.put(indices.toIntArray()).flip()

        ibo = glGenBuffers()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo)
        GL15.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)
        memFree(indicesBuffer)

        // Colors
        val colourBuffer = MemoryUtil.memAllocFloat(colors.size * 3)
        val colourArray = VecArrayToFloatArray(colors)

        println("Colors: ${colourArray.size} colours; ${colourArray.size / 3} triangles -> ${colourArray.toPrintable()}")
        colourBuffer.put(colourArray).flip()

        cbo = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, cbo)
        GL15.glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW)
        GL20.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0)
        memFree(colourBuffer)

        // Normals
        val normalBuffer = MemoryUtil.memAllocFloat(normals.size * 3)
        val normalArray = VecArrayToFloatArray(normals)
        normalBuffer.put(normalArray).flip()

        nbo = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, nbo)
        GL15.glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW)
        GL20.glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0)
        memFree(normalBuffer)

        glBindVertexArray(0)
        return true
    }

    fun destroy() {
        glBindBuffer(vbo, 0)
        GL15.glDeleteBuffers(vbo)

        glBindBuffer(ibo, 0)
        GL15.glDeleteBuffers(ibo)

        glBindVertexArray(vao)
        glDeleteVertexArrays(vao)
    }

    fun draw() {
        glBindVertexArray(vao)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glEnableVertexAttribArray(2)

        GL11.glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0)

        glDisableVertexAttribArray(2)
        glDisableVertexAttribArray(1)
        glDisableVertexAttribArray(0)
        glBindVertexArray(0)
    }

    fun VecArrayToFloatArray(vertices: ArrayList<Vector3f>): FloatArray {
        val verticesArray = FloatArray(vertices.size * 3)

        for (i in 0 until vertices.size) {
            verticesArray[i * 3] = vertices[i].x
            verticesArray[i * 3 + 1] = vertices[i].y
            verticesArray[i * 3 + 2] = vertices[i].z
        }

        return verticesArray
    }
}

private fun <E> java.util.ArrayList<E>.toPrintable(): String {
    val out = arrayListOf<E>()
    val limit = 20

    return if (this.size > limit) {
        for (i in 0 until limit) {
            out.add(this[i])
        }

        "$out..."
    }
    else {
        for (f in this) {
            out.add(f)
        }

        out.toString()
    }
}

private fun FloatArray.toPrintable(): String {
    val out = arrayListOf<Float>()
    val limit = 20

    return if (this.size > limit) {
        for (i in 0 until limit) {
            out.add(this[i])
        }

        "$out..."
    }
    else {
        for (f in this) {
            out.add(f)
        }

        out.toString()
    }
}
