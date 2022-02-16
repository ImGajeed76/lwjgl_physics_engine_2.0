import game_engine.graphics.Cube
import game_engine.graphics.GameWindow
import game_engine.graphics.Mesh
import game_engine.graphics.Shader
import game_engine.maths.Camera
import game_engine.maths.Transform
import org.joml.Quaternionf
import org.joml.Vector3f

val CAMERA: Camera = Camera()
val cube = Cube(Vector3f(2f))

lateinit var gameWindow: GameWindow

val testTransform = Transform()
val testMesh = Mesh()
val testVertexArray = floatArrayOf(
    -1F, -1F, 0F, 0F, 1F, 0F, 1F, -1F, 0F
)

val basicShader = Shader()

var rotY = 0.0

fun main() {
    //testMesh.create(testVertexArray)
    //basicShader.create("basic_vertex.glsl", "basic_fragment.glsl")

    gameWindow = GameWindow(1000, 1000, "Physics Engine")

    cube.createMesh()
    cube.createShader("basic_vertex.glsl", "basic_fragment.glsl")

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
    //rotY += 0.1 * gameWindow.deltaTime
    //rotY %= 360
    //val newRot = Quaternionf(0f, Math.toRadians(rotY).toFloat(), 0f, 1f).normalize()
    //println(newRot.mul(testTransform.rotation.normalize()).mul(newRot.conjugate()))

    //testTransform.rotation.set(newRot.mul(testTransform.rotation.normalize()).mul(newRot.conjugate()))

    //testTransform.rotation.rotateAxis(Math.toRadians(1.0 * gameWindow.deltaTime).toFloat(), 0f, 1f, 0f)
    //testTransform.rotation.rotateAxis(Math.toRadians(0.5 * gameWindow.deltaTime).toFloat(), 1f, 0f, 0f)

    cube.useShader()
    cube.setCamera(CAMERA)
    cube.setTransform(testTransform)
    cube.draw()
}

fun destroy() {
    cube.destroy()
}
