package game_engine.graphics

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWVidMode
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengl.GL11.glClear

class GameWindow(var width: Int, var height: Int, var title: String) {
    private var window: Long = 0
    private var videoMode: GLFWVidMode? = null

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
        return !glfwWindowShouldClose(window)
    }

    fun update() {
        glfwPollEvents()

        glClear(GL_COLOR_BUFFER_BIT)
        glfwSwapBuffers(window)
    }

    fun terminate() {
        glfwTerminate()
    }
}