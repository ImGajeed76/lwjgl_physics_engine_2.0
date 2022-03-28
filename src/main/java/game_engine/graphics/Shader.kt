package game_engine.graphics

import game_engine.maths.Camera
import game_engine.maths.Transform
import org.lwjgl.opengl.GL20.*
import java.io.File
import java.io.IOException

const val resourcePath = "src/main/resources"

class Shader {
    private var vertexShader: Int = 0
    private var fragmentShader: Int = 0
    private var programm: Int = 0

    private var uniMatProjection: Int = 0
    private var uniMatTransformWorld: Int = 0
    private var uniMatTransformObject: Int = 0

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

        uniMatProjection = glGetUniformLocation(programm, "cameraProjection")
        uniMatTransformWorld = glGetUniformLocation(programm, "transformWorld")
        uniMatTransformObject = glGetUniformLocation(programm, "transformObject")

        return true
    }

    fun destroy() {
        glDetachShader(programm, vertexShader)
        glDetachShader(programm, fragmentShader)

        glDeleteShader(vertexShader)
        glDeleteShader(fragmentShader)

        glDeleteProgram(programm)
    }

    fun bind() {
        glUseProgram(programm)
    }

    fun unbind() {
        glUseProgram(0)
    }

    fun setCamera(camera: Camera) {
        if (uniMatProjection != -1) {
            val matrix = FloatArray(16)
            camera.getProjection().get(matrix)
            glUniformMatrix4fv(uniMatProjection, false, matrix)
        }

        if (uniMatTransformWorld != -1) {
            val matrix = FloatArray(16)
            camera.getTransformation().get(matrix)
            glUniformMatrix4fv(uniMatTransformWorld, false, matrix)
        }
    }

    fun setTransform(transform: Transform) {
        if (uniMatTransformObject != -1) {
            val matrix = FloatArray(16)
            transform.getTransformation().get(matrix)
            glUniformMatrix4fv(uniMatTransformObject, false, matrix)
        }
    }

    private fun readSource(file: String): String {
        val reader: File
        var source = ""

        try {
            reader = File("$resourcePath/shaders/$file")
            source = reader.readText()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return source
    }
}