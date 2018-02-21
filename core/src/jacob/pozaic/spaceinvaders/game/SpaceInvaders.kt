package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.FitViewport
import jacob.pozaic.spaceinvaders.entity.Player
import jacob.pozaic.spaceinvaders.resources.ResourceLoader
import jacob.pozaic.spaceinvaders.resources.Sprites

// Handles asset loading
internal val RL = ResourceLoader()

internal val WM: WaveManager = WaveManager()

// Most Android devices have a 16:10 aspect ratio, so use that as a scaling reference
internal var screen_width: Float = 800F
internal var screen_height: Float = 480F

internal val camera = OrthographicCamera(screen_width, screen_height)
internal var viewport: FitViewport? = null

internal const val default_tex_height = 128
internal const val default_tex_width = 128
internal var texture_scale = 0.25F
internal val texture_width = default_tex_width * texture_scale
internal val texture_height = default_tex_height * texture_scale

internal val player_width = RL.getTexture(Sprites.PLAYER).width * texture_scale / 2

// The state the game is currently in, controls logic and render loops
internal var game_state = GameState.SHOW_GAME_START

internal val input_processors = InputMultiplexer()

// The player
var player: Player? = null

//TODO: deal with this
// True if the invaders have reached the ground
var game_over = false

class SpaceInvaders : ApplicationAdapter() {
    override fun create() {
        camera.setToOrtho(false, screen_width, screen_height)
        viewport = FitViewport(screen_width, screen_height, camera)

        // Create the input handler and set it
        Gdx.input.inputProcessor = input_processors

        // Load textures
        RL.loadGameTextures()

        // The pixel location on the x axis where the invaders should drop and reverse direction
        screen_left_cutoff = 0F
        screen_right_cutoff = screen_width
        screen_top_cutoff = screen_height

        // The pixel location on the y axis where the invaders win if reached
        invader_win_distance = y_offset_lose + texture_height

        // Add the WaveManager to the Scene (it has no visual components, but makes use of the act() call)
        stg_game.addActor(WM)

        // Create new player in the center of the screen
        player = Player(RL.getPlayerTexture(), (screen_width / 2) - player_width, 0F, texture_scale, texture_scale)
        stg_game.addPlayer(player!!)

        // Create the first wave
        WM.createWave(1)

        // Test accelerometer availability
        if (!Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
            //TODO: draw arrows and add listeners to move the player
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

    override fun resize(width: Int, height: Int) {
        viewport!!.update(width, height)
        stg_game.viewport.update(width, height)
    }
}
