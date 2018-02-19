package jacob.pozaic.spaceinvaders.ai

class MovePattern {
    private val movement = ArrayList<Move>()

    fun addMovement(movement: Move) {
        this.movement.add(movement)
    }

    fun move(location: Pos): Pos {
        return movement[0].nextPosition(location).pos
    }

    fun getStartPos() = if(movement.size > 0) movement[0].getStart() else Pos(0F, 0F)

    fun getEndPos() = if(movement.size > 0) movement[movement.size - 1].getEnd() else Pos(0F, 0F)
}