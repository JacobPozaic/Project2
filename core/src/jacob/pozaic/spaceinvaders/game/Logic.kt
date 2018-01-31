package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.TimeUtils

class Logic (private val game: SpaceInvaders){
    private val EM = game.getEntityManager()

    fun logicLoop(game_state: GameState) {
        // Determine what logic path to use
        when(game_state) {
            GameState.SHOW_GAME_START -> startMenu()
            GameState.SHOW_GAME_PLAY -> gamePlay()
            GameState.SHOW_GAME_PAUSE -> gamePause()
            GameState.SHOW_GAME_OVER -> gameOver()
        }
    }

    private fun startMenu() {
        // TODO:
    }

    // The last time step was called
    private var last_enemy_step_time = 0L
    private var last_player_step_time = 0L
    // The time between each step
    private var enemy_step_delay = 600L //TODO: less enemies increases speed, also make this 1000L when not debugging, probably use a speed like player does
    private var player_step_delay = 15L  // 66.7 steps per second

    private fun gamePlay() {
        // Lose condition
        if (EM.invadersWin()) game.gameOver()

        // TODO: sounds

        if(TimeUtils.timeSinceMillis(last_player_step_time) >= player_step_delay) {
            last_player_step_time = TimeUtils.millis()
            EM.stepPlayer(Gdx.input.accelerometerY)
        }

        // AI TODO: move this code to EntityManager
        EM.createWave()

        // Step when it is appropriate to do so
        if (TimeUtils.timeSinceMillis(last_enemy_step_time) >= enemy_step_delay) {
            last_enemy_step_time = TimeUtils.millis()
            EM.stepEnemy()
        }
    }

    private fun gamePause() {
        //TODO:
    }

    private fun gameOver() {
        //TODO:
    }
}