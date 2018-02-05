package jacob.pozaic.spaceinvaders.input

import com.badlogic.gdx.InputAdapter
import jacob.pozaic.spaceinvaders.entity.EntityManager

class GameInputHandler(private val EM: EntityManager): InputAdapter() {
    override fun touchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        EM.playerShoot()
        return true
    }
}