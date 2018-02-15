package jacob.pozaic.spaceinvaders.entity

import com.badlogic.gdx.graphics.g2d.TextureRegion

class Invader(
        tex: TextureRegion,
        posX: Int,
        posY: Int,
        scaleWidth: Float,
        scaleHeight: Float,
        val type: InvaderType): Entity(tex, posX, posY, tex.regionWidth, tex.regionHeight, scaleWidth, scaleHeight) {
    var current_texture: Int = 0

    fun step(move_right: Boolean) {
        x = (x + if(move_right) getStepDistance(type)
                 else -getStepDistance(type))
    }

    fun drop() {
        y -= getDropDistance(type)
    }
}