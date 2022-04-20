package game_engine.graphics

import game_engine.maths.Face
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.memFree
import java.nio.FloatBuffer

class Mesh {
    var vao: Int = 0    // vertex array object
    var vbo: Int = 0    // vertex buffer object
    var cbo: Int = 0    // color buffer object
    var ibo: Int = 0    // indices buffer object
    var nbo: Int = 0    // normal buffer object
    var tbo: Int = 0

    var vertexCount = 0

    var vertices: ArrayList<Vector3f> = arrayListOf()
    var indices: ArrayList<Int> = arrayListOf()

    var colors: ArrayList<Vector3f> = arrayListOf()
    var normals: ArrayList<Vector3f> = arrayListOf()

    var textures: ArrayList<Vector2f> = arrayListOf()
    var textureId: Int = 0

    var faces: ArrayList<Face> = arrayListOf()

    fun loadModel(model: Model) {
        faces = model.faces
        colors = model.colors
        normals = model.normals
        textureId = model.textureId

        loadVertices(model.vertices)
        loadTextureCoords(model.textures)
    }

    fun loadFaces(faces: ArrayList<Face>) {
        this.faces = faces
    }

    fun loadTextureCoords(texCoords: ArrayList<Vector2f>) {
        println(vertices.size)

        for (face in faces) {
            textures.add(texCoords[face.texture.x.toInt()])
            textures.add(texCoords[face.texture.y.toInt()])
            textures.add(texCoords[face.texture.z.toInt()])
        }
    }

    fun loadVertices(vertices: ArrayList<Vector3f>) {
        for (face in faces) {
            indices.add(face.vertex.x.toInt())
            indices.add(face.vertex.y.toInt())
            indices.add(face.vertex.z.toInt())
        }

        this.vertices = vertices
        colors = this.vertices
    }

    fun create(): Boolean {
        if (vertices.size == 0 || indices.size == 0) {
            println("Error: Data missing for mesh creation")
            return false
        }

        vertexCount = indices.size
        vao = glGenVertexArrays()
        glBindVertexArray(vao)

        // Vertices
        val verticesBuffer: FloatBuffer = MemoryUtil.memAllocFloat(vertices.size * 3)
        val verticesArray = VecArrayToFloatArray(vertices)

        println("Mesh: ${verticesArray.size / 3} vertices; ${indices.size / 3} triangles -> ${verticesArray.toPrintable()}")
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

        println("Colors: ${colourArray.size / 3} colors -> ${colourArray.toPrintable()}")
        colourBuffer.put(colourArray).flip()

        cbo = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, cbo)
        GL15.glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW)
        GL20.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0)
        memFree(colourBuffer)

        // Textures
        val textureBuffer = MemoryUtil.memAllocFloat(textures.size * 2)
        val textureArray = Vec2ArrayToFloatArray(textures)

        println("Texture Coords: ${textureArray.size / 2} -> ${textureArray.toPrintable()}")
        textureBuffer.put(textureArray).flip()

        tbo = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, tbo)
        GL15.glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW)
        GL20.glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0)
        memFree(textureBuffer)

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
        if (textureId != 0) {
            glActiveTexture(GL_TEXTURE0)
            glBindTexture(GL_TEXTURE_2D, textureId)
        }

        glBindVertexArray(vao)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glEnableVertexAttribArray(2)
        glEnableVertexAttribArray(3)

        GL11.glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0)

        glDisableVertexAttribArray(3)
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

    fun Vec2ArrayToFloatArray(vertices: ArrayList<Vector2f>): FloatArray {
        val verticesArray = FloatArray(vertices.size * 2)

        for (i in 0 until vertices.size) {
            verticesArray[i * 2] = vertices[i].x
            verticesArray[i * 2 + 1] = vertices[i].y
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
    } else {
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
    } else {
        for (f in this) {
            out.add(f)
        }

        out.toString()
    }
}
