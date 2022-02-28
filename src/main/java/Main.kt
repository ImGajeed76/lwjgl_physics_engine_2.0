import game_engine.graphics.objects.Cube
import game_engine.graphics.GameWindow
import game_engine.graphics.Mesh
import game_engine.graphics.Shader
import game_engine.graphics.objects.Triangle
import game_engine.maths.Camera
import game_engine.maths.Transform
import game_engine.maths.Vertex
import org.joml.Vector2f
import org.joml.Vector3f
import kotlin.math.sin

val CAMERA: Camera = Camera()

val cube = Cube(Vector3f(2f))
val tri = Triangle(
    Vertex(Vector3f(-1F, -1F, 0F), Vector3f(1f, 0f, 0f), Vector2f(0f, 0f)),
    Vertex(Vector3f(0F, 1F, 0F), Vector3f(0f, 1f, 0f), Vector2f(0f, 0f)),
    Vertex(Vector3f(1F, -1F, 0F), Vector3f(0f, 0f, 1f), Vector2f(0f, 0f))
)

lateinit var gameWindow: GameWindow

val vs = "lit_vertex.glsl"
val fs = "lit_fragment.glsl"

val testTransform = Transform()
val testMesh = Mesh()
val testVertexArray = floatArrayOf(
    -1F, -1F, 0F, 0F, 1F, 0F, 1F, -1F, 0F
)

val basicShader = Shader()

var per = 0.0

fun main() {
    //testMesh.create(testVertexArray)
    //basicShader.create("basic_vertex.glsl", "basic_fragment.glsl")

    gameWindow = GameWindow(1000, 1000, "Physics Engine")

    //cube.createMesh()
    //cube.createShader("basic_vertex.glsl", "basic_fragment.glsl")

    tri.createMesh()
    tri.createShader(vs, fs)
    testTransform.position.y = 5f

    CAMERA.setPerspective(gameWindow.getFOV(70.0), gameWindow.getAspectRatio(), 0.01f, 1000f)
    CAMERA.position = Vector3f(0f, 0f, 10f)

    while (gameWindow.windowOpen()) {
        gameWindow.updateAfterLast()
        update()
        gameWindow.updateBeforeNext()
    }

    destroy()
    gameWindow.terminate()
}

fun update() {
    testTransform.rotation.rotateAxis(Math.toRadians(1.0 * gameWindow.deltaTime).toFloat(), 0f, 1f, 0f)
    testTransform.rotation.rotateAxis(Math.toRadians(0.5 * gameWindow.deltaTime).toFloat(), 1f, 0f, 0f)

    per += 0.01f * gameWindow.deltaTime
    testTransform.position.y = (sin(per).toFloat())

    //cube.useShader()
    //cube.setCamera(CAMERA)
    //cube.setTransform(testTransform)
    //cube.draw()

    tri.useShader()
    tri.setCamera(CAMERA)
    tri.setTransform(testTransform)
    tri.draw()
}

fun destroy() {
    cube.destroy()
}
