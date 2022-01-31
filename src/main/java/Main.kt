import game_engine.graphics.Cube
import game_engine.graphics.GameWindow
import game_engine.graphics.Mesh
import game_engine.graphics.Shader
import game_engine.maths.Camera
import game_engine.maths.Transform
import org.joml.Vector3f

val CAMERA: Camera = Camera()
val cube = Cube(Vector3f(2f))

val testTransform = Transform()
val testMesh = Mesh()
val testVertexArray = floatArrayOf(
    -1F, -1F, 0F, 0F, 1F, 0F, 1F, -1F, 0F
)

val basicShader = Shader()

fun main() {
    val gameWindow = GameWindow(640, 480, "Physics Engine")

    //testMesh.create(testVertexArray)
    //basicShader.create("basic_vertex.glsl", "basic_fragment.glsl")

    cube.createMesh()
    cube.createShader("basic_vertex.glsl", "basic_fragment.glsl")

    CAMERA.setPerspective(gameWindow.getFOV(70.0), gameWindow.getAspectRatio(), 0.01f, 1000f)
    CAMERA.position = Vector3f(0f, 2f, 10f)

    while (gameWindow.windowOpen()) {
        gameWindow.updateAfterLast()
        update()
        gameWindow.updateBeforeNext()
    }

    destroy()
    gameWindow.terminate()
}

fun update() {
    testTransform.rotation.rotateAxis(Math.toRadians(1.0).toFloat(), 0f, 1f, 0f)
    testTransform.rotation.rotateAxis(Math.toRadians(0.5).toFloat(), 1f, 0f, 0f)

    //basicShader.useShader()
    //basicShader.setCamera(camera)
    //basicShader.setTransform(testTransform)
    //testMesh.draw()

    cube.useShader()
    cube.setCamera(CAMERA)
    cube.setTransform(testTransform)
    cube.draw()
}

fun destroy() {
    basicShader.destroy()
    testMesh.destroy()
}
