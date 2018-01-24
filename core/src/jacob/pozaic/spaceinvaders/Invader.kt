package jacob.pozaic.spaceinvaders

import com.badlogic.gdx.graphics.Texture

/**
 * Created by Jacob on 1/24/2018.
 */
class Invader(
        private val texture: Texture,
        private val width: Float = 128f,
        private val height: Float = 128f,
        private val posX: Float = 0f,
        private val posY: Float = 0f,
        private val type: InvaderType = InvaderType.FIGHTER): Entity(texture, width, height, posX, posY) {
    init {

    }
}