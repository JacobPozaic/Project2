package jacob.pozaic.spaceinvaders.entity

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.TimeUtils
import jacob.pozaic.spaceinvaders.ai.Pos
import jacob.pozaic.spaceinvaders.game.EntityParams
import jacob.pozaic.spaceinvaders.game.EntityType
import jacob.pozaic.spaceinvaders.game.game

abstract class Entity (
        val type: EntityType,
        private var posX: Float,
        private var posY: Float,
        scaleWidth: Float,
        scaleHeight: Float): Actor() {

    private var sprite: Sprite? = null

    protected var current_texture: Int = 0
    private var last_texture_swap = 0L

    init {
        sprite = Sprite(getParameters().textures[0])
        sprite!!.setScale(scaleWidth, scaleHeight)
        this.setScale(scaleWidth, scaleHeight)
        this.setPos(posX, posY)
        sprite!!.setOriginCenter()
        this.setOrigin(sprite!!.originX, sprite!!.originY)
    }

    open fun setPos(pos: Pos) {
        setPos(pos.x, pos.y)
    }

    open fun setPos(x: Float, y: Float) {
        posX = x
        posY = y
        sprite!!.setCenter(x, y)
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

    fun getCenter(): Pos = Pos(posX, posY)

    fun getRect(): Rectangle = this.sprite!!.boundingRectangle

    fun getParameters(): EntityParams = game!!.entity(type)
}