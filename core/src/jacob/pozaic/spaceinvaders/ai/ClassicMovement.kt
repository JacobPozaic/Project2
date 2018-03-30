package jacob.pozaic.spaceinvaders.ai

import com.badlogic.gdx.utils.TimeUtils
import jacob.pozaic.spaceinvaders.game.SpaceInvaders
import jacob.pozaic.spaceinvaders.game.screen

class ClassicMovement(game: SpaceInvaders) : MoveGroup(game) {
    private var step_delay = 0.5F * 1000F
    private var step_distance = 10F
    private var drop_distance = 20F

    private var moving_right = true

    private var segment_complete = false

    private var segment_move = false

    override fun move() {
        // A list of invaders belonging to this move group and currently on screen
        val invaders = getInvaders()

        // If the edge should be reached before starting the next movement
        if(invaders.isNotEmpty()) {
            // The invader closest to the edge that the group is travelling towards
            var edge_invader = invaders[0]

            // Don't bother calculating a bunch of stuff if it hasn't been long enough to move anyways
            if (TimeUtils.timeSinceMillis(edge_invader.last_step_time) < step_delay) return

            if(moving_right) {
                var maxX = invaders[0].getCenter().x
                invaders.forEach { invader ->
                    if(invader.getCenter().x > maxX) {
                        maxX = invader.getCenter().x
                        edge_invader = invader
                    }
                }

                val pos = edge_invader.getCenter()
                val test_move = pos.add(Pos(step_distance, 0F))
                if(test_move.x > screen.right - 10) segment_complete = true
            } else {
                var minX = invaders[0].getCenter().x
                invaders.forEach { invader ->
                    if(invader.getCenter().x < minX) {
                        minX = invader.getCenter().x
                        edge_invader = invader
                    }
                }

                val pos = edge_invader.getCenter()
                val test_move = pos.sub(Pos(step_distance, 0F))
                if(test_move.x < screen.left + 10) segment_complete = true
            }

            val last_step_time = TimeUtils.millis()

            var move_by = Pos(0F, 0F)
            if(segment_complete) {
                if(segment_move) {
                    moving_right = !moving_right
                    segment_move = false
                    segment_complete = false
                    move_by = move_by.sub(Pos(0F, drop_distance))
                } else {
                    segment_move = true
                    move_by = if(moving_right) move_by.add(Pos(step_distance, 0F)) else move_by.sub(Pos(step_distance, 0F))
                }
            } else {
                move_by = if(moving_right) move_by.add(Pos(step_distance, 0F)) else move_by.sub(Pos(step_distance, 0F))
            }

            invaders.forEach { invader ->
                invader.last_step_time = last_step_time
                val pos = invader.getCenter()
                invader.setPos(pos.add(move_by))
            }
        }
    }

    override fun decrementStepDelay() {
        step_delay -= 7F
    }
}