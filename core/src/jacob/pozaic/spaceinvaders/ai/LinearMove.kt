package jacob.pozaic.spaceinvaders.ai

import com.badlogic.gdx.utils.TimeUtils

/**
 * A movement implementation that travels directly from point A to point B
 */
class LinearMove: Move() {
    // Set a continious movement pattern (no delay on steps)
    override fun continious(current_pos: Pos, group_offset: Pos, delta: Float): MoveResult {
        return current_pos.moveToward(end.sub(group_offset), step_speed * delta)
    }

    // Set a step by distance movement pattern (step x distance in pixels every time y time has passed since the last step)
    override fun stepDistance(current_pos: Pos, group_offset: Pos, last_step_time: Long): MoveResult {
        // Moving by pixels per step
        if(TimeUtils.timeSinceMillis(last_step_time) >= step_freq)
            return current_pos.moveToward(end.sub(group_offset), step_dist)
        return MoveResult(current_pos, 0F, false, false)
    }
}