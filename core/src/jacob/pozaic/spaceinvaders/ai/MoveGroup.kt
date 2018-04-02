package jacob.pozaic.spaceinvaders.ai

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.TimeUtils
import jacob.pozaic.spaceinvaders.entity.Invader
import jacob.pozaic.spaceinvaders.game.SpaceInvaders

/**
 * Handles moving groups of Entities at a time
 */
open class MoveGroup(private val game: SpaceInvaders){
    // The list of movements to be done
    private val movement = ArrayList<Move>()

    // The bounds of the group
    private var minX = Float.MAX_VALUE
    private var maxX = Float.MIN_VALUE
    private var minY = Float.MAX_VALUE
    private var maxY = Float.MIN_VALUE

    private var group_width = maxX - minX
    private var group_height = maxY - minY
    private var group_center = Pos(minX + group_width / 2, minY + group_height / 2)

    var decrement_duration_seconds = 0F
    var decrement_distance = 0F
    var decrement_pixels_per_seconds = 0F

    /**
     * Move the group
     */
    open fun move() {
        // if no movement components are left then don't move
        if(movement.isEmpty()) return

        // Has the current move component been completed
        var segment_complete = false

        // The current move component
        val move_segment = movement[0]

        // The time since the last frame, this is used here so that iterating through each invader doesn't desync positions over time
        val delta = Gdx.graphics.deltaTime

        calculateGroup()

        // Move each invader in the move group
        val last_step_time = TimeUtils.millis()
        getInvaders().forEach { invader ->
            val result = move_segment.nextPosition(invader.getCenter(), group_center, delta, invader.last_step_time)

            if(result.success) {
                invader.last_step_time = last_step_time
                invader.setPos(result.pos)
                if(result.reached_target) segment_complete = true
                if (result.remainder != 0F && movement.size > 1) {
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

        // After a segment of movements is completed it can be removed
        if(segment_complete) {
            movement.removeAt(0)
            moveGroupToStart()
            if(movement.size == 0)
                getInvaders().forEach{ invader -> game.removeInvader(invader) }
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
     * Gets a list of all invaders that belong to this move group
     */
    protected fun getInvaders(): List<Invader> = game.getInvaders().filter { invader -> invader.move_group == this }

    /**
     * Calculates the bounds of the region occupied by invaders belonging to this group
     */
    fun calculateGroup() {
        minX = Float.MAX_VALUE
        minY = Float.MAX_VALUE
        maxX = Float.MIN_VALUE
        maxY = Float.MIN_VALUE

        val invaders = getInvaders()
        if(invaders.size == 1) {
            val center = invaders[0].getCenter()
            minX = center.x
            maxX = center.x
            minY = center.y
            maxY = center.y
            group_width = 1F
            group_height = 1F
            group_center = center
        } else {
            invaders.forEach {
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

    fun getGroupWidth() = group_width

    fun getGroupHeight() = group_height

    open fun decrementStepDelay() {
        if(movement.size > 0)
            movement[0].setStepFrequency(movement[0].getStepFrequency() - decrement_duration_seconds)
    }

    open fun decrementStepDistance() {
        if(movement.size > 0)
            movement[0].setStepDistance(movement[0].getStepDistance() - decrement_distance)
    }

    open fun decrementStepSpeed() {
        if(movement.size > 0)
            movement[0].setStepSpeed(movement[0].getStepSpeed() - decrement_pixels_per_seconds)
    }
}