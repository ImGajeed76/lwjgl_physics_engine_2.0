package game_engine.graphics.objects

import game_engine.loaders.Loader

class OBJ(var name: String) {
    lateinit var obj: CustomObject

    fun load() {
        obj = CustomObject(Loader().loadOBJ(name))
    }
}