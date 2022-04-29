import game_engine.graphics.GameWindow
import game_engine.graphics.lighting.DirectionalLight
import game_engine.graphics.lighting.Material
import game_engine.graphics.lighting.PointLight
import game_engine.graphics.objects.OBJ
import game_engine.graphics.objects.ObjectContainer
import game_engine.maths.Camera
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW
import physics_engine.*
import javax.vecmath.Quat4f
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
var lightIntensity = 100f
var lightPosition = Vector3f(0f)
var lightColour = Vector3f(1f)
var sunLight = DirectionalLight(lightColour, lightPosition, lightIntensity)

var sun2 = PointLight("sun", lightColour, Vector3f(-7f, 1f, 0f), 100f, 0f, 0f, 1f)

// Physics
var physicsWorld = PhysicsWorld()

// Game Objects
val torus = OBJ("torus")
val monkey = OBJ("monkey")
val plane = OBJ("plane", "Grass.png")
val cube = OBJ("cube")
val icoBall = OBJ("ico_ball")
val uvBall = OBJ("uv_ball")
val cone = OBJ("cone")
val grass_block = OBJ("Grass_Block", "Grass_Block_TEX.png")

var objectContainer = ObjectContainer()

// Materials
val mat1 = Material(Vector4f(1f), 150f)


fun main() {
    CAMERA.fixCam = false
    GAMEWINDOW = GameWindow(1000, 1000, "Physics Engine", directionalLight = sunLight)
    GAMEWINDOW.disableLight()   // controls light

    // Object and Physics loading
    objectContainer.createShaders(vertexShader, fragmentShader, vertexLightShader, fragmentLightShader)
    objectContainer.createPointLight(sun2)

    icoBall.load()
    objectContainer.addObject(icoBall)

    plane.load()
    plane.obj.rigidBody = RigidBody().createPlane(0f, position = javax.vecmath.Vector3f(0f, -0.5f, 0f))

    objectContainer.addObject(plane)
    physicsWorld.addRigidBody(plane)

    physicsWorld.updateAABBs()
    objectContainer.initNewObjects()


    //createCubePyramid("Grass_Block", "Grass_Block_TEX.png", 1f, 8, scale = Vector3f(0.5f))
    // Finished

    CAMERA.setPerspective(GAMEWINDOW.getFOV(70.0), GAMEWINDOW.getAspectRatio(), 0.01f, 1000f)
    CAMERA.position = Vector3f(0f, 2f, 10f)

    while (GAMEWINDOW.windowOpen()) {
        GAMEWINDOW.updateAfterLast()

        update()
        updatePhysics()

        objectContainer.draw(arrayListOf(sun2))
        GAMEWINDOW.updateBeforeNext()
    }

    destroy()
    GAMEWINDOW.terminate()
}

fun update() {
    updateSunLight()
    checkShoot()
    checkCubeSpawn()
    checkPyraSpawn()
    checkMaterialSpawn()
}

fun updateSunLight() {
    lightAngle += 0.005f * GAMEWINDOW.deltaTime.toFloat()

    /*
    if (lightAngle > 90) {
        sunLight.intensity = 0f
        if (lightAngle >= 360) {
            lightAngle = -90f
        }
    } else if (lightAngle <= -80 || lightAngle >= 80) {
        val factor = 1 - (abs(lightAngle) - 80) / 10f
        sunLight.intensity = factor
        sunLight.colour.y = max(factor, 0.9f)
        sunLight.colour.z = max(factor, 0.5f)
    } else {
        sunLight.intensity = 1f
        sunLight.colour = Vector3f(1f)
    }
    */

    val angRad = Math.toRadians(lightAngle.toDouble())
    sunLight.direction.x = sin(angRad).toFloat()
    sunLight.direction.y = cos(angRad).toFloat()

    objectContainer[0].transform.position = sunLight.direction.mul(100f)

    GAMEWINDOW.directionalLight = sunLight
}

fun updatePhysics() {
    physicsWorld.stepSimulation(1f / GAMEWINDOW.FPS.fps)
    objectContainer.updatePhysics()
}

fun destroy() {
    objectContainer.destroy()
}

fun createCubePyramid(
    objName: String = "cube",
    texture: String = "default.png",
    mass: Float = 2f,
    height: Int = 5,
    position: Vector3f = Vector3f(0f),
    scale: Vector3f = Vector3f(0.5f),
    rbWidth: Float = 2f,
    rbHeight: Float = 2f
) {
    val rW = rbWidth * scale.x
    val rH = rbHeight * scale.y

    for (i in 0 until height) {
        for (j in 0 until height - i) {
            val widthOffset = (j * rW) + (i * (rW / 2)) - (height / 2 * rW)

            val pos = javax.vecmath.Vector3f(0f + position.x, (i * rH) + position.y, widthOffset + position.z)
            println(pos)

            val obj = OBJ(objName, texture, autoLoad = true).obj
            val rb = RigidBody().createCube(mass, position = pos, scale = scale.toVecMath())

            obj.rigidBody = rb

            objectContainer.addObject(obj)
            physicsWorld.addRigidBody(obj)
        }
    }

    physicsWorld.updateAABBs()
    objectContainer.initNewObjects()
}

fun checkShoot() {
    if (GAMEWINDOW.input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
        shoot()
    }
}

fun checkCubeSpawn() {
    if (GAMEWINDOW.input.isKeyPressed(GLFW.GLFW_KEY_C)) {
        spawnCube(objName = "cube", position = CAMERA.position.copy().add(CAMERA.getForward()))
    }
}

fun checkPyraSpawn() {
    if (GAMEWINDOW.input.isKeyPressed(GLFW.GLFW_KEY_P)) {
        createCubePyramid(position = Vector3f(CAMERA.position.x, 0f, CAMERA.position.z), height = 8)
    }
}

fun checkMaterialSpawn() {
    if (GAMEWINDOW.input.isKeyPressed(GLFW.GLFW_KEY_H)) {
        spawnMaterial("uv_ball", material = Material(Vector3f(1f), D_HELIUM), type = RB_SPHERE)
    }

    if (GAMEWINDOW.input.isKeyPressed(GLFW.GLFW_KEY_G)) {
        spawnCubeCube(position = CAMERA.position.copy(), divider = 7)
    }
}

fun spawnMaterial(
    objName: String = "cube",
    texName: String = "default.png",
    material: physics_engine.Material,
    position: Vector3f = CAMERA.position.copy(),
    type: Int = RB_CUBE
) {
    val obj = OBJ(objName, texName, true).obj
    val rb = RigidBody().create(type, material, position)
    obj.rigidBody = rb

    objectContainer.addObject(obj)
    physicsWorld.addRigidBody(obj)

    physicsWorld.updateAABBs()
    objectContainer.initNewObjects()
}

fun shoot(
    objName: String = "uv_ball", scale: Float = 1f, mass: Float = 5f, velPow: Float = 80f
) {
    val pos = CAMERA.position.copy()
    val camDir = CAMERA.getForward()
    val dir = camDir.toVecMath()
    dir.normalize()
    dir.scale(velPow)

    val ball = OBJ(objName, autoLoad = true).obj
    val rb = RigidBody().createSphere(0.7f * scale, mass, position = pos.toVecMath())
    rb.rigidBody.activate(true)
    rb.rigidBody.applyCentralImpulse(dir)

    ball.rigidBody = rb

    objectContainer.addObject(ball)
    physicsWorld.addRigidBody(ball)

    physicsWorld.updateAABBs()
    objectContainer.initNewObjects()
}

fun spawnCubeCube(
    divider: Int = 2,
    objName: String = "cube",
    texName: String = "default.png",
    mat: physics_engine.Material = Material(Vector3f(1f), D_IRON),
    position: Vector3f = Vector3f(0f)
) {
    val scale = Vector3f(
        (mat.getVolume() / divider), (mat.getVolume() / divider), (mat.getVolume() / divider)
    )

    val cube = OBJ(objName, texName, true)
    cube.load()

    for (i in 0 until divider) {
        for (j in 0 until divider) {
            for (k in 0 until divider) {
                val pos = Vector3f(
                    position.x + (i * scale.x * 2),
                    j * scale.y * 2 - 0.5f,
                    position.z + (k * scale.z * 2)
                )
                val cube_copy = cube.copy()
                val rb = RigidBody().createCube(mat.mass, pos.toVecMath(), scale = scale.toVecMath())
                cube_copy.rigidBody = rb

                objectContainer.addObject(cube_copy)
                physicsWorld.addRigidBody(cube_copy)
            }
        }
    }

    physicsWorld.updateAABBs()
    objectContainer.initNewObjects()

    println("${scale.x}, ${scale.y}, ${scale.z}")
}

fun spawnCube(
    objName: String = "cube",
    textureName: String = "default.png",
    mass: Float = 2f,
    position: Vector3f = Vector3f(0f),
    rotation: Quat4f = Quat4f(0f, 0f, 0f, 1f),
    scale: Vector3f = Vector3f(0.5f)
) {
    val cube = OBJ(objName, textureName, true).obj
    val rb = RigidBody().createCube(mass, position.toVecMath(), rotation, scale.toVecMath())
    rb.rigidBody.activate(true)

    cube.rigidBody = rb

    objectContainer.addObject(cube)
    physicsWorld.addRigidBody(cube)

    physicsWorld.updateAABBs()
    objectContainer.initNewObjects()
}

private fun Vector3f.toVecMath(): javax.vecmath.Vector3f {
    return javax.vecmath.Vector3f(this.x, this.y, this.z)
}

private fun javax.vecmath.Vector3f.toJoml(): Vector3f {
    return Vector3f(this.x, this.y, this.z)
}

private fun Vector3f.copy(): Vector3f {
    return Vector3f(this.x, this.y, this.z)
}
