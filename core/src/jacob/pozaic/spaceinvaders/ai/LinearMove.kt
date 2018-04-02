package jacob.pozaic.spaceinvaders.ai

import com.badlogic.gdx.utils.TimeUtils

/**
 * A movement implementation that travels directly from point A to point B
 */
class LinearMove: Move() {
    private var wait_start_time = -1L

    // Set a duration to stay
    override fun stationary(current_pos: Pos, group_center: Pos, delta: Float): MoveResult {
        if(wait_start_time == -1L) wait_start_time = TimeUtils.millis()
        if(TimeUtils.timeSinceMillis(wait_start_time) >= step_freq)
            return MoveResult(current_pos, 0F, true, true)
        return MoveResult(current_pos, 0F, true, false)
    }

    // Set a continious movement pattern (no delay on steps)
    override fun continious(current_pos: Pos, group_center: Pos, delta: Float): MoveResult {
        if(withinRange(group_center, end, acceptable_range))
            return MoveResult(current_pos, 0F, true, true)
        return current_pos.moveToward(end, end.sub(group_center).norm(), step_speed * delta)
    }

    // Set a step by distance movement pattern (step x distance in pixels every time y time has passed since the last step)
    override fun stepDistance(current_pos: Pos, group_center: Pos, last_step_time: Long): MoveResult {
        if(withinRange(group_center, end, acceptable_range))
            return MoveResult(current_pos, 0F, true, true)

        // Moving by pixels per step
        if(TimeUtils.timeSinceMillis(last_step_time) >= step_freq)
            return current_pos.moveToward(end, end.sub(group_center).norm(), step_dist)
        return MoveResult(current_pos, 0F, false, false)
    }
}