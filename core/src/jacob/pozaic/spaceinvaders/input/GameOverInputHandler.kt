package jacob.pozaic.spaceinvaders.input

import com.badlogic.gdx.InputAdapter
import jacob.pozaic.spaceinvaders.game.SpaceInvaders

class GameOverInputHandler(private val game: SpaceInvaders): InputAdapter() {
    override fun touchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        return false
    }
}