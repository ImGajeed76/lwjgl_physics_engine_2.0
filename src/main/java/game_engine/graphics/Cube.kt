package game_engine.graphics

import game_engine.maths.Camera
import game_engine.maths.Transform
import org.joml.Quaternionf
import org.joml.Vector3f

class Cube {
    var size: Vector3f = Vector3f()
    var position: Vector3f = Vector3f()
    var rotation: Quaternionf = Quaternionf()

    var mesh: Mesh = Mesh()
    var vertexArray: ArrayList<Float> = arrayListOf()

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

        // left
        addVertex(corners[1], corners[3], corners[2])
        addVertex(corners[1], corners[2], corners[0])

        // front
        addVertex(corners[0], corners[2], corners[6])
        addVertex(corners[0], corners[6], corners[4])

        // right
        addVertex(corners[4], corners[6], corners[7])
        addVertex(corners[4], corners[7], corners[5])

        // back
        addVertex(corners[5], corners[7], corners[3])
        addVertex(corners[5], corners[3], corners[1])

        // bottom
        addVertex(corners[1], corners[0], corners[4])
        addVertex(corners[1], corners[4], corners[5])

        // top
        addVertex(corners[2], corners[3], corners[7])
        addVertex(corners[2], corners[7], corners[6])

        mesh.create(vertexArray.toFloatArray())
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

    private fun addVertex(a: Vector3f, b: Vector3f, c: Vector3f) {
        vertexArray.add(a.x)
        vertexArray.add(a.y)
        vertexArray.add(a.z)

        vertexArray.add(b.x)
        vertexArray.add(b.y)
        vertexArray.add(b.z)

        vertexArray.add(c.x)
        vertexArray.add(c.y)
        vertexArray.add(c.z)
    }
}