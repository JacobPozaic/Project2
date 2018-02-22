package jacob.pozaic.spaceinvaders.ai

import com.badlogic.gdx.utils.TimeUtils
import jacob.pozaic.spaceinvaders.entity.Invader
import jacob.pozaic.spaceinvaders.game.screen

class MoveGroup {
    // The list of movements to be done
    private val movement = ArrayList<Move>()
    // Invaders assigned to this movement pattern
    private val invaders = ArrayList<Invader>()

    private var minX = Float.MAX_VALUE
    private var maxX = Float.MIN_VALUE
    private var minY = Float.MAX_VALUE
    private var maxY = Float.MIN_VALUE

    internal var group_width = maxX - minX
    internal var group_height = maxY - minY
    private var group_center = Pos(minX + group_width / 2, minY + group_height / 2)

    //TODO: have a way to update a movement pattern if needed (ex: when a column dies, move further that direction)
    // This is handled only for movement sharing a group

    fun move(delta: Float) {
        if(movement.isEmpty()) return

        calculateGroup()

        print("${group_center.x}, ${group_center.y} : ")
        println("${movement[0].end.x}, ${movement[0].end.y}")

        var segment_complete = false
        invaders.forEach { invader ->
            invader.move_group_offset = invader.getCenter().sub(group_center)
            val group_offset = invader.move_group_offset
            val pos = invader.getCenter().sub(group_offset)
            var move_segment = movement[0]
            val result = move_segment.nextPosition(pos, delta, invader.last_step_time)
            if(result.success) {
                invader.last_step_time = TimeUtils.millis()
                if(result.remainder == 0F) {
                    invader.setPos(result.pos.add(group_offset))
                    return@forEach
                }
                if(result.reached_target) segment_complete = true
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
        if(segment_complete) {
            movement.removeAt(0)
            moveGroupToStart()
        }
    }

    fun addMovement(movement: Move) {
        this.movement.add(movement)
    }

    fun addInvaders(invaders: ArrayList<Invader>) {
        this.invaders.addAll(invaders)
        calculateGroup()
    }

    fun removeInvader(invader: Invader){
        this.invaders.remove(invader)
        calculateGroup()
    }

    fun moveGroupToStart() {
        if(movement.isEmpty()) return
        calculateGroup()
        val distance_to_start = group_center.sub(movement[0].start)
        invaders.forEach { it.setPos(it.getCenter().sub(distance_to_start)) }
        calculateGroup()
    }

    private fun calculateGroup() {
        minX = Float.MAX_VALUE
        minY = Float.MAX_VALUE
        maxX = Float.MIN_VALUE
        maxY = Float.MIN_VALUE

        this.invaders.forEach {
            val pos = it.getCenter()
            if(pos.x < minX) minX = pos.x
            else if(pos.x > maxX) maxX = pos.x
            if(pos.y < minY) minY = pos.y
            else if(pos.y > maxY) maxY = pos.y
        }

        group_width = maxX - minX
        group_height = maxY - minY
        group_center = Pos(minX + group_width / 2, minY + group_height / 2)
    }
}