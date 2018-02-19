package jacob.pozaic.spaceinvaders.entity

import com.badlogic.gdx.graphics.g2d.TextureRegion
import jacob.pozaic.spaceinvaders.ai.MovePattern

class Invader(
        tex: TextureRegion,
        posX: Float,
        posY: Float,
        scaleWidth: Float,
        scaleHeight: Float,
        val type: InvaderType,
        private val movement: MovePattern): Entity(tex, posX, posY, scaleWidth, scaleHeight) {
    var current_texture: Int = 0

    override fun act(delta: Float) {
        super.act(delta)

        setPos(movement.move(getPos()))

        //TODO: cycle texture
    }
}