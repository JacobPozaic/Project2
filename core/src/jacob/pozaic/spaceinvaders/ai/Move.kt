package jacob.pozaic.spaceinvaders.ai

data class MoveResult(val pos: Pos, val remainder: Float)

abstract class Move {
    abstract fun getStart(): Pos
    abstract fun getEnd(): Pos
    abstract fun nextPosition(current_pos: Pos, delta_time: Long): MoveResult
}