package game_engine.graphics.objects

import game_engine.graphics.Mesh
import game_engine.graphics.Shader
import game_engine.maths.*
import org.joml.Vector3f
import physics_engine.Physic
import physics_engine.PhysicsObject

class Triangle(var v1: Vertex, var v2: Vertex, var v3: Vertex, pos: Vector3f = Vector3f(0f), var doubleSite: Boolean = false, flip: Boolean = false) : GameObject() {
    override var mesh: Mesh = Mesh()
    override var vertexArray: ArrayList<Vertex>  = arrayListOf()
    override var faces: ArrayList<Face> = arrayListOf()

    override var shader: Shader = Shader()

    override var transform: Transform = Transform()
    override var physics: ArrayList<Physic> = arrayListOf()
    override var physicsObject: PhysicsObject = PhysicsObject()

    private var order = arrayListOf(v1, v2, v3)

    init {
        transform.position = pos

        if (flip) {
            order = order.reversed() as ArrayList<Vertex>
        }
    }

    override fun createMesh() {
        vertexArray = order
        faces.addVertexIndices(arrayListOf(0, 1, 2))

        if (doubleSite) {
            faces = Face().addVertexIndices(faces, arrayListOf(0, 1, 2))
            vertexArray.addAll(order.reversed() as ArrayList<Vertex>)
        }

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