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

    protected val sprite = Sprite(tex)

    init {
        sprite.setScale(scaleWidth, scaleHeight)
        sprite.setOriginCenter()
        setScale(scaleWidth, scaleHeight)
        setPos(posX, posY)
        setOrigin(0F, 0F)
    }

    fun setPos(pos: Pos) {
        setPos(pos.x, pos.y)
    }

    fun setPos(x: Float, y: Float) {
        sprite.setPosition(x - (sprite.width / 2), y - (sprite.height / 2))
        setBounds(sprite.x, sprite.y, sprite.width, sprite.height)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        sprite.draw(batch)
    }

    fun getCenter(): Pos = Pos(
            sprite.x + (sprite.width / 2),
            sprite.y + (sprite.height / 2)
    )
}