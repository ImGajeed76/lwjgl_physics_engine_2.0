package game_engine.graphics

import GAMEWINDOW
import game_engine.graphics.lighting.DirectionalLight
import game_engine.maths.Camera
import game_engine.maths.Transform
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL20.*
import org.lwjgl.system.MemoryStack
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

    private var uniforms: HashMap<String, Int> = HashMap()
    private var camera: Camera = Camera()
    private var transform: Transform = Transform()

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

        createUniform("cameraProjection")
        createUniform("transformWorld")
        createUniform("transformObject")

        createUniform("texture_sampler")
        setUniform("texture_sampler", 0)

        if (GAMEWINDOW.usesLight) {
            createUniform("specularPower")
            createDirectionLightUniform("directionalLight")
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

    fun bind() {
        glUseProgram(programm)
    }

    fun unbind() {
        glUseProgram(0)
    }

    fun setLight(directionalLight: DirectionalLight, specularPower: Float = 10f) {
        if (GAMEWINDOW.usesLight) {
            setUniform("specularPower", specularPower)
            setUniform("directionalLight", directionalLight)
        }
    }

    fun setCamera(camera: Camera) {
        this.camera = camera

        setUniform("cameraProjection", camera.getProjection())
        setUniform("transformWorld", camera.getTransformation())
    }

    fun setTransform(transform: Transform) {
        this.transform = transform

        setUniform("transformObject", transform.getTransformation())
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

    private fun createUniform(uniformName: String) {
        val uniformLocation = glGetUniformLocation(programm, uniformName)

        if (uniformLocation < 0) {
            error("Error: Could not find uniform $uniformName")
        }

        uniforms[uniformName] = uniformLocation
    }

    private fun setUniform(uniformName: String, value: Matrix4f) {
        try {
            val stack = MemoryStack.stackPush()
            val fb = stack.mallocFloat(16)
            value.get(fb)
            uniforms[uniformName]?.let { glUniformMatrix4fv(it, false, fb) }

            stack.pop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setUniform(uniformName: String, value: Int) {
        uniforms[uniformName]?.let { glUniform1i(it, value) }
    }

    private fun setUniform(uniformName: String, value: Float) {
        uniforms[uniformName]?.let { glUniform1f(it, value) }
    }

    private fun setUniform(uniformName: String, value: Vector3f) {
        uniforms[uniformName]?.let { glUniform3f(it, value.x, value.y, value.z) }
    }

    private fun setUniform(uniformName: String, value: Vector4f) {
        uniforms[uniformName]?.let { glUniform4f(it, value.x, value.y, value.z, value.w) }
    }

    private fun setUniform(uniformName: String, value: DirectionalLight) {
        setUniform(uniformName, value.colour)
        setUniform(uniformName, value.direction)
        setUniform(uniformName, value.intensity)
    }

    fun createDirectionLightUniform(uniformName: String) {
        createUniform("$uniformName.colour")
        createUniform("$uniformName.direction")
        createUniform("$uniformName.intensity")
    }
}