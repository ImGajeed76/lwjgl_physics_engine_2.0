package game_engine.graphics.objects

import game_engine.loaders.Loader

class OBJ(var name: String) {
    var obj: CustomObject = CustomObject(Loader().loadOBJ(name))

}