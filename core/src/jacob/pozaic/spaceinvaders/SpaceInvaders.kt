package jacob.pozaic.spaceinvaders

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 * Created by Jacob on 1/22/2018.
 */

private val batch = SpriteBatch()

class SpaceInvaders : ApplicationAdapter() {
    private val RL = ResourceLoader()

    private var screen_width: Float = 480f
    private var screen_height: Float = 800f
    private val camera = OrthographicCamera()
    //TODO: use screen_size to create a ratio

    private var player: Player? = null

    override fun create() {
        RL.LoadGameTextures()

        screen_width = Gdx.graphics.width.toFloat()
        screen_height = Gdx.graphics.height.toFloat()

        camera.setToOrtho(false, screen_width, screen_height)
        player = Player(RL.GetPlayer()!!)
    }

    override fun render() {
        // Input: Gdx.input.xxx
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()

        batch.projectionMatrix = camera.combined
        batch.begin()
        drawEntity(player)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        RL.DisposeGameTextures()
    }

    private fun drawEntity(entity: Entity?) {
        if(entity != null)
            batch.draw(entity.GetTexture(), entity.GetRectangle().getX(), entity.GetRectangle().getY())
    }
}
