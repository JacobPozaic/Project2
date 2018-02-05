package jacob.pozaic.spaceinvaders.entity

import com.badlogic.gdx.math.Rectangle

abstract class Entity (
        width: Float = 0F,
        height: Float = 0F,
        posX: Float = 0F,
        posY: Float = 0F) {
    private val pos = Rectangle()

    data class Position(val x: Double, val y: Double)

    init {
        pos.width = width
        pos.height = height
        pos.x = posX
        pos.y = posY
    }

    fun getCenter(): Position = Position(
            pos.x.toDouble() + (pos.width / 2),
            pos.y.toDouble() + (pos.height / 2)
    )

    protected fun getRectangle(): Rectangle = pos

    fun getX() = pos.x

    fun getY() = pos.y
}