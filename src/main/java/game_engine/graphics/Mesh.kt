package game_engine.graphics

import game_engine.maths.Vertex
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer

class Mesh {
    var pbo: Int = 0    //position buffer
    var ibo: Int = 0    //indices buffer
    var vao: Int = 0    //vertex array object
    var cbo: Int = 0    //color buffer
    var tbo: Int = 0    //texture buffer

    var vertexCount = 0

    var vertices: ArrayList<Vertex> = arrayListOf()
    var indices: ArrayList<Int> = arrayListOf()

    fun create(vertices: Array<Vertex>? = null, indices: IntArray? = null): Boolean {
        var verts: Array<Vertex>? = vertices
        var inds: IntArray? = indices

        if (verts == null) {
            verts = this.vertices.toTypedArray()
        }

        if (inds == null) {
            inds = this.indices.toIntArray()
        }

        vao = glGenVertexArrays()
        glBindVertexArray(vao)

        val positionBuffer = MemoryUtil.memAllocFloat(verts.size * 3)
        val positionData = FloatArray(verts.size * 3)
        for (i in verts.indices) {
            positionData[i * 3] = verts[i].getPosition().x
            positionData[i * 3 + 1] = verts[i].getPosition().y
            positionData[i * 3 + 2] = verts[i].getPosition().z
        }
        positionBuffer.put(positionData).flip()

        pbo = storeData(positionBuffer, 0, 3)

        val colorBuffer = MemoryUtil.memAllocFloat(verts.size * 3)
        val colorData = FloatArray(verts.size * 3)
        for (i in verts.indices) {
            colorData[i * 3] = verts[i].getColor().x
            colorData[i * 3 + 1] = verts[i].getColor().y
            colorData[i * 3 + 2] = verts[i].getColor().z
        }
        colorBuffer.put(colorData).flip()

        cbo = storeData(colorBuffer, 1, 3)

        val textureBuffer = MemoryUtil.memAllocFloat(verts.size * 2)
        val textureData = FloatArray(verts.size * 2)
        for (i in verts.indices) {
            textureData[i * 2] = verts[i].getTextureCoord().x
            textureData[i * 2 + 1] = verts[i].getTextureCoord().y
        }
        textureBuffer.put(textureData).flip()

        tbo = storeData(textureBuffer, 2, 2)

        val indicesBuffer = MemoryUtil.memAllocInt(inds.size)
        indicesBuffer.put(inds).flip()

        ibo = glGenBuffers()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)

        vertexCount = inds.size

        return true
    }

    private fun storeData(buffer: FloatBuffer, index: Int, size: Int): Int {
        val bufferID = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, bufferID)                                                 // Bind buffer
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)                                   // Write data
        GL20.glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0)    // Save data
        glBindBuffer(GL_ARRAY_BUFFER, 0)                                                  // Unbind buffer
        return bufferID
    }

    fun destroy() {
        GL15.glDeleteBuffers(pbo)
        GL15.glDeleteBuffers(cbo)
        GL15.glDeleteBuffers(ibo)
        GL15.glDeleteBuffers(tbo)
        glDeleteVertexArrays(vao)
    }

    fun draw() {
        glBindVertexArray(vao)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glEnableVertexAttribArray(2)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo)

        glDrawArrays(GL_TRIANGLES, 0, vertexCount)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)

        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glDisableVertexAttribArray(2)
        glBindVertexArray(0)
    }
}