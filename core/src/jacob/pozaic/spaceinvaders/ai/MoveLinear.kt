package jacob.pozaic.spaceinvaders.ai

import com.badlogic.gdx.utils.TimeUtils

class MoveLinear(
        private val start_pos: Pos,
        private val end_pos: Pos,
        private val step_time: Long,
        private val step_distance: Float): Move() {
    private var last_step: Long = 0L

    override fun getStart() = start_pos

    override fun getEnd() = end_pos

    override fun nextPosition(current_pos: Pos, delta_time: Long): MoveResult {
        if(last_step == 0L) {
            last_step = TimeUtils.millis()
            return MoveResult(start_pos, 0F)
        }
        val temp = TimeUtils.timeSinceMillis(last_step)
        if(temp >= step_time)
            return current_pos.moveToward(end_pos, step_distance * temp)//TODO: going to need tweaking
        return MoveResult(current_pos, 0F)
    }
}