package jacob.pozaic.spaceinvaders.entity

import jacob.pozaic.spaceinvaders.ai.MoveGroup
import jacob.pozaic.spaceinvaders.ai.Pos
import jacob.pozaic.spaceinvaders.game.EntityType

class Invader(
        type: EntityType,
        posX: Float,
        posY: Float,
        scale: Float,
        val move_group: MoveGroup): Entity(type, posX, posY, scale, scale) {

    var last_step_time = 0L

    var move_group_offset = Pos(0F, 0F)

    override fun act(delta: Float) {
        super.act(delta)
        //TODO: check if they reach the ground
    }
}