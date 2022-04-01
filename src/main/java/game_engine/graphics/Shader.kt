package game_engine.graphics

import game_engine.maths.Camera
import game_engine.maths.Material
import game_engine.maths.PointLight
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
        this.camera = camera

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
        this.transform = transform

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

    private fun createUniform(uniformName: String) {
        val uniformLocation = glGetUniformLocation(programm, uniformName)

        if (uniformLocation < 0) {
            error("Error: Could not find uniform $uniformName")
        }

        uniforms[uniformName] = uniformLocation
    }

    private fun setUniform(uniformName: String, value: Matrix4f) {
        try  {
            val stack = MemoryStack.stackPush()
            val fb = stack.mallocFloat(16)
            value.get(fb)
            uniforms[uniformName]?.let { glUniformMatrix4fv(it, false, fb) }
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

    fun createPointLightUniform(uniformName: String) {
        createUniform("$uniformName.colour")
        createUniform("$uniformName.position")
        createUniform("$uniformName.intensity")
        createUniform("$uniformName.att.constant")
        createUniform("$uniformName.att.linear")
        createUniform("$uniformName.att.exponent")
    }

    fun createMaterialUniform(uniformName: String) {
        createUniform("$uniformName.ambient")
        createUniform("$uniformName.diffuse")
        createUniform("$uniformName.specular")
        createUniform("$uniformName.hasTexture")
        createUniform("$uniformName.reflectance")
    }

    fun setUniform(uniformName: String, pointLight: PointLight) {
        setUniform("$uniformName.colour", pointLight.colour)
        setUniform("$uniformName.position", pointLight.position)
        setUniform("$uniformName.intensity", pointLight.intensity)
        val att: PointLight.Attenuation = pointLight.attenuation
        setUniform("$uniformName.att.constant", att.constant)
        setUniform("$uniformName.att.linear", att.linear)
        setUniform("$uniformName.att.exponent", att.exponent)
    }

    fun setUniform(uniformName: String, material: Material) {
        setUniform("$uniformName.ambient", material.ambient)
        setUniform("$uniformName.diffuse", material.diffuse)
        setUniform("$uniformName.specular", material.specular)
        setUniform("$uniformName.hasTexture", if (material.hasTexture) 1 else 0)
        setUniform("$uniformName.reflectance", material.reflectance)
    }


    fun setupLight() {
        createPointLightUniform("pointLight")
        createMaterialUniform("material")
    }

    fun useLight() {
        val viewMatrix = camera.getTransformation().mul(transform.getTransformation())

        val pointLight = PointLight()
        val lightPos = pointLight.position
        val aux = Vector4f(lightPos, 1f)
        aux.mul(viewMatrix)

        lightPos.x = aux.x
        lightPos.y = aux.y
        lightPos.z = aux.z

        setUniform("pointLight", pointLight)
    }
}