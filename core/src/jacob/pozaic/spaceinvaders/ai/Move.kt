package jacob.pozaic.spaceinvaders.ai

data class MoveResult(val pos: Pos, val remainder: Float, val success: Boolean)

abstract class Move {
    protected var start = Pos(0F, 0F)
    protected var end = Pos(0F, 0F)

    private var real_start = Pos(0F, 0F)
    private var real_end = Pos(0F, 0F)

    var remainder_handling = Remainder.IGNORE
    private var step_type: StepType? = null

    protected var step_freq: Float = 0F
    protected var step_dist: Float = 0F
    protected var step_speed: Float = 0F

    // Sets the start and end positions of the movement
    fun start(start: Pos){
        this.real_start = start
    }

    fun end(end: Pos) {
        this.real_end = end
    }

    // Sets the type of movement that will be used
    fun setStationary(){
        step_type = StepType.STATIONARY
    }

    fun setContinious(pixels_per_second: Float) {
        step_type = StepType.CONTINIOUS
        this.step_speed = pixels_per_second
    }

    fun setStepByDistance(seconds_per_step: Float, pixels_per_step: Float) {
        step_type = StepType.STEP_DISTANCE
        this.step_freq = seconds_per_step * 1000
        this.step_dist = pixels_per_step
    }

    fun updateMoveOffsets(group_width: Float, group_height: Float, current_pos: Pos){
        if(real_end.x < current_pos.x)
            end.x = real_end.x + group_width
        else if(real_end.x > current_pos.x)
            end.x = real_end.x - group_width
        if(real_end.y < current_pos.y)
            end.y = real_end.y + group_height
        else if(real_end.y > current_pos.y)
            end.y = real_end.y - group_height

        if(real_start.x < current_pos.x)
            start.x = real_start.x + group_width
        else if(real_start.x > current_pos.x)
            start.x = real_start.x - group_width
        if(real_start.y < current_pos.y)
            start.y = real_start.y + group_height
        else if(real_start.y > current_pos.y)
            start.y = real_start.y - group_height
    }

    // Calculates the next movement to be made
    fun nextPosition(current_pos: Pos, delta: Float, last_step_time: Long): MoveResult {
        return when(step_type){
            StepType.STATIONARY -> stationary(current_pos, delta)
            StepType.CONTINIOUS -> continious(current_pos, delta)
            StepType.STEP_DISTANCE -> stepDistance(current_pos, last_step_time)
            else -> MoveResult(current_pos, 0F, false)
        }
    }

    // How each type of movement is handled
    protected open fun stationary(current_pos: Pos, delta: Float): MoveResult {
        return MoveResult(current_pos, 0F, true)
    }

    protected open fun continious(current_pos: Pos, delta: Float): MoveResult{
        return MoveResult(current_pos, 0F, true)
    }

    protected open fun stepDistance(current_pos: Pos, last_step_time: Long): MoveResult{
        return MoveResult(current_pos, 0F, true)
    }

    // Sets parameters for movement (only use these if the movement pattern needs to change after creation)
    fun setStepFrequency(seconds: Float){
        step_freq = seconds * 1000
    }

    fun setStepDistance(pixels: Float){
        step_dist = pixels
    }

    fun setStepSpeed(pixels_per_second: Float){
        step_speed = pixels_per_second
    }
}