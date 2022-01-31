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
    }

    fun windowOpen(): Boolean {
        return isWindowOpen && !glfwWindowShouldClose(window)
    }

    fun updateAfterLast() {
        updateFPS()
        checkExit()
        input.update()
        //updateCam()
        glfwPollEvents()

        glClear(GL_COLOR_BUFFER_BIT)
        glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w)
    }

    fun updateCam() {
        if (input.isKeyDown(GLFW_KEY_W)) {
            CAMERA.position.x -= CAMERA.speed * sin(-CAMERA.rotation.y)
            CAMERA.position.y -= CAMERA.speed * sin(-CAMERA.rotation.x)
            CAMERA.position.z -= CAMERA.speed * -cos(-CAMERA.rotation.y)
            CAMERA.position.z -= CAMERA.speed
        }

        if (input.isKeyDown(GLFW_KEY_S)) {
            CAMERA.position.x += CAMERA.speed * sin(-CAMERA.rotation.y)
            CAMERA.position.y += CAMERA.speed * sin(-CAMERA.rotation.x)
            CAMERA.position.z += CAMERA.speed * -cos(-CAMERA.rotation.y)
            CAMERA.position.z += CAMERA.speed
        }

        if (input.isKeyDown(GLFW_KEY_A)) {
            CAMERA.position.z -= CAMERA.speed * sin(-CAMERA.rotation.y)
            CAMERA.position.x -= CAMERA.speed * -cos(-CAMERA.rotation.y)
            CAMERA.position.x -= CAMERA.speed
        }

        if (input.isKeyDown(GLFW_KEY_D)) {
            CAMERA.position.z += CAMERA.speed * sin(-CAMERA.rotation.y)
            CAMERA.position.x += CAMERA.speed * -cos(-CAMERA.rotation.y)
            CAMERA.position.x += CAMERA.speed
        }

        if (input.isKeyDown(GLFW_KEY_LEFT_SHIFT) || input.isKeyDown(GLFW_KEY_Q)) {
            CAMERA.position.y -= CAMERA.speed
        }

        if (input.isKeyDown(GLFW_KEY_SPACE) || input.isKeyDown(GLFW_KEY_E)) {
            CAMERA.position.y += CAMERA.speed
        }

        val mouseX = input.getMouseX() - width / 2
        val mouseY = input.getMouseY() - height / 2

        //CAMERA.rotation.x += (mouseX * CAMERA.speed).toFloat()
        //CAMERA.rotation.y += (mouseY * CAMERA.speed).toFloat()

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