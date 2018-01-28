package jacob.pozaic.spaceinvaders

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.TimeUtils

private val batch = SpriteBatch()

class SpaceInvaders : ApplicationAdapter() {
    // Most Android devices have a 16:10 aspect ratio, so use that as a scaling reference
    private var screen_width: Float = 800F
    private var screen_height: Float = 480F

    // Assuming all textures are 128x128, downscaled 32x32 to fit an 800x480 screen
    // the scale ratio will scale the sprites back up for larger screen resolutions
    private var texture_scale:Float = 0.25F
    private var scale_ratio_width:Float = 0F
    private var scale_ratio_height:Float = 0F

    private val camera = OrthographicCamera()

    private val RL = ResourceLoader()
    private var EM: EnemyManager? = null

    private var player: Player? = null

    private var game_state = GameState.SHOW_GAME_START

    override fun create() {
        scale_ratio_width = Gdx.graphics.width.toFloat() / screen_width
        scale_ratio_height = Gdx.graphics.height.toFloat() / screen_height
        texture_scale *= scale_ratio_width // Technically this wont scale things right height-wise, but it shouldn't be a huge issue

        screen_width = Gdx.graphics.width.toFloat()
        screen_height = Gdx.graphics.height.toFloat()

        EM = EnemyManager(RL, scale_ratio_width, scale_ratio_height, texture_scale, screen_width, screen_height)

        // Initialize the camera
        camera.setToOrtho(false, screen_width, screen_height)

        // Start the game
        startMenu()
    }

    private fun startMenu() {
        game_state = GameState.SHOW_GAME_START
        // TODO: display a start menu and wait for input before starting the game, move startGame() call to render loop
        startGame()
    }

    private fun startGame() {
        game_state = GameState.SHOW_GAME_PLAY
        RL.loadGameTextures()
        player = Player()
    }

    private fun pauseGame() {
        game_state = GameState.SHOW_GAME_PAUSE
        // TODO:
    }

    private fun gameOver() {
        game_state = GameState.SHOW_GAME_OVER
        // TODO:
    }

    private fun renderStartMenu() {
        // TODO:
    }

    // The last time step was called
    private var last_step_time = 0L
    // The time between each step
    private var step_time = 100L //TODO: less enemies increases speed, also make this 1000L when not debugging

    private fun renderGamePlay() {
        // TODO: Input: Gdx.input.xxx
        /* TODO:
        if(input = pause button) pauseGame()
        */

        // AI
        EM!!.createWave()

        // Step when it is appropriate to do so
        if (TimeUtils.timeSinceMillis(last_step_time) >= step_time) {
            last_step_time = TimeUtils.millis()
            EM!!.step()
        }

        //TODO: sounds

        // Lose condition
        if (EM!!.invadersWin()) gameOver()

        batch.begin()
        batch.draw(RL.getTexture(Sprites.BACKGROUND), 0F, 0F, screen_width, screen_height)
        drawPlayer()
        drawEnemies()
        batch.end()
    }

    private fun renderGamePause() {
        //TODO:
    }

    private fun renderGameOver() {
        RL.disposeGameTextures()
        //TODO:
    }

    override fun render() {
        // Render frame
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // Update camera
        camera.update()

        // Update the batch projection
        batch.projectionMatrix = camera.combined

        // Determine what render path to use
        when(game_state) {
            GameState.SHOW_GAME_START -> renderStartMenu()
            GameState.SHOW_GAME_PLAY  -> renderGamePlay()
            GameState.SHOW_GAME_PAUSE -> renderGamePause()
            GameState.SHOW_GAME_OVER  -> renderGameOver()
        }
    }

    override fun dispose() {
        batch.dispose()
        RL.disposeGameTextures()
    }

    private fun drawPlayer() {
        if(player != null) drawEntity(RL.getTexture(Sprites.PLAYER), player as Entity)
    }

    private fun drawEnemies() {
        EM!!.getAllInvaders().forEach{ drawEntity(RL.getInvaderTextures(it.type)[it.current_texture], it as Entity) }
    }

    private fun drawEntity(texture: Texture, entity: Entity) {
        batch.draw(texture, entity.getRectangle().getX(), entity.getRectangle().getY(), texture.width * texture_scale, texture.height * texture_scale)
    }
}
