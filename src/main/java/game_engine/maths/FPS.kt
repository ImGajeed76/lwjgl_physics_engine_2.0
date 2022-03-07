package game_engine.maths

class FPS {
    private var FPS: Float = 0f

    private var frames1 = 0F
    private var fpsTimer1: Long = System.currentTimeMillis()
    private var fps1: Float = 0f

    private var frames2 = 0F
    private var fpsTimer2: Long = System.currentTimeMillis()
    private var fps2: Float = 0f

    val fps: Float
        get() {
            return FPS
        }

    fun next() {
        frames1++
        if (System.currentTimeMillis() > fpsTimer1 + 1000) {
            fpsTimer1 = System.currentTimeMillis()
            fps1 = frames1
            frames1 = 0F
        }

        frames2++
        if (System.currentTimeMillis() > fpsTimer2 + 10) {
            fpsTimer2 = System.currentTimeMillis()
            fps2 = frames2 * 100
            frames2 = 0F
        }

        FPS = if (fps1 != 0f) {
            (fps1 + fps2) / 2
        } else {
            fps2
        }
    }
}