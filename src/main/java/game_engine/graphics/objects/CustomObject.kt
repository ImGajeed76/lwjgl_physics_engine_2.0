package game_engine.graphics.objects

import game_engine.graphics.Mesh
import game_engine.graphics.Model
import game_engine.graphics.Shader
import game_engine.maths.*
import org.joml.Vector2f
import org.joml.Vector3f
import physics_engine.Physic
import physics_engine.PhysicsObject
import physics_engine.RigidBody

class CustomObject(override var model: Model, pos: Vector3f = Vector3f(0f)) : GameObject() {

    override var mesh: Mesh = Mesh()

    override var shader: Shader = Shader()
    override var transform: Transform = Transform()
    override var rigidBody: RigidBody = RigidBody()

    init {
        transform.position = pos
    }

    override fun createMesh() {
        mesh.loadModel(model)
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
        transform.position = rigidBody.getPosition()
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