package jacob.pozaic.spaceinvaders.input

import com.badlogic.gdx.InputAdapter
import jacob.pozaic.spaceinvaders.game.SpaceInvaders

class GameInputHandler(private val game: SpaceInvaders): InputAdapter() {
    override fun touchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        game.getEntityManager().playerShoot()
        return true
    }
}