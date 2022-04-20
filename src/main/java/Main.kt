import game_engine.graphics.GameWindow
import game_engine.graphics.lighting.DirectionalLight
import game_engine.graphics.objects.GameObject
import game_engine.graphics.objects.OBJ
import game_engine.maths.Camera
import org.joml.Vector3f
import physics_engine.PhysicsWorld
import physics_engine.RigidBody
import kotlin.math.cos
import kotlin.math.sin

//Const vars
const val vertexShader = "basic_vertex.glsl"
const val fragmentShader = "basic_fragment.glsl"

const val vertexLightShader = "lit_vertex.glsl"
const val fragmentLightShader = "lit_fragment.glsl"

// Camera and GameWindow
val CAMERA: Camera = Camera()
lateinit var GAMEWINDOW: GameWindow

// Light
var lightAngle = -90f
var lightIntensity = 10.0f
var lightPosition = Vector3f(-1f, -10f, 0f)
var lightColour = Vector3f(255f)
var directionalLight = DirectionalLight(lightColour, lightPosition, lightIntensity)

// Physics
var physicsWorld = PhysicsWorld()

// Game Objects
val torus = OBJ("torus")
val monkey = OBJ("monkey")
val plane = OBJ("plane")
val cube = OBJ("cube")
val icoBall = OBJ("ico_ball")
val uvBall = OBJ("uv_ball")
val bigIcoBall = OBJ("big_ico_ball")
val cone = OBJ("cone")

val grass_block = OBJ("Grass_Block", "Grass_Block_TEX.png")


var gameObjects = arrayListOf<GameObject>()


fun main() {
    var rigidBody = RigidBody()

    CAMERA.fixCam = false
    GAMEWINDOW = GameWindow(1000, 1000, "Physics Engine", directionalLight = directionalLight)
    GAMEWINDOW.disableLight()   // controls light

    //cube.obj.physics.add(Gravity())
    uvBall.load()
    plane.load()

    plane.obj.rigidBody = RigidBody().createPlane(0f, position = javax.vecmath.Vector3f(0f, -35f, 0f))

    plane.obj.transform.scale.x = 1f
    plane.obj.transform.scale.z = 1f

    gameObjects.add(uvBall.obj)
    gameObjects.add(plane.obj)

    physicsWorld.addRigidBody(uvBall.obj.rigidBody)
    physicsWorld.addRigidBody(uvBall.obj.rigidBody)

    for (gameObject in gameObjects) {
        gameObject.createMesh()
        if (GAMEWINDOW.usesLight) {
            gameObject.createShader(vertexLightShader, fragmentLightShader)
        } else {
            gameObject.createShader(vertexShader, fragmentShader)
        }
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

    // println(".... DT: ${GAMEWINDOW.deltaTime}")
    // println("Phys DT: ${GAMEWINDOW.physDT}")

    // Light update
    lightAngle += 0.005f * GAMEWINDOW.deltaTime.toFloat()
    val angRad = Math.toRadians(lightAngle.toDouble())
    directionalLight.direction.x = sin(angRad).toFloat()
    directionalLight.direction.y = cos(angRad).toFloat()
}

fun updatePhysics() {
    physicsWorld.stepSimulation(1f / GAMEWINDOW.FPS.fps)

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
