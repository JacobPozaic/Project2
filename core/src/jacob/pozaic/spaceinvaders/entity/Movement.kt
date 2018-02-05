package jacob.pozaic.spaceinvaders.entity

private data class MoveParameters(val step_distance: Float, val drop_distance: Float)

private val move_map = HashMap<InvaderType, MoveParameters>()

fun setMovement(invader_type: InvaderType, step_distance: Float, drop_distance: Float) {
    move_map[invader_type] = MoveParameters(step_distance, drop_distance)
}

fun getStepDistance(invader_type: InvaderType) = move_map[invader_type]!!.step_distance

fun getDropDistance(invader_type: InvaderType) = move_map[invader_type]!!.drop_distance
