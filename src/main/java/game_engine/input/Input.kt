package game_engine.input

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWCursorPosCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.glfw.GLFWMouseButtonCallback


class Input {
    private var keys = IntArray(GLFW.GLFW_KEY_LAST)
    private var lastKeys = IntArray(GLFW.GLFW_KEY_LAST)

    private var buttons = IntArray(GLFW.GLFW_MOUSE_BUTTON_LAST)
    private var lastButtons = IntArray(GLFW.GLFW_MOUSE_BUTTON_LAST)

    private var mouseX: Double = 0.0
    private var mouseY: Double = 0.0

    fun setCursorPos(window: Long, x: Double, y: Double) {
        GLFW.glfwSetCursorPos(window, x, y)
    }

    private var keyboard: GLFWKeyCallback = object : GLFWKeyCallback() {
        override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
            keys[key] = action
        }
    }


    private var mousePos: GLFWCursorPosCallback = object : GLFWCursorPosCallback() {
        override fun invoke(window: Long, xpos: Double, ypos: Double) {
            mouseX = xpos
            mouseY = ypos
        }
    }


    private var mouseButton: GLFWMouseButtonCallback = object : GLFWMouseButtonCallback() {
        override fun invoke(window: Long, button: Int, action: Int, mods: Int) {
            buttons[button] = action
        }
    }

    fun update() {
        for (i in keys.indices) {
            lastKeys[i] = keys[i]
        }
        for (i in buttons.indices) {
            lastButtons[i] = buttons[i]
        }
    }

    fun keyState(key: Int): Int {
        return keys[key]
    }

    fun isKeyDown(key: Int): Boolean {
        return (keyState(key) != GLFW.GLFW_RELEASE)
    }

    fun isKeyUp(key: Int): Boolean {
        return (keyState(key) != GLFW.GLFW_PRESS)
    }

    fun isKeyReleased(key: Int): Boolean {
        return isKeyUp(key) && (lastKeys[key] != keys[key])
    }

    fun isKeyPressed(key: Int): Boolean {
        return isKeyDown(key) && (lastKeys[key] != keys[key])
    }

    fun mouseButtonState(button: Int): Int {
        return buttons[button]
    }

    fun isMouseButtonDown(button: Int): Boolean {
        return (mouseButtonState(button) != GLFW.GLFW_RELEASE)
    }

    fun isMouseButtonUp(button: Int): Boolean {
        return (mouseButtonState(button) != GLFW.GLFW_PRESS)
    }

    fun isMouseButtonReleased(button: Int): Boolean {
        return isMouseButtonUp(button) && (lastButtons[button] != buttons[button])
    }

    fun isMouseButtonPressed(button: Int): Boolean {
        return isMouseButtonDown(button) && (lastButtons[button] != buttons[button])
    }

    fun getKeyboardCallback(): GLFWKeyCallback {
        return keyboard
    }

    fun getMouseMoveCallback(): GLFWCursorPosCallback {
        return mousePos
    }

    fun getMouseButtonCallback(): GLFWMouseButtonCallback {
        return mouseButton
    }

    fun getMouseX(): Double {
        return mouseX
    }

    fun getMouseY(): Double {
        return mouseY
    }

    fun destroy() {
        keyboard.free()
        mousePos.free()
        mouseButton.free()
    }
}