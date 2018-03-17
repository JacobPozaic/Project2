package jacob.pozaic.spaceinvaders.entity

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.TimeUtils
import jacob.pozaic.spaceinvaders.ai.Pos
import jacob.pozaic.spaceinvaders.game.EntityType
import jacob.pozaic.spaceinvaders.game.rand

class Explode(
        score: Int,
        pos: Pos,
        scale: Float): Entity(EntityType.EXPLODE, pos.x, pos.y, scale, scale) {

    private var text: Label? = null

    private var time_created = 0L
    private val exist_time = getParameters().move_Speed

    init {
        time_created = TimeUtils.millis()
        val tex = getParameters().textures
        current_texture = rand.nextInt(tex.size)

        val style = Label.LabelStyle()
        style.font = BitmapFont()
        text = Label("$score", style)
        text!!.setAlignment(Align.center)
        text!!.setScale(scaleX)
        setPos(pos)
    }

    override fun setPos(pos: Pos) {
        super.setPos(pos)
        text!!.setPosition(pos.x - (text!!.width / 2), pos.y - (text!!.height / 2))
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        text!!.draw(batch, parentAlpha)
    }

    override fun act(delta: Float) {
        super.act(delta)

        if(TimeUtils.timeSinceMillis(time_created) >= exist_time) {
            remove()
        }
    }
}