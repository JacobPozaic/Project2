package jacob.pozaic.spaceinvaders.ai

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.TimeUtils
import jacob.pozaic.spaceinvaders.entity.Invader
import jacob.pozaic.spaceinvaders.game.SpaceInvaders
import jacob.pozaic.spaceinvaders.game.screen

/**
 * Handles moving groups of Entities at a time
 */
class MoveGroup(private val game: SpaceInvaders){
    // The list of movements to be done
    private val movement = ArrayList<Move>()

    // Should the MoveGroup continue its current move pattern until it reaches an edge instead of the end point
    private var touch_edge_to_continue = false

    // The bounds of the group
    private var minX = Float.MAX_VALUE
    private var maxX = Float.MIN_VALUE
    private var minY = Float.MAX_VALUE
    private var maxY = Float.MIN_VALUE

    private var group_width = maxX - minX
    private var group_height = maxY - minY
    private var group_center = Pos(minX + group_width / 2, minY + group_height / 2)

    /**
     * Move the group
     */
    fun move() {
        // if no movement components are left then don't move
        if(movement.isEmpty()) return

        val group_center_old = group_center

        // Recalculate the group bounds as they are out of date from the last movement / not initially calculated
        calculateGroup()

        // Has the current move component been completed
        var segment_complete = false

        // A list of invaders belonging to this move group and currently on screen
        val invaders = getInvaders()

        // The current move component
        val move_segment = movement[0]

        // The time since the last frame, this is used here so that iterating through each invader doesn't desync positions over time
        val delta = Gdx.graphics.deltaTime

        // If the edge should be reached before starting the next movement
        if(touch_edge_to_continue && invaders.isNotEmpty()) {
            // The invader closest to the edge that the group is travelling towards
            var edge_invader = invaders[0]

            // Don't bother calculating a bunch of stuff if it hasn't been long enough to move anyways
            if(!move_segment.nextPosition(edge_invader.getCenter(), delta, edge_invader.last_step_time).success) return

            // Check if the group is moving to the right or to the left, and set the appropriate edge_invader
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

                if(test_move.pos.add(group_offset).x != edge_invader.getCenter().x) {
                    group_center = Pos(group_center.x, group_center_old.y)
                }
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

                if(test_move.pos.add(group_offset).x != edge_invader.getCenter().x) {
                    group_center = Pos(group_center.x, group_center_old.y)
                }
                if(test_move.pos.add(group_offset).x <= screen.left) segment_complete = true
                else if(test_move.reached_target) move_segment.end(Pos(move_segment.end.x, move_segment.end.y))
            }
        }

        // Move each invader in the move group
        val last_step_time = TimeUtils.millis()
        invaders.forEach { invader ->
            val group_offset = invader.getCenter().sub(group_center)
            val pos = invader.getCenter().sub(group_offset)
            val result = move_segment.nextPosition(pos, delta, invader.last_step_time)
            if(result.success) {
                invader.last_step_time = last_step_time
                if(result.reached_target) {
                    segment_complete = true
                }
                if (result.remainder == 0F) {
                    invader.setPos(result.pos.add(group_offset))
                    return@forEach
                }
                if (movement.size > 1) {
                    when (movement[1].remainder_handling) {
                        Remainder.IGNORE -> return@forEach
                        Remainder.PASS_BY_DISTANCE -> {
                            return@forEach // TODO: move by remainder
                        }
                        Remainder.PASS_BY_TIME -> {
                            return@forEach // TODO: move by remainder
                        }
                    }
                }
            }
        }

        // Recalculate the group after moving
        calculateGroup()

        // After a segment of movements is completed it can be removed
        if(segment_complete) {
            movement.removeAt(0)
            if(movement.size > 0)
                movement[0].start(group_center)
            moveGroupToStart()
        }
    }

    /**
     * Add a new movement component to this group
     */
    fun addMovement(movement: Move) {
        this.movement.add(movement)
    }

    /**
     * Move the group to the start position of the current move component and recalculate the group bounds
     */
    fun moveGroupToStart() {
        if(movement.isEmpty()) return
        calculateGroup()
        val distance_to_start = group_center.sub(movement[0].start)
        getInvaders().forEach { invader -> invader.setPos(invader.getCenter().sub(distance_to_start)) }
        calculateGroup()
    }

    /**
     * Self explanitory
     */
    fun setTouchEdgeToContinue(touch: Boolean) {
        touch_edge_to_continue = touch
    }

    /**
     * Gets a list of all invaders that belong to this move group
     */
    private fun getInvaders(): List<Invader> = game.getInvaders().filter { invader -> invader.move_group == this }

    /**
     * Calculates the bounds of the region occupied by invaders belonging to this group
     */
    private fun calculateGroup() {
        minX = Float.MAX_VALUE
        minY = Float.MAX_VALUE
        maxX = Float.MIN_VALUE
        maxY = Float.MIN_VALUE

        getInvaders().forEach {
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

    /**
     * Gets the width of the move group
     */
    fun getGroupWidth(): Float {
        calculateGroup()
        return group_width
    }

    /**
     * Gets the height of the move group
     */
    fun getGroupHeight(): Float {
        calculateGroup()
        return group_height
    }
}