package game_engine.graphics.objects

import game_engine.graphics.Mesh
import game_engine.graphics.Model
import game_engine.graphics.Shader
import game_engine.maths.Camera
import game_engine.maths.Transform
import game_engine.maths.Vertex
import org.joml.Vector2f
import org.joml.Vector3f
import physics_engine.Physic
import physics_engine.PhysicsObject

class CustomObject(model: Model, pos: Vector3f = Vector3f(0f)) : GameObject() {

    override var mesh: Mesh = Mesh()
    override var vertexArray: ArrayList<Vertex> = arrayListOf()
    override var indices: ArrayList<Int> = arrayListOf()

    override var shader: Shader = Shader()
    override var transform: Transform = Transform()
    override var physics: ArrayList<Physic> = arrayListOf()
    override var physicsObject: PhysicsObject = PhysicsObject()

    init {
        indices = model.indices

        for (vertex in model.vertices) {
            vertexArray.add(Vertex(vertex, vertex, Vector2f(0f)))
        }

        transform.position = pos
    }

    override fun createMesh() {
        mesh.create(vertexArray.toTypedArray(), indices.toIntArray())
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