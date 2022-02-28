package game_engine.graphics.objects

import game_engine.graphics.Mesh
import game_engine.graphics.Shader
import game_engine.maths.Camera
import game_engine.maths.Transform
import game_engine.maths.Vertex

class Triangle(var v1: Vertex, var v2: Vertex, var v3: Vertex) {
    var mesh: Mesh = Mesh()
    var shader: Shader = Shader()

    fun createMesh() {
        mesh.create(arrayOf(v1, v2, v3), intArrayOf(0, 1, 2))
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
}