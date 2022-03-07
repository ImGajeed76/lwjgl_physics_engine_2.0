package game_engine.graphics.objects

import game_engine.graphics.Mesh
import game_engine.graphics.Shader
import game_engine.maths.Camera
import game_engine.maths.Transform
import game_engine.maths.Vertex
import org.joml.Vector3f
import physics_engine.Physic
import physics_engine.PhysicsObject

abstract class GameObject {
    abstract var mesh: Mesh
    abstract var vertexArray: ArrayList<Vertex>
    abstract var indices: ArrayList<Int>

    abstract var shader: Shader

    abstract var transform: Transform
    abstract var physics: ArrayList<Physic>
    abstract var physicsObject: PhysicsObject

    abstract fun createMesh()
    abstract fun createShader(vertex_shader: String, fragment_shader: String)
    abstract fun setCamera(camera: Camera)
    abstract fun useShader()
    abstract fun draw()
    abstract fun destroy()
}