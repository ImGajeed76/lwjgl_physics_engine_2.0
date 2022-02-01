package game_engine.graphics

import CAMERA
import game_engine.input.Input
import org.joml.Math.cos
import org.joml.Math.sin
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

    var FPS = 0F
    var frames = 0F
    var fpsTimer: Long = System.currentTimeMillis()
    var showFpsInTitle = true

    var deltaTime = 1f

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
        if (FPS > 0) {
            deltaTime = 60 / FPS
        }
    }

    fun updateCam() {
        if (input.isKeyDown(GLFW_KEY_W)) {
            //CAMERA.position.x -= CAMERA.speed * sin(-CAMERA.rotation.y) * deltaTime
            //CAMERA.position.y -= CAMERA.speed * sin(-CAMERA.rotation.x) * deltaTime
            //CAMERA.position.z -= CAMERA.speed * -cos(-CAMERA.rotation.y) * deltaTime
            CAMERA.position.z -= CAMERA.speed * deltaTime
        }

        if (input.isKeyDown(GLFW_KEY_S)) {
            //CAMERA.position.x += CAMERA.speed * sin(-CAMERA.rotation.y) * deltaTime
            //CAMERA.position.y += CAMERA.speed * sin(-CAMERA.rotation.x) * deltaTime
            //CAMERA.position.z += CAMERA.speed * -cos(-CAMERA.rotation.y) * deltaTime
            CAMERA.position.z += CAMERA.speed * deltaTime
        }

        if (input.isKeyDown(GLFW_KEY_A)) {
            //CAMERA.position.z -= CAMERA.speed * sin(-CAMERA.rotation.y) * deltaTime
            //CAMERA.position.x -= CAMERA.speed * -cos(-CAMERA.rotation.y) * deltaTime
            CAMERA.position.x -= CAMERA.speed * deltaTime
        }

        if (input.isKeyDown(GLFW_KEY_D)) {
            //CAMERA.position.z += CAMERA.speed * sin(-CAMERA.rotation.y) * deltaTime
            //CAMERA.position.x += CAMERA.speed * -cos(-CAMERA.rotation.y) * deltaTime
            CAMERA.position.x += CAMERA.speed * deltaTime
        }

        if (input.isKeyDown(GLFW_KEY_LEFT_SHIFT) || input.isKeyDown(GLFW_KEY_Q)) {
            CAMERA.position.y -= CAMERA.speed * deltaTime
        }

        if (input.isKeyDown(GLFW_KEY_SPACE) || input.isKeyDown(GLFW_KEY_E)) {
            CAMERA.position.y += CAMERA.speed * deltaTime
        }

        val mouseX = input.getMouseX() - width / 2
        val mouseY = input.getMouseY() - height / 2

        CAMERA.rotation.rotateAxis(Math.toRadians((mouseX * CAMERA.speed * deltaTime)).toFloat(), 0f, -1f, 0f)
        CAMERA.rotation.rotateAxis(Math.toRadians((mouseY * CAMERA.speed * deltaTime)).toFloat(), -1f, 0f, 0f)

        input.setCursorPos(window, (width / 2).toDouble(), (height / 2).toDouble())
    }

    private fun checkExit() {
        if (input.isKeyPressed(GLFW_KEY_ESCAPE)) {
            isWindowOpen = false
        }
    }

    private fun updateFPS() {
        frames++
        if (System.currentTimeMillis() > fpsTimer + 1000) {
            FPS = frames
            fpsTimer = System.currentTimeMillis()
            frames = 0F
        }

        if (showFpsInTitle) {
            glfwSetWindowTitle(window, "$title | FPS: $FPS")
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