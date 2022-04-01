import game_engine.graphics.GameWindow
import game_engine.graphics.objects.*
import game_engine.loaders.Loader
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
    Vertex(Vector3f(-1F, -1F, 0F), Vector3f(1f, 0f, 0f)),
    Vertex(Vector3f(0F, 1F, 0F), Vector3f(0f, 1f, 0f)),
    Vertex(Vector3f(1F, -1F, 0F), Vector3f(0f, 0f, 1f)),
    doubleSite = true
)
val ground = Triangle(
    Vertex(Vector3f(-10f, -5.5f, -5f), Vector3f(0.4f, 0.4f, 0.4f)),
    Vertex(Vector3f(10f, -5.5f, -5f), Vector3f(0.4f, 0.4f, 0.4f)),
    Vertex(Vector3f(0f, -5.5f, 5f), Vector3f(0.4f, 0.4f, 0.4f)),
    flip = true
)
val customObject = CustomObject(Loader().loadOBJ("monkey"))
val plane = Plane(Vector3f(0.5f), 2f)

var gameObjects = arrayListOf<GameObject>()


fun main() {
    CAMERA.fixCam = false
    GAMEWINDOW = GameWindow(1000, 1000, "Physics Engine")
    triangle.physics.add(Gravity())

    //gameObjects.add(triangle)
    //gameObjects.add(ground)
    gameObjects.add(customObject)
    //gameObjects.add(cube)
    //gameObjects.add(plane)

    for (gameObject in gameObjects) {
        gameObject.createMesh()
        gameObject.createShader(vertexShader, fragmentShader)
    }

    CAMERA.setPerspective(GAMEWINDOW.getFOV(70.0), GAMEWINDOW.getAspectRatio(), 0.01f, 1000f)
    CAMERA.position = Vector3f(0f, 2f, 10f)
    CAMERA.rotation.rotateAxis(Math.toRadians(-0.0).toFloat(), Vector3f(1f, 0f, 0f))

    while (GAMEWINDOW.windowOpen()) {
        GAMEWINDOW.updateAfterLast()

        updatePhysics()
        update()

        GAMEWINDOW.drawObjects(gameObjects)
        GAMEWINDOW.updateBeforeNext()
    }

    destroy()
    GAMEWINDOW.terminate()
}

fun update() {
    gameObjects[0].transform.rotation.rotateAxis(Math.toRadians(0.1 * GAMEWINDOW.deltaTime).toFloat(), 0f, 1f, 0f)


    // println(".... DT: ${GAMEWINDOW.deltaTime}")
    // println("Phys DT: ${GAMEWINDOW.physDT}")
}

fun updatePhysics() {
    for (i in 0 until GAMEWINDOW.physUpdatesPerFrame) {
        for (gameObject in gameObjects) {
            gameObject.updatePhysics(gameObjects)
        }
    }
}

fun destroy() {
    cube.destroy()
}
