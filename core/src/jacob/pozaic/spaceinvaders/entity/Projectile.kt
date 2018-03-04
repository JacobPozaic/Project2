package jacob.pozaic.spaceinvaders.entity

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.TimeUtils
import jacob.pozaic.spaceinvaders.game.RL
import jacob.pozaic.spaceinvaders.resources.ProjectileType

class Projectile(
        tex: TextureRegion,
        posX: Float,
        posY: Float,
        scaleWidth: Float,
        scaleHeight: Float,
        val type: ProjectileType,
        private val texture_cycle_delay: Long): Entity(tex, posX, posY, scaleWidth, scaleHeight) {

    fun step(distance: Float) {
        val pos = getCenter()
        setPos(pos.x, pos.y + distance)
    }

    fun collidesWith(target: Entity): Boolean {
        return this.getRect().overlaps(target.getRect())
    }

    private var current_texture: Int = 0
    private var last_texture_swap = 0L

    override fun act(delta: Float) {
        super.act(delta)
        if(TimeUtils.timeSinceMillis(last_texture_swap) >= texture_cycle_delay){
            last_texture_swap = TimeUtils.millis()

            // Cycle the texture
            sprite.setRegion(RL.getProjectileTex(type, current_texture++))
        }
    }
}