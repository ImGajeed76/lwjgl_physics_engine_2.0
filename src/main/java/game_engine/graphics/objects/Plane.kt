package game_engine.graphics.objects

import game_engine.graphics.Mesh
import game_engine.graphics.Model
import game_engine.graphics.Shader
import game_engine.maths.*
import org.joml.Vector2f
import org.joml.Vector3f
import physics_engine.Physic
import physics_engine.PhysicsObject

class Plane(pos: Vector3f, radius: Float) : GameObject() {
    override var mesh: Mesh = Mesh()
    override var model: Model = Model()

    override var shader: Shader = Shader()
    override var transform: Transform = Transform()
    override var physics: ArrayList<Physic> = arrayListOf()
    override var physicsObject: PhysicsObject = PhysicsObject()

    init {
        val p1 = Vector3f(pos.x - radius, pos.y, pos.z - radius)
        val p2 = Vector3f(pos.x - radius, pos.y, pos.z + radius)
        val p3 = Vector3f(pos.x + radius, pos.y, pos.z + radius)
        val p4 = Vector3f(pos.x + radius, pos.y, pos.z - radius)

        model.vertices.add(p1)
        model.vertices.add(p2)
        model.vertices.add(p3)
        model.vertices.add(p4)

        model.faces.addVertexIndices(arrayListOf(1, 2, 0, 1, 3, 2))
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
