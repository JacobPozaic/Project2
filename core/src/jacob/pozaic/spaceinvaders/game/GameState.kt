package jacob.pozaic.spaceinvaders.game

/**
 * Keeps track of what stage the game is in to direct the logic and render loops
 */
enum class GameState {
    SHOW_GAME_START, SHOW_GAME_OPTIONS, SHOW_GAME_PLAY, SHOW_GAME_OVER
}