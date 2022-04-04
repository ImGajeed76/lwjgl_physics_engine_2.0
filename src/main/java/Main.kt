import game_engine.graphics.GameWindow
import game_engine.graphics.objects.GameObject
import game_engine.graphics.objects.OBJ
import game_engine.maths.Camera
import org.joml.Vector3f

//Const vars
const val vertexShader = "basic_vertex.glsl"
const val fragmentShader = "basic_fragment.glsl"

// Camera and GameWindow
val CAMERA: Camera = Camera()
lateinit var GAMEWINDOW: GameWindow

//Game Objects
val torus = OBJ("torus")
val monkey = OBJ("monkey")
val plane = OBJ("plane", "Grass.png")
val cube = OBJ("cube")
val icoBall = OBJ("ico_ball")
val uvBall = OBJ("uv_ball")
val bigIcoBall = OBJ("big_ico_ball")
val cone = OBJ("cone")

val grass_block = OBJ("Grass_Block", "Grass_Block_TEX.png")


var gameObjects = arrayListOf<GameObject>()


fun main() {
    CAMERA.fixCam = false
    GAMEWINDOW = GameWindow(1000, 1000, "Physics Engine")

    //cube.obj.physics.add(Gravity())

    grass_block.load()
    monkey.load()
    plane.load()

    plane.obj.transform.position.y = -7f
    plane.obj.transform.scale.x = 2f
    plane.obj.transform.scale.z = 2f

    gameObjects.add(monkey.obj)
    gameObjects.add(plane.obj)

    for (gameObject in gameObjects) {
        gameObject.createMesh()
        gameObject.createShader(vertexShader, fragmentShader)
    }

    CAMERA.setPerspective(GAMEWINDOW.getFOV(70.0), GAMEWINDOW.getAspectRatio(), 0.01f, 1000f)
    CAMERA.position = Vector3f(0f, 2f, 10f)

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


    if (gameObjects[0].physicsObject.position.y < -6) {
        gameObjects[0].physicsObject.velocity.y *= -1
    }

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
    for (gameObject in gameObjects) {
        gameObject.destroy()
    }
}
