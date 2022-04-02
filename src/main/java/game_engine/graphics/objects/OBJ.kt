package game_engine.graphics.objects

import game_engine.loaders.Loader

class OBJ(var name: String, autoLoad: Boolean = false) {
    lateinit var obj: CustomObject

    init {
        if (autoLoad) {
            obj = CustomObject(Loader().loadOBJ(name))
        }
    }

    fun load() {
        obj = CustomObject(Loader().loadOBJ(name))
    }
}