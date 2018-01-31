package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.TimeUtils
import jacob.pozaic.spaceinvaders.entity.Entity
import jacob.pozaic.spaceinvaders.resources.Sprites

class Render (
        private val screen_width: Float,
        private val screen_height: Float,
        private val texture_scale: Float,
        game: SpaceInvaders){

    private val batch = SpriteBatch()
    private val camera = OrthographicCamera()

    private val RL = game.getResourceLoader()
    private val EM = game.getEntityManager()

    init {
        // Initialize the camera
        camera.setToOrtho(false, screen_width, screen_height)
    }

    fun renderLoop(game_state: GameState) {
        // Render frame
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // Update camera
        camera.update()

        // Update the batch projection
        batch.projectionMatrix = camera.combined

        // Determine what render path to use
        when(game_state) {
            GameState.SHOW_GAME_START -> startMenu()
            GameState.SHOW_GAME_PLAY -> gamePlay()
            GameState.SHOW_GAME_PAUSE -> gamePause()
            GameState.SHOW_GAME_OVER -> gameOver()
        }
    }

    private fun startMenu() {
        // TODO:
    }

    private fun gamePlay() {
        batch.begin()
        batch.draw(RL.getTexture(Sprites.BACKGROUND), 0F, 0F, screen_width, screen_height)
        drawPlayer()
        drawEnemies()
        batch.end()
    }

    private fun gamePause() {
        //TODO:
    }

    private fun gameOver() {
        //TODO:
    }

    private fun dispose() {
        batch.dispose()
        //TODO: dispose of any textures, this should be done when swapping stages...
    }

    private fun drawPlayer() {
        drawEntity(RL.getTexture(Sprites.PLAYER), EM.getPlayer() as Entity)
    }

    private fun drawEnemies() {
        EM.getAllInvaders().forEach{ drawEntity(RL.getInvaderTextures(it.type)[it.current_texture], it as Entity) }
    }

    private fun drawEntity(texture: Texture, entity: Entity) {
        batch.draw(texture, entity.getRectangle().getX(), entity.getRectangle().getY(), texture.width * texture_scale, texture.height * texture_scale)
    }
}