package jacob.pozaic.spaceinvaders

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

private val batch = SpriteBatch()

class SpaceInvaders : ApplicationAdapter() {
    // Most Android devices have a 16:10 aspect ratio, so use that as a scaling reference
    private var screen_width: Float = 800F
    private var screen_height: Float = 480F

    // Assuming all textures are 128x128, downscaled 32x32 with no spacing will be 352 of 800 wide
    private var texture_scale:Float = 0.25F
    private var scale_ratio:Float = 0F

    private val camera = OrthographicCamera()

    private val RL = ResourceLoader()
    private val EM = EnemyManager(RL)

    private var player: Player? = null

    override fun create() {
        scale_ratio = Gdx.graphics.width.toFloat() / screen_width
        texture_scale *= scale_ratio

        screen_width = Gdx.graphics.width.toFloat()
        screen_height = Gdx.graphics.height.toFloat()

        EM.scale_ratio = scale_ratio
        EM.texture_scale = texture_scale
        EM.screen_height = screen_height

        // On game start
        RL.LoadGameTextures()
        camera.setToOrtho(false, screen_width, screen_height)
        //val stepsUntilDrop = 50
        //val stepSize = (screen_width  - (11 * scaled down 128x128 image width)) / 50
        player = Player()
        EM.createWave()
    }

    override fun render() {
        // Input: Gdx.input.xxx

        // AI
        if (EM.noInvaders()) {/*TODO: next wave*/}
        else if (EM.invadersWin()) {/*TODO: invaders win*/}
        EM.step()

        // Render frame
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()

        batch.projectionMatrix = camera.combined
        batch.begin()
        batch.draw(RL.getTexture(Sprites.BACKGROUND), 0F, 0F, screen_width, screen_height)
        drawPlayer()
        drawEnemies()
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        RL.DisposeGameTextures()
    }

    private fun drawPlayer() {
        if(player != null) drawEntity(RL.getTexture(Sprites.PLAYER), player as Entity)
    }

    private fun drawEnemies() {
        EM.getAllInvaders().forEach{ drawEntity(RL.getInvaderTextures(it.type)[it.current_texture], it as Entity) }
    }

    private fun drawEntity(texture: Texture, entity: Entity) {
        batch.draw(texture, entity.getRectangle().getX(), entity.getRectangle().getY(), texture.width * texture_scale, texture.height * texture_scale)
    }
}
