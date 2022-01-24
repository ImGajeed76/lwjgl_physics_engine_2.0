package game_engine.graphics

import org.lwjgl.opengl.GL20.*
import java.io.File
import java.io.IOException

const val resourcePath = "src/main/resources"

class Shader {
    private var vertexShader: Int = 0
    private var fragmentShader: Int = 0
    private var programm: Int = 0

    fun create(vertex_shader: String, fragment_shader: String): Boolean {

        vertexShader = glCreateShader(GL_VERTEX_SHADER)
        glShaderSource(vertexShader, readSource(vertex_shader))
        glCompileShader(vertexShader)

        var success = glGetShaderi(vertexShader, GL_COMPILE_STATUS)
        if (success == GL_FALSE) {
            error(glGetShaderInfoLog(vertexShader))
        }

        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER)
        glShaderSource(fragmentShader, readSource(fragment_shader))
        glCompileShader(fragmentShader)

        success = glGetShaderi(fragmentShader, GL_COMPILE_STATUS)
        if (success == GL_FALSE) {
            error(glGetShaderInfoLog(fragmentShader))
        }

        programm = glCreateProgram()
        glAttachShader(programm, vertexShader)
        glAttachShader(programm, fragmentShader)

        glLinkProgram(programm)
        success = glGetProgrami(programm, GL_LINK_STATUS)
        if (success == GL_FALSE) {
            error(glGetProgramInfoLog(programm))
        }

        glValidateProgram(programm)
        success = glGetProgrami(programm, GL_VALIDATE_STATUS)
        if (success == GL_FALSE) {
            error(glGetProgramInfoLog(programm))
        }

        return true
    }

    fun destroy() {
        glDetachShader(programm, vertexShader)
        glDetachShader(programm, fragmentShader)

        glDeleteShader(vertexShader)
        glDeleteShader(fragmentShader)

        glDeleteProgram(programm)
    }

    fun useShader() {
        glUseProgram(programm)
    }

    private fun readSource(file: String): String {
        var reader: File = File("")
        var source: String = ""

        try {
            reader = File("$resourcePath/shaders/$file")
            source = reader.readText()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return source
    }
}