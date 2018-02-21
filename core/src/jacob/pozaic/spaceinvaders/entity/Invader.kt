package jacob.pozaic.spaceinvaders.entity

import com.badlogic.gdx.graphics.g2d.TextureRegion
import jacob.pozaic.spaceinvaders.ai.Pos

class Invader(
        tex: TextureRegion,
        posX: Float,
        posY: Float,
        scaleWidth: Float,
        scaleHeight: Float,
        val type: InvaderType): Entity(tex, posX, posY, scaleWidth, scaleHeight) {
    var current_texture: Int = 0

    var move_group_offset = Pos(0F, 0F)

    var last_step_time = 0L

    //TODO: in act here check if they reach the ground
}