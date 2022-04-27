package game_engine.graphics

import CAMERA
import game_engine.graphics.lighting.DirectionalLight
import game_engine.input.Input
import game_engine.maths.FPS
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWVidMode
import org.lwjgl.glfw.GLFWWindowSizeCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import java.awt.Dimension
import java.awt.Toolkit


class GameWindow(
    var width: Int,
    var height: Int,
    var title: String,
    var usesLight: Boolean = false,
    var directionalLight: DirectionalLight = DirectionalLight(Vector3f(1f), Vector3f(-100f, -100f, 0f), 1f)
) {
    private var window: Long = 0
    private var videoMode: GLFWVidMode? = null
    var clearColor = Vector4f(1f, 1f, 1f, 1f)

    var input = Input()
    private var isWindowOpen = true

    var FPS: FPS = FPS()
    var showFpsInTitle = true

    var deltaTime: Double = 0.001
    var lastDTCheck: Long = System.nanoTime()
    var dtScale: Double = 1.0

    // Physics
    var physDT: Double = 0.001
    var physUpdatesPerFrame: Int = 100
    var physDtScale: Double = 0.000000006
    // -------

    var currentWidth = width
    var currentHeight = height

    private var lastPosX = IntArray(1)
    private var lastPosY = IntArray(1)
    private var fullscreen = false
    private var lastIsFullscreen = false

    private var sizeCallback = object : GLFWWindowSizeCallback() {
        override fun invoke(window: Long, w: Int, h: Int) {
            currentWidth = w
            currentHeight = h
            glViewport(0, 0, currentWidth, currentHeight)
        }
    }

    init {
        if (!glfwInit()) {
            throw IllegalStateException("Failed to initialize GLFW!")
        }

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        window = glfwCreateWindow(width, height, title, 0, 0)
        if (window == 0.toLong()) {
            throw IllegalStateException("Failed to create window!")
        }
        glfwMakeContextCurrent(window)
        GL.createCapabilities()

        glEnable(GL_CULL_FACE)
        glEnable(GL_DEPTH_TEST)
        glDepthMask(true)

        videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
        if (videoMode == null) {
            throw IllegalStateException("Failed to create video mode!")
        }
        glfwSetWindowPos(window, (videoMode!!.width() - width) / 2, (videoMode!!.height() - height) / 2)

        glfwShowWindow(window)
        glfwSwapInterval(0)

        createCallbacks()
        input.setCursorPos(window, (width / 2).toDouble(), (height / 2).toDouble())
    }

    fun enableLight() {
        usesLight = true
        clearColor = Vector4f(0f, 0f, 0f, 1f)
    }

    fun disableLight() {
        usesLight = false
        clearColor = Vector4f(1f, 1f, 1f, 1f)
    }

    private fun createCallbacks() {
        glfwSetKeyCallback(window, input.getKeyboardCallback())
        glfwSetCursorPosCallback(window, input.getMouseMoveCallback())
        glfwSetMouseButtonCallback(window, input.getMouseButtonCallback())
        glfwSetWindowSizeCallback(window, sizeCallback)
    }

    fun windowOpen(): Boolean {
        return isWindowOpen && !glfwWindowShouldClose(window)
    }

    fun updateAfterLast() {
        checkForFullscreen()
        showFullscreen()

        CAMERA.setPerspective(getFOV(70.0), getAspectRatio(), 0.01f, 1000f)
        updateDeltaTime()
        updateFPS()
        checkExit()
        input.update()
        updateCam()
        glfwPollEvents()

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w)
    }

    private fun updateDeltaTime() {
        val current = System.nanoTime()
        val dif = current - lastDTCheck
        lastDTCheck = current

        deltaTime = (dif * dtScale) / 1000000

        if (deltaTime > 10) {
            deltaTime = 10.0
        }

        physDT = (deltaTime / physUpdatesPerFrame) * physDtScale
    }

    private fun updateCam() {

        input.setCursorPos(window, (width / 2).toDouble(), (height / 2).toDouble())

        if (input.isKeyDown(GLFW_KEY_W)) {
            CAMERA.forward(CAMERA.speed * deltaTime)
        }

        if (input.isKeyDown(GLFW_KEY_S)) {
            CAMERA.backwards(CAMERA.speed * deltaTime)
        }

        if (input.isKeyDown(GLFW_KEY_A)) {
            CAMERA.left(CAMERA.speed * deltaTime)
        }

        if (input.isKeyDown(GLFW_KEY_D)) {
            CAMERA.right(CAMERA.speed * deltaTime)
        }

        if (input.isKeyDown(GLFW_KEY_LEFT_SHIFT) || input.isKeyDown(GLFW_KEY_Q)) {
            CAMERA.down(CAMERA.speed * deltaTime)
        }

        if (input.isKeyDown(GLFW_KEY_SPACE) || input.isKeyDown(GLFW_KEY_E)) {
            CAMERA.up(CAMERA.speed * deltaTime)
        }

        val mouseX = input.getMouseX() - (height / 2)
        val mouseY = input.getMouseY() - (width / 2)

        CAMERA.moveRotation(mouseX.toFloat() * CAMERA.speed * deltaTime, mouseY.toFloat() * CAMERA.speed * deltaTime)
    }

    private fun checkExit() {
        if (input.isKeyPressed(GLFW_KEY_ESCAPE)) {
            isWindowOpen = false
        }
    }

    private fun updateFPS() {
        FPS.next()

        if (showFpsInTitle) {
            glfwSetWindowTitle(window, "$title | FPS: ${FPS.fps}")
        }
    }

    fun updateBeforeNext() {
        glfwSwapBuffers(window)
    }

    fun terminate() {
        glfwTerminate()
    }

    fun getAspectRatio(): Float {
        return currentWidth.toFloat() / currentHeight.toFloat()
    }

    fun getFOV(deg: Double): Float {
        return Math.toRadians(deg).toFloat()
    }

    private fun checkForFullscreen() {
        if (input.isKeyPressed(GLFW_KEY_F11) || input.isKeyPressed(GLFW_KEY_F)) {
            fullscreen = !fullscreen
        }
    }

    fun showFullscreen() {
        val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
        val screenW = screenSize.width
        val screenH = screenSize.height

        if (fullscreen && lastIsFullscreen != fullscreen) {
            currentWidth = screenW
            currentHeight = screenH
            glfwGetWindowPos(window, lastPosX, lastPosY)
            glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, currentWidth, currentHeight, 0)
        }
        else if (lastIsFullscreen != fullscreen) {
            currentWidth = width
            currentHeight = height
            glfwSetWindowMonitor(window, 0, lastPosX[0], lastPosY[0], currentWidth, currentHeight, 0)
        }

        lastIsFullscreen = fullscreen
    }
}