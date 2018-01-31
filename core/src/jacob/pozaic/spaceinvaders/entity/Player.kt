package jacob.pozaic.spaceinvaders.entity

class Player(
        size: Float = 0F,
        posX: Float = 0F,
        posY: Float = 0F): Entity(size, size, posX, posY) {

    fun getX(): Float {
        return getRectangle().getX()
    }

    fun step(gyro_angle: Float) {
        getRectangle().setX(getRectangle().getX() + gyro_angle)
    }
}