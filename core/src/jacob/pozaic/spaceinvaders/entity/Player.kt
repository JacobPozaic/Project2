package jacob.pozaic.spaceinvaders.entity

import com.badlogic.gdx.Gdx
import jacob.pozaic.spaceinvaders.game.*

class Player(
        posX: Float,
        posY: Float,
        scale: Float): Entity(EntityType.PLAYER, posX, posY, scale, scale) {

    override fun act(delta: Float) {
        super.act(delta)

        if(game!!.useAccelerometer()) {
            // Move the player
            val gyro_angle = Gdx.input.accelerometerY
            val player_speed = getParameters().move_Speed * delta

            when{
                gyro_angle >  tilt_sensitivity -> if(getCenter().x < screen.right) step( player_speed)
                gyro_angle < -tilt_sensitivity -> if(getCenter().x > screen.left)  step(-player_speed)
            }
        }
    }

    fun step(distance: Float) {
        setPos(getCenter().x + distance, getCenter().y)
    }
}