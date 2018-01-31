package jacob.pozaic.spaceinvaders.entity

class Invader(
        size: Float = 0F,
        posX: Float = 0F,
        posY: Float = 0F,
        val type: InvaderType = InvaderType.FIGHTER,
        private val step_distance: Float = 0F,
        private val drop_distance: Float = 0F):
            Entity(size, size, posX, posY) {
    var current_texture: Int = 0

    fun step(move_right: Boolean): Float {
        return getRectangle().setX(getRectangle().getX()
                + if(move_right) step_distance else -step_distance).getX()
    }

    fun drop(): Float {
        return getRectangle().setY(getRectangle().getY() - drop_distance).getY()
    }
}