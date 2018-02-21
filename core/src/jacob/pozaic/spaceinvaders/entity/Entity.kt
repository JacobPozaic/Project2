package jacob.pozaic.spaceinvaders.entity

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.graphics.g2d.Batch
import jacob.pozaic.spaceinvaders.ai.Pos

abstract class Entity (
        tex: TextureRegion,
        posX: Float,
        posY: Float,
        scaleWidth: Float,
        scaleHeight: Float): Actor() {

    private val sprite = Sprite(tex)

    init {
        sprite.setScale(scaleWidth, scaleHeight)
        sprite.setOriginCenter()
        setPos(posX, posY)
    }

    fun setPos(pos: Pos) {
        setPos(pos.x, pos.y)
    }

    fun setPos(x: Float, y: Float) {
        sprite.setPosition(x, y)
        setBounds(sprite.x, sprite.y, sprite.width, sprite.height)
    }

    override fun getX() = sprite.x

    override fun getY() = sprite.y

    fun getPos() = Pos(sprite.x, sprite.y)

    override fun draw(batch: Batch?, parentAlpha: Float) {
        sprite.draw(batch)
    }

    fun getCenter(): Pos = Pos(
            sprite.x + (sprite.width / 2),
            sprite.y + (sprite.height / 2)
    )
}