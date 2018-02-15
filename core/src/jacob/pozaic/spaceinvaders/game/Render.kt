package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import jacob.pozaic.spaceinvaders.resources.Sprites

internal fun renderLoop() {
    // Render frame
    Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    // Determine what render path to use
    when(game_state) {
        GameState.SHOW_GAME_START   -> startMenuRender()
        GameState.SHOW_GAME_OPTIONS -> optionMenuRender()
        GameState.SHOW_GAME_PLAY    -> gamePlayRender()
        GameState.SHOW_GAME_PAUSE   -> gamePauseRender()
        GameState.SHOW_GAME_OVER    -> gameOverRender()
    }
}

private fun startMenuRender() {
    stg_start.viewport.apply()
    stg_start.draw()
}

private fun optionMenuRender() {
    stg_options.viewport.apply()
    stg_options.draw()
}

private fun gamePlayRender() {
    stg_game.viewport.apply()
    stg_game.draw()

    batch.begin()

    // Draw the background
    batch.draw(RL.getTexture(Sprites.BACKGROUND), 0F, 0F, screen_width, screen_height)

    // Draw the invaders
    EM!!.getAllInvaders().forEach{ it.draw(batch) }

    // Draw the player
    EM!!.getPlayer().draw(batch)

    // Draw projectiles
    EM!!.getProjectiles().forEach { it.draw(batch) }

    batch.end()
}

private fun gamePauseRender() {
    //TODO:
}

private fun gameOverRender() {
    //TODO:
}