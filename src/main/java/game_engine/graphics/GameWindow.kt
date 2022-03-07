package game_engine.graphics

import CAMERA
import game_engine.input.Input
import game_engine.maths.FPS
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWVidMode
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*

class GameWindow(var width: Int, var height: Int, var title: String) {
    private var window: Long = 0
    private var videoMode: GLFWVidMode? = null
    var clearColor = Vector4f(1f, 1f, 1f, 1f)

    private var input = Input()
    private var isWindowOpen = true

    var FPS: FPS = FPS()
    var showFpsInTitle = true

    var deltaTime = 1f

    var camRotX = 0f
    var camRotY = 0f

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

        videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
        if (videoMode == null) {
            throw IllegalStateException("Failed to create video mode!")
        }
        glfwSetWindowPos(window, (videoMode!!.width() - width) / 2, (videoMode!!.height() - height) / 2)

        glfwShowWindow(window)
        glfwSwapInterval(0)

        createCallbacks()
        input.setCursorPos(window, (width / 2).toDouble(), (height / 2).toDouble())
        CAMERA.rotation.setAngleAxis(0f, 1f, 1f, 1f)
    }

    private fun createCallbacks() {
        glfwSetKeyCallback(window, input.getKeyboardCallback())
        glfwSetCursorPosCallback(window, input.getMouseMoveCallback())
        glfwSetMouseButtonCallback(window, input.getMouseButtonCallback())
    }

    fun windowOpen(): Boolean {
        return isWindowOpen && !glfwWindowShouldClose(window)
    }

    fun updateAfterLast() {
        updateDeltaTime()
        updateFPS()
        checkExit()
        input.update()
        updateCam()
        glfwPollEvents()

        glClear(GL_COLOR_BUFFER_BIT)
        glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w)
    }

    private fun updateDeltaTime() {
        if (FPS.fps > 0) {
            deltaTime = 60 / FPS.fps
        }
    }

    fun updateCam() {
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

        val mouseX = input.getMouseX() - (width / 2)
        val mouseY = input.getMouseY() - (height / 2)

        CAMERA.turnHorizontal(mouseX.toFloat() * CAMERA.speed * deltaTime)
        CAMERA.turnVertical(mouseY.toFloat() * CAMERA.speed * deltaTime)

        input.setCursorPos(window, (width / 2).toDouble(), (height / 2).toDouble())
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
        return (width / height).toFloat()
    }

    fun getFOV(deg: Double): Float {
        return Math.toRadians(deg).toFloat()
    }
}