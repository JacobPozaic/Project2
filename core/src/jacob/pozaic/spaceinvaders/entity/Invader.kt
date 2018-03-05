package jacob.pozaic.spaceinvaders.entity

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.TimeUtils
import jacob.pozaic.spaceinvaders.ai.MoveGroup
import jacob.pozaic.spaceinvaders.ai.Pos
import jacob.pozaic.spaceinvaders.game.RL

class Invader(
        tex: TextureRegion,
        posX: Float,
        posY: Float,
        scaleWidth: Float,
        scaleHeight: Float,
        val move_group: MoveGroup,
        val type: InvaderType,
        private val texture_cycle_delay: Long): Entity(tex, posX, posY, scaleWidth, scaleHeight) {

    private var current_texture: Int = 0
    private var last_texture_swap = 0L

    var last_step_time = 0L

    var move_group_offset = Pos(0F, 0F)

    override fun act(delta: Float) {
        super.act(delta)
        //TODO: check if they reach the ground
        if(TimeUtils.timeSinceMillis(last_texture_swap) >= texture_cycle_delay){
            last_texture_swap = TimeUtils.millis()

            // Cycle the texture
            sprite.setRegion(RL.getInvaderTexture(type, current_texture++))
        }
    }
}