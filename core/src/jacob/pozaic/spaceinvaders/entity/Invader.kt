package jacob.pozaic.spaceinvaders.entity

class Invader(
        size: Float = 0F,
        posX: Float = 0F,
        posY: Float = 0F,
        val type: InvaderType = InvaderType.BOMBER):
            Entity(size, size, posX, posY) {
    var current_texture: Int = 0

    fun step(move_right: Boolean):
            Float = getRectangle().setX(getRectangle().getX()
                + if(move_right) getStepDistance(type)
                else -getStepDistance(type)).getX()

    fun drop(): Float = getRectangle().setY(getRectangle().getY() - getDropDistance(type)).getY()
}