package game_engine.graphics.objects

import CAMERA
import GAMEWINDOW
import game_engine.graphics.Shader
import game_engine.graphics.lighting.Material
import game_engine.graphics.lighting.PointLight
import org.joml.Vector4f

class ObjectContainer {
    var newObjects = arrayListOf<GameObject>()

    var gameObjects = arrayListOf<GameObject>()
    var rootLightShader = Shader()
    var rootShader = Shader()

    fun initNewObjects(
        vertexShader: String, fragmentShader: String, vertexLightShader: String, fragmentLightShader: String
    ) {
        for (gameObject in gameObjects) {
            gameObject.createMesh()
            if (GAMEWINDOW.usesLight) {
                gameObject.createShader(vertexLightShader, fragmentLightShader)
            } else {
                gameObject.createShader(vertexShader, fragmentShader)
            }
        }
    }

    fun createShaders(
        vertexShader: String, fragmentShader: String, vertexLightShader: String, fragmentLightShader: String
    ) {
        if (!GAMEWINDOW.usesLight) {
            rootShader.create(vertexShader, fragmentShader)
        } else {
            rootLightShader.create(vertexLightShader, fragmentLightShader)
        }
    }

    fun initNewObjects(mat: Material = Material(Vector4f(1f), 0.1f)) {
        for (gameObject in newObjects) {
            gameObject.createMesh()
            if (GAMEWINDOW.usesLight) {
                gameObject.shader = rootLightShader
            } else {
                gameObject.shader = rootShader
            }

            gameObject.model.material = mat
        }

        gameObjects.addAll(newObjects)
        newObjects = arrayListOf()
    }

    fun createPointLight(pointLight: PointLight) {
        if (GAMEWINDOW.usesLight) {
            rootLightShader.createPointLightUniform(pointLight.name)
        }
    }

    fun addObject(gameObject: GameObject) {
        newObjects.add(gameObject)
    }

    fun addObjects(gameObjects: ArrayList<GameObject>) {
        for (gameObject in gameObjects) {
            addObject(gameObject)
        }
    }

    fun addObject(obj: OBJ) {
        newObjects.add(obj.obj)
    }

    fun removeObject(gameObject: GameObject) {
        gameObjects.remove(gameObject)
    }

    fun removeObject(index: Int) {
        gameObjects.removeAt(index)
    }

    fun destroy() {
        for (gameObject in gameObjects) {
            gameObject.destroy()
        }
    }

    fun draw(pointLights: ArrayList<PointLight> = arrayListOf()) {
        for (gameObject in gameObjects) {
            gameObject.useShader()
            gameObject.shader.setLight(GAMEWINDOW.directionalLight)
            gameObject.shader.setMaterial(gameObject.model)

            for (pointLight in pointLights) {
                gameObject.shader.setPointLight(pointLight.name, pointLight)
            }

            gameObject.setCamera(CAMERA)
            gameObject.draw()
        }
    }

    fun updatePhysics() {
        for (gameObject in gameObjects) {
            gameObject.updatePhysics(gameObjects)
        }
    }

    operator fun get(i: Int): GameObject {
        return gameObjects[i]
    }

    operator fun set(i: Int, value: GameObject) {
        gameObjects[i] = value
    }

    fun setMaterials(material: Material) {
        for (obj in gameObjects) {
            obj.model.material = material
        }
    }
}