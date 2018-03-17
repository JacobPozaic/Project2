package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture

private val bkg = Texture("Background.png")

internal fun renderLoop() {
    // Render frame
    Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    // Determine what render path to use
    when(game_state) {
        GameState.SHOW_GAME_START   -> startMenuRender()
        GameState.SHOW_GAME_OPTIONS -> optionMenuRender()
        GameState.SHOW_GAME_PLAY    -> gamePlayRender()
        GameState.SHOW_GAME_OVER    -> gameOverRender()
    }
}

private fun startMenuRender() {
    stg_start.camera.update()
    stg_start.viewport.apply()
    stg_start.draw()
}

private fun optionMenuRender() {
    stg_options.camera.update()
    stg_options.viewport.apply()
    stg_options.draw()
}

private fun gamePlayRender() {
    batch.begin()

    // Draw the background
    batch.draw(bkg, 0F, 0F, screen.width, screen.height)

    batch.end()
    stg_game.camera.update()
    stg_game.viewport.apply()
    stg_game.draw()
}

private fun gameOverRender() {
    batch.begin()

    // Draw the background
    batch.draw(bkg, 0F, 0F, screen.width, screen.height)

    batch.end()
    stg_game_over.camera.update()
    stg_game_over.viewport.apply()
    stg_game_over.draw()
}