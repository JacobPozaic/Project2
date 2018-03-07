package jacob.pozaic.spaceinvaders.entity

import jacob.pozaic.spaceinvaders.game.EntityType

class Projectile(
        type: EntityType,
        posX: Float,
        posY: Float,
        scale: Float): Entity(type, posX, posY, scale, scale) {

    fun step(distance: Float) {
        val pos = getCenter()
        setPos(pos.x, pos.y + distance)
    }

    fun collidesWith(target: Entity): Boolean {
        return this.getRect().overlaps(target.getRect())
    }
}