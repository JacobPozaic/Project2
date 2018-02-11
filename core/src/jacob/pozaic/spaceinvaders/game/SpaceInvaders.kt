package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.utils.viewport.FitViewport
import jacob.pozaic.spaceinvaders.entity.EntityManager
import jacob.pozaic.spaceinvaders.resources.ResourceLoader

// Most Android devices have a 16:10 aspect ratio, so use that as a scaling reference
internal var screen_width: Float = 800F
internal var screen_height: Float = 480F

internal var viewport: FitViewport? = null
internal var texture_scale = 0.25F

// The state the game is currently in, controls logic and render loops
internal var game_state = GameState.SHOW_GAME_START
// Handles asset loading
internal val RL = ResourceLoader()
// Handles enemy and player movement and controls
internal var EM: EntityManager? = null

internal val input_processors = InputMultiplexer()

class SpaceInvaders : ApplicationAdapter() {
    override fun create() {
        viewport = FitViewport(screen_width, screen_height)

        // Create the input handler and set it
        Gdx.input.inputProcessor = input_processors

        // Create the Entity Manager
        EM = EntityManager(RL, texture_scale, screen_width, screen_height)

        // Test accelerometer availability
        if (!Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
            //TODO: tell user accelerometer was not found and close app
        }

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
