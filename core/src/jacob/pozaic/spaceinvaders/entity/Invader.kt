package jacob.pozaic.spaceinvaders.entity

import jacob.pozaic.spaceinvaders.ai.MoveGroup
import jacob.pozaic.spaceinvaders.game.EntityType
import jacob.pozaic.spaceinvaders.game.gameOver
import jacob.pozaic.spaceinvaders.game.invader_win_distance

class Invader(
        type: EntityType,
        posX: Float,
        posY: Float,
        scale: Float,
        val move_group: MoveGroup): Entity(type, posX, posY, scale, scale) {

    var can_touch_ground = true

    var last_step_time = 0L

    override fun act(delta: Float) {
        super.act(delta)

        if(can_touch_ground && getCenter().y <= invader_win_distance)
            gameOver()
    }
}