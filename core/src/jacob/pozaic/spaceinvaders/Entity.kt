package jacob.pozaic.spaceinvaders

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle

/**
 * Created by Jacob on 1/22/2018.
 */

open class Entity (
        private val texture: Texture,
        private val width: Float = 128f,
        private val height: Float = 128f,
        private val posX: Float = 0f,
        private val posY: Float = 0f) {
    private val pos = Rectangle()

    init {
        pos.width = width
        pos.height = height
        pos.x = posX
        pos.y = posY
    }

    fun GetTexture(): Texture {
        return texture
    }

    fun GetRectangle(): Rectangle {
        return pos
    }
}