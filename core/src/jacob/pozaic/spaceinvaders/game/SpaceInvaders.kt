package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import jacob.pozaic.spaceinvaders.entity.EntityManager
import jacob.pozaic.spaceinvaders.resources.ResourceLoader

// Most Android devices have a 16:10 aspect ratio, so use that as a scaling reference
internal var screen_width: Float = 800F
internal var screen_height: Float = 480F

// Assuming all textures are 128x128, downscaled 32x32 to fit an 800x480 screen
// The scale ratio will scale the sprites back up for larger screen resolutions
internal var texture_scale:Float = 0.25F
internal var scale_ratio_width:Float = 0F
internal var scale_ratio_height:Float = 0F

// The state the game is currently in, controls logic and render loops
internal var game_state = GameState.SHOW_GAME_START
// Handles asset loading
internal val RL = ResourceLoader()
// Handles enemy and player movement and controls
internal var EM: EntityManager? = null

internal val input_processors = InputMultiplexer()

class SpaceInvaders : ApplicationAdapter() {
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
        
        // Initialize the render loop
        initRender()

        // Start the game
        startMenu()
    }

    override fun render() {
        // Logic
        logicLoop()

        // Render the frame
        renderLoop()
    }
}
