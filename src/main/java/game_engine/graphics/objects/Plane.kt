package game_engine.graphics.objects

import game_engine.graphics.Mesh
import game_engine.graphics.Shader
import game_engine.maths.*
import org.joml.Vector2f
import org.joml.Vector3f
import physics_engine.Physic
import physics_engine.PhysicsObject

class Plane(pos: Vector3f, radius: Float) : GameObject() {
    override var mesh: Mesh = Mesh()
    override var vertexArray: ArrayList<Vertex> = arrayListOf()
    override var faces: ArrayList<Face> = arrayListOf()

    override var shader: Shader = Shader()
    override var transform: Transform = Transform()
    override var physics: ArrayList<Physic> = arrayListOf()
    override var physicsObject: PhysicsObject = PhysicsObject()

    init {
        val p1 = Vector3f(pos.x - radius, pos.y, pos.z - radius)
        val p2 = Vector3f(pos.x - radius, pos.y, pos.z + radius)
        val p3 = Vector3f(pos.x + radius, pos.y, pos.z + radius)
        val p4 = Vector3f(pos.x + radius, pos.y, pos.z - radius)

        vertexArray.add(Vertex(p1, p1))
        vertexArray.add(Vertex(p2, p2))
        vertexArray.add(Vertex(p3, p3))
        vertexArray.add(Vertex(p4, p4))

        faces.addVertexIndices(arrayListOf(1, 2, 0, 1, 3, 2))
    }

    override fun createMesh() {
        mesh.loadVertices(vertexArray, faces)
        mesh.create()
    }

    override fun createShader(vertex_shader: String, fragment_shader: String) {
        shader.create(vertex_shader, fragment_shader)
    }

    override fun setCamera(camera: Camera) {
        shader.setCamera(camera)
    }

    override fun useShader() {
        shader.bind()
    }

    override fun updatePhysics(others: ArrayList<GameObject>) {
        physicsObject.position = transform.position

        for (physic in physics) {
            physicsObject = physic.update(physicsObject)
        }

        transform.position = physicsObject.position
    }

    override fun draw() {
        shader.setTransform(transform)
        mesh.draw()
        shader.unbind()
    }

    override fun destroy() {
        shader.destroy()
        mesh.destroy()
    }
}
