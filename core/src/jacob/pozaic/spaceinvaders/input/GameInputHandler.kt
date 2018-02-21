package jacob.pozaic.spaceinvaders.input

import com.badlogic.gdx.InputAdapter
import jacob.pozaic.spaceinvaders.game.playerShoot

class GameInputHandler: InputAdapter() {
    override fun touchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        playerShoot()
        return true
    }
}