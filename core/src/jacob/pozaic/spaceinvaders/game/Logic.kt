package jacob.pozaic.spaceinvaders.game

internal fun logicLoop() {
    // Determine what logic path to use
    when(game_state) {
        GameState.SHOW_GAME_START -> startMenuLogic()
        GameState.SHOW_GAME_PLAY  -> gamePlayLogic()
        GameState.SHOW_GAME_PAUSE -> gamePauseLogic()
        GameState.SHOW_GAME_OVER  -> gameOverLogic()
    }
}

private fun startMenuLogic() {
    // TODO:
}

private fun gamePlayLogic() {
    // Lose condition
    if (EM!!.invadersWin()) gameOver()

    // TODO: sounds

    // AI TODO: move this code to EntityManager
    EM!!.createWave()

    // Update locations
    EM!!.update()
}

private fun gamePauseLogic() {
    //TODO:
}

private fun gameOverLogic() {
    //TODO:
}