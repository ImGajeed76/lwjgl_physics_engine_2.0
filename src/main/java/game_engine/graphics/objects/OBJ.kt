package game_engine.graphics.objects

import game_engine.loaders.Loader

class OBJ(var name: String, var texture: String = "default.png", autoLoad: Boolean = false) {
    lateinit var obj: CustomObject

    init {
        if (autoLoad) {
            obj = CustomObject(Loader().loadOBJ(name))
            obj.model.textureId = Loader().loadTexture(texture)
        }
    }

    fun load() {
        obj = CustomObject(Loader().loadOBJ(name))
        obj.model.textureId = Loader().loadTexture(texture)
    }

    fun copy(): GameObject {
        return CustomObject(obj.model)
    }
}