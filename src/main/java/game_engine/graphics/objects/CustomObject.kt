package game_engine.graphics.objects

import game_engine.graphics.Mesh
import game_engine.graphics.Model
import game_engine.graphics.Shader
import game_engine.maths.*
import org.joml.Vector2f
import org.joml.Vector3f
import physics_engine.Physic
import physics_engine.PhysicsObject

class CustomObject(model: Model, pos: Vector3f = Vector3f(0f)) : GameObject() {

    override var mesh: Mesh = Mesh()
    override var vertexArray: ArrayList<Vertex> = arrayListOf()
    override var faces: ArrayList<Face> = arrayListOf()

    override var shader: Shader = Shader()
    override var transform: Transform = Transform()
    override var physics: ArrayList<Physic> = arrayListOf()
    override var physicsObject: PhysicsObject = PhysicsObject()

    init {
        faces.addVertexIndices(model.indices)

        for (vertex in model.vertices) {
            vertexArray.add(Vertex(vertex, vertex))
        }

        transform.position = pos
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
    }

    override fun destroy() {
        shader.destroy()
        mesh.destroy()
    }
}