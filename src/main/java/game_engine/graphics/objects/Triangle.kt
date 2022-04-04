package game_engine.graphics.objects

import game_engine.graphics.Mesh
import game_engine.graphics.Model
import game_engine.graphics.Shader
import game_engine.maths.*
import org.joml.Vector3f
import physics_engine.Physic
import physics_engine.PhysicsObject

class Triangle(var v1: Vector3f, var v2: Vector3f, var v3: Vector3f, pos: Vector3f = Vector3f(0f), var doubleSite: Boolean = false, flip: Boolean = false) : GameObject() {
    override var mesh: Mesh = Mesh()
    override var model: Model = Model()

    override var shader: Shader = Shader()

    override var transform: Transform = Transform()
    override var physics: ArrayList<Physic> = arrayListOf()
    override var physicsObject: PhysicsObject = PhysicsObject()

    private var order = arrayListOf(v1, v2, v3)

    init {
        transform.position = pos

        if (flip) {
            order = order.reversed() as ArrayList<Vector3f>
        }
    }

    override fun createMesh() {
        model.vertices = order
        model.faces.addVertexIndices(arrayListOf(0, 1, 2))

        if (doubleSite) {
            model.faces = Face().addVertexIndices(model.faces, arrayListOf(0, 1, 2))
            model.vertices.addAll(order.reversed() as ArrayList<Vector3f>)
        }

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