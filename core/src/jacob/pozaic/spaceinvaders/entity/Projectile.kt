package jacob.pozaic.spaceinvaders.entity

import com.badlogic.gdx.graphics.g2d.TextureRegion
import jacob.pozaic.spaceinvaders.resources.ProjectileType

class Projectile(
        tex: TextureRegion,
        posX: Float,
        posY: Float,
        scaleWidth: Float,
        scaleHeight: Float,
        val type: ProjectileType): Entity(tex, posX, posY, scaleWidth, scaleHeight) {

    fun step(distance: Float) {
        val pos = getCenter()
        setPos(pos.x, pos.y + distance)
    }

    fun collidesWith(target: Entity): Boolean {
        val center = getCenter()
        val other = target.getCenter()
        return (Math.sqrt(Math.pow(center.x - other.x.toDouble(), 2.0) + Math.pow(center.y - other.y.toDouble(), 2.0))).toFloat() <= width
    }
}