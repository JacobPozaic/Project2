package jacob.pozaic.spaceinvaders.ai

import com.badlogic.gdx.utils.TimeUtils

class LinearMove: Move() {
    override fun continious(current_pos: Pos, delta: Float): MoveResult {
        return current_pos.moveToward(end, step_speed * delta)
    }

    override fun stepDistance(current_pos: Pos, last_step_time: Long): MoveResult {
        // Moving by pixels per step
        if(TimeUtils.timeSinceMillis(last_step_time) >= step_freq) {
            println("${current_pos.x}, ${current_pos.y} : ${end.x}, ${end.y}") //TODO: DEBUG
            return current_pos.moveToward(end, step_dist)
        }
        return MoveResult(current_pos, 0F, false)
    }
}