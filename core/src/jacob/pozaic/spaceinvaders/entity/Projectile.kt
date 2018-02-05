package jacob.pozaic.spaceinvaders.entity

import jacob.pozaic.spaceinvaders.resources.Sprites

class Projectile(
        size: Float = 0F,
        posX: Float = 0F,
        posY: Float = 0F,
        val type: Sprites): Entity(size, size, posX, posY) {

    fun step(distance: Float) {
        getRectangle().setY(getY() + distance)
    }

    fun distanceTo(target: Entity): Float {
        val center = getCenter()
        val other = target.getCenter()
        return Math.sqrt(Math.pow(center.x - other.x, 2.0) + Math.pow(center.y - other.y, 2.0)).toFloat()
    }
}