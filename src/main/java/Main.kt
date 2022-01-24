import game_engine.graphics.GameWindow
import game_engine.graphics.Mesh
import game_engine.graphics.Shader

val testMesh = Mesh()
val testVertexArray = floatArrayOf(
    -1F, -1F, 0F,
    0F, 1F, 0F,
    1F, -1F, 0F
)

val basicShader = Shader()

fun main() {
    val gameWindow = GameWindow(640, 480, "Physics Engine")

    testMesh.create(testVertexArray)
    basicShader.create("basic_vertex.glsl", "basic_fragment.glsl")

    while (gameWindow.windowOpen()) {
        update()
        gameWindow.update()
    }

    destroy()
    gameWindow.terminate()
}

fun destroy() {
    basicShader.destroy()
    testMesh.destroy()
}

fun update() {
    basicShader.useShader()
    testMesh.draw()
}