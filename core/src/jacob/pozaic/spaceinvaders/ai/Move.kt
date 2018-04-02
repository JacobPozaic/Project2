package jacob.pozaic.spaceinvaders.ai

/**
 * A data class that stores relevant parameters to a movement.
 *
 * pos - The position the Entity was moved to
 * remainder - The extra distance that could have been covered since the last move, but wasn't as the end of the movement was reached
 *             This is used primarily for continious movement so that transitions between movements does not desyncronize group positions
 * success - If the movement was successfully made
 * reached_target - If the movement has completed
 */
data class MoveResult(val pos: Pos, val remainder: Float, val success: Boolean, val reached_target: Boolean)

/**
 * A abstract implementation of a movement, to be extended upon
 */
abstract class Move {
    // The start and end positions of the movement
    internal var start = Pos(0F, 0F)
    internal var end = Pos(0F, 0F)

    // How remainders should be handled //TODO: currently not used
    internal var remainder_handling = Remainder.IGNORE

    // The type of movement that should be used, IE: STATIONARY, CONTINIOUS, STEP_DISTANCE
    private var step_type: StepType? = null

    // Parameters for moving
    protected var step_freq: Float = 0F
    protected var step_dist: Float = 0F
    protected var step_speed: Float = 0F

    // The acceptable range for reaching a destination
    protected var acceptable_range = 0F

    // Sets the start and end positions of the movement
    fun start(start: Pos){
        this.start = start
    }

    fun end(end: Pos) {
        this.end = end
    }

    fun acceptableRange(range: Float) {
        this.acceptable_range = range
    }

    // Sets the type of movement that will be used
    /**
     * Sets the movement to stationary, preventing the group from moving at all
     */
    fun setStationary(seconds_to_wait: Float){
        step_type = StepType.STATIONARY
        this.step_freq = seconds_to_wait * 1000
    }

    /**
     * Sets the movement to continious, moving every frame by pixels-per-second
     */
    fun setContinious(pixels_per_second: Float) {
        step_type = StepType.CONTINIOUS
        this.step_speed = pixels_per_second
    }

    /**
     * Sets the movement to step by distance, moving a set distance every x amount of time
     */
    fun setStepByDistance(seconds_per_step: Float, pixels_per_step: Float) {
        step_type = StepType.STEP_DISTANCE
        this.step_freq = seconds_per_step * 1000
        this.step_dist = pixels_per_step
    }

    /**
     * Calculates the next movement to be made, returning a MoveResult with the destination position
     */
    fun nextPosition(current_pos: Pos, group_center: Pos, delta: Float, last_step_time: Long): MoveResult {
        return when(step_type){
            StepType.STATIONARY -> stationary(current_pos, group_center, delta)
            StepType.CONTINIOUS -> continious(current_pos, group_center, delta)
            StepType.STEP_DISTANCE -> stepDistance(current_pos, group_center, last_step_time)
            else -> MoveResult(current_pos, 0F, false, false)
        }
    }

    // How each type of movement is handled
    /**
     * Overridable function for how to handle stationary movement
     */
    protected open fun stationary(current_pos: Pos, group_center: Pos, delta: Float): MoveResult {
        return MoveResult(current_pos, 0F, true, false)
    }

    /**
     * Overridable function for how to handle continious movement
     */
    protected open fun continious(current_pos: Pos, group_center: Pos, delta: Float): MoveResult{
        return MoveResult(current_pos, 0F, true, false)
    }

    /**
     * Overridable function for how to handle step by distance movement
     */
    protected open fun stepDistance(current_pos: Pos, group_center: Pos, last_step_time: Long): MoveResult{
        return MoveResult(current_pos, 0F, true, false)
    }

    protected fun withinRange(point: Pos, dest: Pos, range: Float): Boolean {
        return point.distanceTo(dest) <= range
    }

    // Sets parameters for movement (only use these if the movement pattern needs to change after creation)
    /**
     * Updates the frequency that each step should take place at
     */
    fun setStepFrequency(seconds: Float){
        step_freq = seconds * 1000
    }

    fun getStepFrequency() = step_freq / 1000F

    /**
     * Updates the distance that should be traveled each frame in continious movement
     */
    fun setStepDistance(pixels: Float){
        step_dist = pixels
    }

    fun getStepDistance() = step_dist

    /**
     * Updates the distance per second traveled in step by distance movements
     */
    fun setStepSpeed(pixels_per_second: Float){
        step_speed = pixels_per_second
    }

    fun getStepSpeed() = step_speed
}