package game_engine.graphics.objects

import game_engine.graphics.Mesh
import game_engine.graphics.Shader
import game_engine.maths.Camera
import game_engine.maths.Transform
import game_engine.maths.Vertex
import org.joml.Quaternionf
import org.joml.Vector2f
import org.joml.Vector3f
import physics_engine.Physic
import physics_engine.PhysicsObject

class Cube : GameObject {
    var size: Vector3f = Vector3f()
    var position: Vector3f = Vector3f()
    var rotation: Quaternionf = Quaternionf()

    override var mesh: Mesh = Mesh()
    override var vertexArray: ArrayList<Vertex> = arrayListOf()
    override var indices: ArrayList<Int> = arrayListOf()

    override var shader: Shader = Shader()

    override var transform: Transform = Transform()
    override var physics: ArrayList<Physic> = arrayListOf()
    override var physicsObject: PhysicsObject = PhysicsObject()

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

    override fun createMesh() {
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

    override fun createShader(vertex_shader: String, fragment_shader: String) {
        shader.create(vertex_shader, fragment_shader)
    }

    override fun setCamera(camera: Camera) {
        shader.setCamera(camera)
    }

    override fun useShader() {
        shader.useShader()
    }

    override fun draw() {
        physicsObject.position = transform.position

        for (physic in physics) {
            physicsObject = physic.update(physicsObject)
        }

        transform.position = physicsObject.position
        shader.setTransform(transform)
        mesh.draw()
    }

    override fun destroy() {
        shader.destroy()
        mesh.destroy()
    }
}