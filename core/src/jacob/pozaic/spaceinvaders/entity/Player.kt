package jacob.pozaic.spaceinvaders.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureRegion
import jacob.pozaic.spaceinvaders.game.player_speed
import jacob.pozaic.spaceinvaders.game.screen
import jacob.pozaic.spaceinvaders.game.tilt_sensitivity

class Player(
        tex: TextureRegion,
        posX: Float,
        posY: Float,
        scaleWidth: Float,
        scaleHeight: Float): Entity(tex, posX, posY, scaleWidth, scaleHeight) {

    private var use_accelerometer = false

    override fun act(delta: Float) {
        super.act(delta)

        if(!use_accelerometer) {
            // Move the player
            val gyro_angle = Gdx.input.accelerometerY

            when{
                gyro_angle > tilt_sensitivity ->
                    if(getCenter().x < screen.right)
                        step(player_speed * delta)
                gyro_angle < -tilt_sensitivity ->
                    if(getCenter().x > screen.left)
                        step(-player_speed * delta)
            }
        }
    }

    fun step(distance: Float) {
        setPos(getCenter().x + distance, getCenter().y)
    }

    fun useAccelerometer(b: Boolean) {
        use_accelerometer = b
    }
}