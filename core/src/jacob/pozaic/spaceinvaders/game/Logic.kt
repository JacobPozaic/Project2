package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.Gdx

internal fun logicLoop() {
    // Determine what logic path to use
    when(game_state) {
        GameState.SHOW_GAME_PLAY  -> gamePlayLogic()
        GameState.SHOW_GAME_PAUSE -> gamePauseLogic()
        GameState.SHOW_GAME_OVER  -> gameOverLogic()
    }
}

private fun gamePlayLogic() {
    // Lose condition
    if (EM!!.invadersWin()) gameOver()

    // TODO: sounds

    // Update locations
    EM!!.update()

    stg_game.act(Gdx.graphics.deltaTime)
}

private fun gamePauseLogic() {
    //TODO:
}

private fun gameOverLogic() {
    //TODO:
}