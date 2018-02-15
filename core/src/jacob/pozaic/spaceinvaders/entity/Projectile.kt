package jacob.pozaic.spaceinvaders.entity

import com.badlogic.gdx.graphics.g2d.TextureRegion
import jacob.pozaic.spaceinvaders.resources.ProjectileType

class Projectile(
        tex: TextureRegion,
        posX: Int,
        posY: Int,
        scaleWidth: Float,
        scaleHeight: Float,
        val type: ProjectileType): Entity(tex, posX, posY, tex.regionWidth, tex.regionHeight, scaleWidth, scaleHeight) {

    fun step(distance: Float) {
        y += distance
    }

    fun collidesWith(target: Entity): Boolean {
        val center = getCenter()
        val other = target.getCenter()
        return (Math.sqrt(Math.pow(center.x - other.x, 2.0) + Math.pow(center.y - other.y, 2.0))).toFloat() <= width
    }
}