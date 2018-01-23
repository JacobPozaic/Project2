package jacob.pozaic.spaceinvaders

import com.badlogic.gdx.graphics.Texture

/**
 * Created by Jacob on 1/22/2018.
 */

class Player(
        private val texture: Texture,
        private val width: Float = 128f,
        private val height: Float = 128f,
        private val posX: Float = 0f,
        private val posY: Float = 0f): Entity(texture, width, height, posX, posY) {
    //TODO: player specific stuff
}