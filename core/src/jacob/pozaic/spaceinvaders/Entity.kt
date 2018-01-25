package jacob.pozaic.spaceinvaders

import com.badlogic.gdx.math.Rectangle

abstract class Entity (
        width: Float = 0F,
        height: Float = 0F,
        posX: Float = 0F,
        posY: Float = 0F) {
    private val pos = Rectangle()

    init {
        pos.width = width
        pos.height = height
        pos.x = posX
        pos.y = posY
    }

    fun getRectangle(): Rectangle {
        return pos
    }
}