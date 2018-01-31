package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import jacob.pozaic.spaceinvaders.entity.EntityManager
import jacob.pozaic.spaceinvaders.input.GameInputHandler
import jacob.pozaic.spaceinvaders.input.GameOverInputHandler
import jacob.pozaic.spaceinvaders.input.PauseMenuInputHandler
import jacob.pozaic.spaceinvaders.input.StartMenuInputHandler
import jacob.pozaic.spaceinvaders.resources.ResourceLoader

class SpaceInvaders : ApplicationAdapter() {
    // Most Android devices have a 16:10 aspect ratio, so use that as a scaling reference
    private var screen_width: Float = 800F
    private var screen_height: Float = 480F

    // Assuming all textures are 128x128, downscaled 32x32 to fit an 800x480 screen
    // the scale ratio will scale the sprites back up for larger screen resolutions
    private var texture_scale:Float = 0.25F
    private var scale_ratio_width:Float = 0F
    private var scale_ratio_height:Float = 0F

    private var game_state = GameState.SHOW_GAME_START

    private var logic: Logic? = null
    private var render: Render? = null

    private val RL = ResourceLoader()
    private var EM: EntityManager? = null

    private val input_processors = InputMultiplexer()

    override fun create() {
        scale_ratio_width = Gdx.graphics.width.toFloat() / screen_width
        scale_ratio_height = Gdx.graphics.height.toFloat() / screen_height
        texture_scale *= scale_ratio_width // Technically this wont scale things right height-wise, but it shouldn't be a huge issue

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
        logic = Logic(this)
        // Init the render loop
        render = Render(screen_width, screen_height, texture_scale, this)

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

    fun getResourceLoader(): ResourceLoader {
        return RL
    }

    fun getEntityManager(): EntityManager {
        return EM!!
    }
}
