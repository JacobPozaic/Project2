package jacob.pozaic.spaceinvaders.entity

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.TimeUtils
import jacob.pozaic.spaceinvaders.ai.Pos
import jacob.pozaic.spaceinvaders.game.EntityParams
import jacob.pozaic.spaceinvaders.game.EntityType
import jacob.pozaic.spaceinvaders.game.game

abstract class Entity (
        val type: EntityType,
        posX: Float,
        posY: Float,
        scaleWidth: Float,
        scaleHeight: Float): Actor() {

    private var sprite: Sprite? = null

    private var current_texture: Int = 0
    private var last_texture_swap = 0L

    init {
        sprite = Sprite(getParameters().textures[0])
        sprite!!.setScale(scaleWidth, scaleHeight)
        sprite!!.setOriginCenter()
        this.setScale(scaleWidth, scaleHeight)
        setPos(posX, posY)
        this.setOrigin(0F, 0F)
    }

    fun setPos(pos: Pos) {
        setPos(pos.x, pos.y)
    }

    fun setPos(x: Float, y: Float) {
        sprite!!.setPosition(x - (sprite!!.width / 2), y - (sprite!!.height / 2))
        setBounds(sprite!!.x, sprite!!.y, sprite!!.width, sprite!!.height)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val texture_cycle_delay = getParameters().texture_cycle_delay
        if(texture_cycle_delay != 0L) {
            if(TimeUtils.timeSinceMillis(last_texture_swap) >= texture_cycle_delay){
                last_texture_swap = TimeUtils.millis()

                // Cycle the texture
                val tex = getParameters().textures
                val index = current_texture++ % tex.size
                sprite!!.setRegion(tex[index])
            }
        }

        sprite!!.draw(batch)
    }

    fun getCenter(): Pos = Pos(
            sprite!!.x + (sprite!!.width / 2),
            sprite!!.y + (sprite!!.height / 2)
    )

    fun getRect(): Rectangle = this.sprite!!.boundingRectangle

    fun getParameters(): EntityParams = game!!.entity(type)
}