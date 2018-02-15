package jacob.pozaic.spaceinvaders.entity

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion

abstract class Entity (
        tex: TextureRegion,
        posX: Int,
        posY: Int,
        width: Int,
        height: Int,
        scaleWidth: Float,
        scaleHeight: Float): Sprite(tex, posX, posY, width, height) {

    init {
        setScale(scaleWidth, scaleHeight)
    }

    data class Position(val x: Double, val y: Double)

    fun getCenter(): Position = Position(
            x.toDouble() + (width / 2),
            y.toDouble() + (height / 2)
    )
}