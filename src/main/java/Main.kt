import game_engine.graphics.objects.Cube
import game_engine.graphics.GameWindow
import game_engine.graphics.objects.GameObject
import game_engine.graphics.objects.Triangle
import game_engine.maths.Camera
import game_engine.maths.Vertex
import org.joml.Vector2f
import org.joml.Vector3f
import physics_engine.Gravity

//Const vars
const val vertexShader = "basic_vertex.glsl"
const val fragmentShader = "basic_fragment.glsl"

// Camera and GameWindow
val CAMERA: Camera = Camera()
lateinit var GAMEWINDOW: GameWindow

//Game Objects
val cube = Cube(Vector3f(2f))
val triangle = Triangle(
    Vertex(Vector3f(-1F, -1F, 0F), Vector3f(1f, 0f, 0f), Vector2f(0f, 0f)),
    Vertex(Vector3f(0F, 1F, 0F), Vector3f(0f, 1f, 0f), Vector2f(0f, 0f)),
    Vertex(Vector3f(1F, -1F, 0F), Vector3f(0f, 0f, 1f), Vector2f(0f, 0f))
)

var gameObjects = arrayListOf<GameObject>()


fun main() {
    CAMERA.fixCam = true
    GAMEWINDOW = GameWindow(1000, 1000, "Physics Engine")
    triangle.physics.add(Gravity())

    //gameObjects.add(cube)
    gameObjects.add(triangle)

    for (gameObject in gameObjects) {
        gameObject.createMesh()
        gameObject.createShader(vertexShader, fragmentShader)
    }

    CAMERA.setPerspective(GAMEWINDOW.getFOV(70.0), GAMEWINDOW.getAspectRatio(), 0.01f, 1000f)
    CAMERA.position = Vector3f(0f, 0f, 10f)
    CAMERA.rotation.rotateAxis(Math.toRadians(-20.0).toFloat(), Vector3f(1f, 0f, 0f))

    while (GAMEWINDOW.windowOpen()) {
        GAMEWINDOW.updateAfterLast()
        update()
        GAMEWINDOW.updateBeforeNext()
    }

    destroy()
    GAMEWINDOW.terminate()
}

fun update() {
    for (gameObject in gameObjects) {
        gameObject.useShader()
        gameObject.setCamera(CAMERA)
        gameObject.draw()
    }
}

fun destroy() {
    cube.destroy()
}
