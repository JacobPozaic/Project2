package jacob.pozaic.spaceinvaders.entity

import com.badlogic.gdx.graphics.g2d.TextureRegion

class Player(
        tex: TextureRegion,
        posX: Int,
        posY: Int,
        scaleWidth: Float,
        scaleHeight: Float): Entity(tex, posX, posY, tex.regionWidth, tex.regionHeight, scaleWidth, scaleHeight) {

    fun step(gyro_angle: Float) {
        x += gyro_angle
    }
}