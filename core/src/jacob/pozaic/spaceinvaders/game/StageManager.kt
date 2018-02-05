package jacob.pozaic.spaceinvaders.game

import jacob.pozaic.spaceinvaders.input.GameInputHandler
import jacob.pozaic.spaceinvaders.input.GameOverInputHandler
import jacob.pozaic.spaceinvaders.input.PauseMenuInputHandler
import jacob.pozaic.spaceinvaders.input.StartMenuInputHandler

internal fun startMenu() {
    // TODO: display a start menu and wait for input before starting the game
    // Set the input processors to use
    input_processors.clear()
    input_processors.addProcessor(StartMenuInputHandler())

    // Set the game state
    game_state = GameState.SHOW_GAME_START

    startGame() //TODO: move to StartMenuInputHandler
}

internal fun startGame() {
    // Set the input processors to use
    input_processors.clear()
    input_processors.addProcessor(GameInputHandler(EM!!))

    // Load textures
    RL.loadGameTextures()

    // Init the Entity Manager
    EM!!.init()

    // Set the game state
    game_state = GameState.SHOW_GAME_PLAY
}

internal fun pauseGame() {
    // TODO: pause the game and display the pause menu
    // Set the input processors to use
    input_processors.clear()
    input_processors.addProcessor(PauseMenuInputHandler())

    // Set the game state
    game_state = GameState.SHOW_GAME_PAUSE
}

internal fun gameOver() {
    // TODO: End the game, show the final score, etc.
    // Set the input processors to use
    input_processors.clear()
    input_processors.addProcessor(GameOverInputHandler())

    // Dispose of game textures
    RL.disposeGameTextures()

    // Set the game state
    game_state = GameState.SHOW_GAME_OVER
}