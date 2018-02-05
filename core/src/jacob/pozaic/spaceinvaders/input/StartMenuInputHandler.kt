package jacob.pozaic.spaceinvaders.input

import com.badlogic.gdx.InputAdapter

class StartMenuInputHandler: InputAdapter() {
    override fun touchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        return false
    }
}