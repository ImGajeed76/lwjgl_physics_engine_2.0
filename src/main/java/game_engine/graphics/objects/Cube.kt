package game_engine.graphics.objects

import game_engine.graphics.Mesh
import game_engine.graphics.Shader
import game_engine.maths.Camera
import game_engine.maths.Transform
import game_engine.maths.Vertex
import org.joml.Quaternionf
import org.joml.Vector2f
import org.joml.Vector3f

class Cube {
    var size: Vector3f = Vector3f()
    var position: Vector3f = Vector3f()
    var rotation: Quaternionf = Quaternionf()

    var mesh: Mesh = Mesh()
    var vertexArray: ArrayList<Vertex> = arrayListOf()
    var indices: ArrayList<Int> = arrayListOf()

    var shader: Shader = Shader()


    constructor(size: Vector3f) {
        this.size = size
        this.position = Vector3f(0f)
    }

    constructor(size: Vector3f, position: Vector3f) {
        this.size = size
        this.position = position
    }

    constructor(size: Vector3f, position: Vector3f, rotation: Quaternionf) {
        this.size = size
        this.position = position
        this.rotation = rotation
    }

    fun createMesh() {
        val halfWidth = size.x / 2
        val halfHeight = size.y / 2
        val halfDepth = size.z / 2

        val corners: ArrayList<Vector3f> = arrayListOf(
            Vector3f(
                position.x - halfWidth,
                position.y - halfHeight,
                position.z - halfDepth
            ),
            Vector3f(
                position.x + halfWidth,
                position.y - halfHeight,
                position.z - halfDepth
            ),
            Vector3f(
                position.x - halfWidth,
                position.y + halfHeight,
                position.z - halfDepth
            ),
            Vector3f(
                position.x + halfWidth,
                position.y + halfHeight,
                position.z - halfDepth
            ),
            Vector3f(
                position.x - halfWidth,
                position.y - halfHeight,
                position.z + halfDepth
            ),
            Vector3f(
                position.x + halfWidth,
                position.y - halfHeight,
                position.z + halfDepth
            ),
            Vector3f(
                position.x - halfWidth,
                position.y + halfHeight,
                position.z + halfDepth
            ),
            Vector3f(
                position.x + halfWidth,
                position.y + halfHeight,
                position.z + halfDepth
            )
        )

        vertexArray = arrayListOf()

        for (pos in corners) {
            vertexArray.add(Vertex(pos, Vector3f(0.46666667f, 0.7176470f, 0.9215686f), Vector2f(0f, 0f)))
        }

        // left
        indices.addAll(arrayListOf(1, 3, 2))
        indices.addAll(arrayListOf(1, 2, 0))

        // front
        indices.addAll(arrayListOf(0, 2, 6))
        indices.addAll(arrayListOf(0, 6, 4))

        // right
        indices.addAll(arrayListOf(4, 6, 7))
        indices.addAll(arrayListOf(4, 7, 5))

        // back
        indices.addAll(arrayListOf(5, 7, 3))
        indices.addAll(arrayListOf(5, 3, 1))

        // bottom
        indices.addAll(arrayListOf(1, 0, 4))
        indices.addAll(arrayListOf(1, 4, 5))

        // top
        indices.addAll(arrayListOf(2, 3, 7))
        indices.addAll(arrayListOf(2, 7, 6))

        //color test
        vertexArray[0].setColor(Vector3f(0f, 0f, 0f))
        vertexArray[1].setColor(Vector3f(1f, 0f, 0f))
        vertexArray[2].setColor(Vector3f(0f, 1f, 0f))
        vertexArray[3].setColor(Vector3f(1f, 1f, 0f))
        vertexArray[4].setColor(Vector3f(0f, 0f, 1f))
        vertexArray[5].setColor(Vector3f(1f, 0f, 1f))
        vertexArray[6].setColor(Vector3f(0f, 1f, 1f))
        vertexArray[7].setColor(Vector3f(1f, 1f, 1f))

        mesh.create(vertexArray.toTypedArray(), indices.toIntArray())
    }

    fun createShader(vertex_shader: String, fragment_shader: String) {
        shader.create(vertex_shader, fragment_shader)
    }

    fun setCamera(camera: Camera) {
        shader.setCamera(camera)
    }

    fun setTransform(transform: Transform) {
        shader.setTransform(transform)
    }

    fun useShader() {
        shader.useShader()
    }

    fun draw() {
        mesh.draw()
    }

    fun destroy() {
        shader.destroy()
        mesh.destroy()
    }
}