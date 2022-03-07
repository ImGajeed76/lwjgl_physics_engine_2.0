package game_engine.graphics.objects

import GAMEWINDOW
import game_engine.graphics.Mesh
import game_engine.graphics.Shader
import game_engine.maths.Camera
import game_engine.maths.Transform
import game_engine.maths.Vertex
import org.joml.Vector3f
import physics_engine.Physic
import physics_engine.PhysicsObject

class Triangle(var v1: Vertex, var v2: Vertex, var v3: Vertex) : GameObject() {
    override var mesh: Mesh = Mesh()
    override var vertexArray: ArrayList<Vertex>  = arrayListOf()
    override var indices: ArrayList<Int> = arrayListOf()

    override var shader: Shader = Shader()

    override var transform: Transform = Transform()
    override var physics: ArrayList<Physic> = arrayListOf()
    override var physicsObject: PhysicsObject = PhysicsObject()

    override fun createMesh() {
        vertexArray = arrayListOf(v1, v2, v3)
        indices = arrayListOf(0, 1, 2)

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