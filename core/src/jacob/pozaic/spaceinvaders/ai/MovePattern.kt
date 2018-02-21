package jacob.pozaic.spaceinvaders.ai

import com.badlogic.gdx.utils.TimeUtils
import jacob.pozaic.spaceinvaders.entity.Invader

class MovePattern {
    // The list of movements to be done
    private val movement = ArrayList<Move>()
    // Invaders assigned to this movement pattern
    private val invaders = ArrayList<Invader>()

    private var minX = Float.MAX_VALUE
    private var maxX = Float.MIN_VALUE
    private var minY = Float.MAX_VALUE
    private var maxY = Float.MIN_VALUE

    fun addMovement(movement: Move) {
        this.movement.add(movement)
    }

    //TODO: have a way to update a movement pattern if needed (ex: when a column dies, move further that direction)
    // This is handled only for movement sharing a group

    fun move(delta: Float) {
        if(movement.isEmpty()) return

        var segment_complete = false
        invaders.forEach { invader ->
            val group_offset = invader.move_group_offset
            val pos = invader.getPos().sub(group_offset)
            var move_segment = movement[0]
            val result = move_segment.nextPosition(pos, delta, invader.last_step_time)
            if(result.success) {
                invader.last_step_time = TimeUtils.millis()
                if(result.remainder == 0F) {
                    invader.setPos(result.pos.add(group_offset))
                    return@forEach
                }
                segment_complete = true
                if(movement.size < 2) return@forEach
                move_segment = movement[1]
                when(move_segment.remainder_handling){
                    Remainder.IGNORE -> return
                    Remainder.PASS_BY_DISTANCE -> {
                        return // TODO: move by remainder
                    }
                    Remainder.PASS_BY_TIME -> {
                        return // TODO: move by remainder
                    }
                }
            }
        }

        // After a segment of movements is completed it can be removed
        if(segment_complete) movement.removeAt(0)
    }

    fun addInvaders(invaders: ArrayList<Invader>) {
        this.invaders.addAll(invaders)
        calculateGroup()
    }

    fun removeInvader(invader: Invader){
        this.invaders.remove(invader)
        calculateGroup()
        movement.forEach {
            it.updateMoveOffsets((maxX - minX) / 2,
                    (maxY - minY) / 2,
                    invader.getPos().sub(invader.move_group_offset))
        }
    }

    private fun calculateGroup() {
        this.invaders.forEach {
            val pos = it.getPos()
            if(pos.x < minX) minX = pos.x
            else if(pos.x > maxX) maxX = pos.x
            if(pos.y < minY) minY = pos.y
            else if(pos.y > maxY) maxY = pos.y
        }

        val center_of_move_group = Pos(minX + (maxX - minX) / 2, minY + (maxY - minY) / 2)
        this.invaders.forEach { it.move_group_offset = it.getPos().sub(center_of_move_group) }
    }
}