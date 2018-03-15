package jacob.pozaic.spaceinvaders.ai

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.TimeUtils
import jacob.pozaic.spaceinvaders.entity.Invader
import jacob.pozaic.spaceinvaders.game.SpaceInvaders
import jacob.pozaic.spaceinvaders.game.screen
import jacob.pozaic.spaceinvaders.game.texture_width

class MoveGroup(private val game: SpaceInvaders){
    // The list of movements to be done
    private val movement = ArrayList<Move>()

    private var touch_edge_to_continue = false

    private var minX = Float.MAX_VALUE
    private var maxX = Float.MIN_VALUE
    private var minY = Float.MAX_VALUE
    private var maxY = Float.MIN_VALUE

    private var group_width = maxX - minX
    private var group_height = maxY - minY
    private var group_center = Pos(minX + group_width / 2, minY + group_height / 2)

    fun move() {
        if(movement.isEmpty()) return

        calculateGroup()

        var segment_complete = false

        val invaders = getInvaders()
        val move_segment = movement[0]

        val delta = Gdx.graphics.deltaTime

        if(touch_edge_to_continue && invaders.isNotEmpty()) {
            var edge_invader = invaders[0]
            // Don't bother calculating a bunch of stuff if it hasn't been long enough to move anyways
            if(!move_segment.nextPosition(edge_invader.getCenter(), delta, edge_invader.last_step_time).success) return

            if(invaders[0].getCenter().x < move_segment.end.x) {
                // Moving right
                var maxX = invaders[0].getCenter().x
                invaders.forEach { invader ->
                    if(invader.getCenter().x > maxX) {
                        maxX = invader.getCenter().x
                        edge_invader = invader
                    }
                }

                val group_offset = edge_invader.getCenter().sub(group_center)
                val pos = edge_invader.getCenter().sub(group_offset)
                val test_move = move_segment.nextPosition(pos, delta, edge_invader.last_step_time)

                if(test_move.pos.add(group_offset).x >= screen.right) segment_complete = true
                else if(test_move.reached_target) move_segment.end(Pos(move_segment.end.x, move_segment.end.y))
            } else if(invaders[0].getCenter().x > move_segment.end.x) {
                // Moving left
                var minX = invaders[0].getCenter().x
                invaders.forEach { invader ->
                    if(invader.getCenter().x < minX) {
                        minX = invader.getCenter().x
                        edge_invader = invader
                    }
                }

                val group_offset = edge_invader.getCenter().sub(group_center)
                val pos = edge_invader.getCenter().sub(group_offset)
                val test_move = move_segment.nextPosition(pos, delta, edge_invader.last_step_time)

                if(test_move.pos.add(group_offset).x <= screen.left) segment_complete = true
                else if(test_move.reached_target) move_segment.end(Pos(move_segment.end.x, move_segment.end.y))
            }
        }

        val last_step_time = TimeUtils.millis()
        invaders.forEach { invader ->
            val group_offset = invader.getCenter().sub(group_center)
            val pos = invader.getCenter().sub(group_offset)
            val result = move_segment.nextPosition(pos, delta, invader.last_step_time)
            if(result.success) {
                invader.last_step_time = last_step_time
                if(result.remainder == 0F) {
                    invader.setPos(result.pos.add(group_offset))
                    return@forEach
                }
                if(result.reached_target) segment_complete = true
            }
        }

        /*if(movement.size <= 1) return@forEach // handling remainder in movement
        when(movement[1].remainder_handling){
            Remainder.IGNORE -> return@forEach
            Remainder.PASS_BY_DISTANCE -> {
                return@forEach // TODO: move by remainder
            }
            Remainder.PASS_BY_TIME -> {
                return@forEach // TODO: move by remainder
            }
        }*/

        calculateGroup()

        // After a segment of movements is completed it can be removed
        if(segment_complete) {
            movement.removeAt(0)
            if(movement.size > 0)
                movement[0].start(group_center)
            moveGroupToStart()
        }
    }

    fun addMovement(movement: Move) {
        this.movement.add(movement)
    }

    fun moveGroupToStart() {
        if(movement.isEmpty()) return
        calculateGroup()
        val distance_to_start = group_center.sub(movement[0].start)
        getInvaders().forEach { invader -> invader.setPos(invader.getCenter().sub(distance_to_start)) }
        calculateGroup()
    }

    fun setTouchEdgeToContinue(touch: Boolean) {
        touch_edge_to_continue = touch
    }

    private fun getInvaders(): List<Invader> = game.getInvaders().filter { invader -> invader.move_group == this }

    private fun calculateGroup() {
        minX = Float.MAX_VALUE
        minY = Float.MAX_VALUE
        maxX = Float.MIN_VALUE
        maxY = Float.MIN_VALUE

        game.getInvaders().forEach {
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

    fun getGroupWidth(): Float {
        calculateGroup()
        return group_width
    }

    fun getGroupHeight(): Float {
        calculateGroup()// TODO: moving should be done only from original center, cannot adjust end point, calculate messes with verticle movement
        //what if keep original rectangle, but continue moving in direction with calculated rect until wall is hit?
        // or on column/row destroyed update end position
        return group_height
    }
}