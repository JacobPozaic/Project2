package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.TimeUtils
import jacob.pozaic.spaceinvaders.entity.Entity
import jacob.pozaic.spaceinvaders.entity.EntityManager
import jacob.pozaic.spaceinvaders.input.GameInputHandler
import jacob.pozaic.spaceinvaders.input.GameOverInputHandler
import jacob.pozaic.spaceinvaders.input.PauseMenuInputHandler
import jacob.pozaic.spaceinvaders.input.StartMenuInputHandler
import jacob.pozaic.spaceinvaders.resources.ResourceLoader
import jacob.pozaic.spaceinvaders.resources.Sprites

class SpaceInvaders : ApplicationAdapter() {
    // Most Android devices have a 16:10 aspect ratio, so use that as a scaling reference
    private var screen_width: Float = 800F
    private var screen_height: Float = 480F

    // Assuming all textures are 128x128, downscaled 32x32 to fit an 800x480 screen
    // the scale ratio will scale the sprites back up for larger screen resolutions
    private var texture_scale:Float = 0.25F
    private var scale_ratio_width:Float = 0F
    private var scale_ratio_height:Float = 0F

    // The state the game is currently in, controls logic and render loops
    private var game_state = GameState.SHOW_GAME_START

    // The singleton Logic and Render loop
    private var logic: Logic? = null
    private var render: Render? = null

    // Handles asset loading
    private val RL = ResourceLoader()
    // Handles enemy and player movement and controls
    private var EM: EntityManager? = null

    private val input_processors = InputMultiplexer()

    override fun create() {
        // Get the scale ratio from the template size to the actual size
        scale_ratio_width = Gdx.graphics.width.toFloat() / screen_width
        scale_ratio_height = Gdx.graphics.height.toFloat() / screen_height
        // Technically this wont scale things right height-wise, but it shouldn't be a huge issue
        texture_scale *= scale_ratio_width

        screen_width = Gdx.graphics.width.toFloat()
        screen_height = Gdx.graphics.height.toFloat()

        // Create the input handler and set it
        Gdx.input.inputProcessor = input_processors

        // Create the Entity Manager
        EM = EntityManager(RL, scale_ratio_width, scale_ratio_height, texture_scale, screen_width, screen_height)

        // Test accelerometer availability
        if (!Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
            //TODO: tell user accelerometer was not found and close app
        }

        // Init the logic loop
        logic = Logic()
        // Init the render loop
        render = Render()

        // Start the game
        startMenu()
    }

    private fun startMenu() {
        // TODO: display a start menu and wait for input before starting the game
        // Set the input processors to use
        input_processors.clear()
        input_processors.addProcessor(StartMenuInputHandler(this))

        // Set the game state
        game_state = GameState.SHOW_GAME_START

        startGame() //TODO: move to StartMenuInputHandler
    }

    private fun startGame() {
        // Set the input processors to use
        input_processors.clear()
        input_processors.addProcessor(GameInputHandler(this))

        // Load textures
        RL.loadGameTextures()

        // Init the Entity Manager
        EM!!.init()

        // Set the game state
        game_state = GameState.SHOW_GAME_PLAY
    }

    private fun pauseGame() {
        // TODO: pause the game and display the pause menu
        // Set the input processors to use
        input_processors.clear()
        input_processors.addProcessor(PauseMenuInputHandler(this))

        // Set the game state
        game_state = GameState.SHOW_GAME_PAUSE
    }

    fun gameOver() {
        // TODO: End the game, show the final score, etc.
        // Set the input processors to use
        input_processors.clear()
        input_processors.addProcessor(GameOverInputHandler(this))

        // Dispose of game textures
        RL.disposeGameTextures()

        // Set the game state
        game_state = GameState.SHOW_GAME_OVER
    }

    override fun render() {
        // Logic
        logic!!.logicLoop(game_state)

        // Render the frame
        render!!.renderLoop(game_state)
    }

    fun getEntityManager(): EntityManager = EM!!

    private inner class Logic{
        fun logicLoop(game_state: GameState) {
            // Determine what logic path to use
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
            // Lose condition
            if (EM!!.invadersWin()) this@SpaceInvaders.gameOver()

            // TODO: sounds

            // AI TODO: move this code to EntityManager
            EM!!.createWave()

            // Update locations
            EM!!.update()
        }

        private fun gamePause() {
            //TODO:
        }

        private fun gameOver() {
            //TODO:
        }
    }

    private inner class Render {
        private val batch = SpriteBatch()
        private val camera = OrthographicCamera()

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
                GameState.SHOW_GAME_PLAY  -> gamePlay()
                GameState.SHOW_GAME_PAUSE -> gamePause()
                GameState.SHOW_GAME_OVER  -> gameOver()
            }
        }

        private fun startMenu() {
            // TODO:
        }

        private fun gamePlay() {
            batch.begin()

            // Draw the background
            batch.draw(RL.getTexture(Sprites.BACKGROUND), 0F, 0F, screen_width, screen_height)

            // Draw the invaders
            EM!!.getAllInvaders().forEach{
                drawEntity(RL.getInvaderTextures(it.type)[it.current_texture], it as Entity)
            }

            // Draw projectiles
            EM!!.getProjectiles().forEach {
                drawEntity(RL.getTexture(it.type), it as Entity)
            }

            // Draw the player
            drawEntity(RL.getTexture(Sprites.PLAYER), EM!!.getPlayer() as Entity)

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

        private fun drawEntity(texture: Texture, entity: Entity) {
            batch.draw(texture, entity.getX(), entity.getY(), texture.width * texture_scale, texture.height * texture_scale)
        }
    }
}
