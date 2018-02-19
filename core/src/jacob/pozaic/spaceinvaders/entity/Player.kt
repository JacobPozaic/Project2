package jacob.pozaic.spaceinvaders.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureRegion

class Player(
        tex: TextureRegion,
        posX: Float,
        posY: Float,
        scaleWidth: Float,
        scaleHeight: Float): Entity(tex, posX, posY, scaleWidth, scaleHeight) {

    override fun act(delta: Float) {
        super.act(delta)

        // Move the player
        val gyro_angle = Gdx.input.accelerometerY

        when{
            gyro_angle > tilt_sensitivity ->
                if(getX() < screen_right_cutoff)
                    step(player_speed * delta)
            gyro_angle < -tilt_sensitivity ->
                if(getX() > screen_left_cutoff)
                    step(-player_speed * delta)
        }
    }

    fun step(gyro_angle: Float) {
        setPos(getX() + gyro_angle, getY())
    }
}