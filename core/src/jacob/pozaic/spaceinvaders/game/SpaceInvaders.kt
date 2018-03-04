package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import jacob.pozaic.spaceinvaders.entity.Invader
import jacob.pozaic.spaceinvaders.entity.Player
import jacob.pozaic.spaceinvaders.entity.Projectile
import jacob.pozaic.spaceinvaders.resources.ResourceLoader

// Handles asset loading
internal val RL = ResourceLoader()

internal var WM: WaveManager? = null

internal val input_processors = InputMultiplexer()

internal var game: SpaceInvaders? = null

// Constants
internal const val default_tex_height = 128
internal const val default_tex_width = 128
internal const val texture_scale = 0.25F
internal const val ui_texture_scale = 0.2F * default_tex_width
internal const val texture_width = default_tex_width * texture_scale
internal const val texture_height = default_tex_height * texture_scale

internal const val shoot_delay = 1000L

// The state the game is currently in, controls logic and render loops
internal var game_state = GameState.SHOW_GAME_START

// The player
internal var player: Player? = null

internal var invadersUpdated = false
internal var projectilesUpdated = false

// True if the invaders have reached the ground
internal var game_over = false

internal var player_lives = 3
internal var player_score = 0

class SpaceInvaders : ApplicationAdapter() {
    private val invaders = ArrayList<Invader>()

    // A list of each projectile currently on screen
    private val projectiles = ArrayList<Projectile>()

    override fun create() {
        game = this
        // Test accelerometer availability
        if (!Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
            //TODO: draw arrows and add listeners to move the player
        }

        WM = WaveManager(game!!)

        // Create the input handler and set it
        Gdx.input.inputProcessor = input_processors

        // The pixel location on the y axis where the invaders win if reached
        invader_win_distance = y_offset_lose + texture_height

        // The pixel location on the x axis where the invaders should drop and reverse direction
        with(screen){
            width = 800F
            height = 480F
            left = texture_width / 2.0F
            right = width - left
            top = height
            bottom = texture_height
            center_x = left + (right - left) / 2
            center_y = bottom + (top - bottom) / 2
            x_offset = 20F
            y_offset = 50F
        }

        // Load textures
        RL.loadGameTextures()

        // Create new player in the center of the screen
        player = Player(RL.getPlayerTexture(0), screen.center_x, screen.bottom, texture_scale, texture_scale)
        stg_game.addActor(player!!)

        // Create the first wave
        WM!!.createWave(0)

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
        when(game_state){
            GameState.SHOW_GAME_OPTIONS -> stg_options.viewport.update(width, height)
            GameState.SHOW_GAME_OVER -> stg_game.viewport.update(width, height)
            GameState.SHOW_GAME_PLAY -> stg_game.viewport.update(width, height)
            GameState.SHOW_GAME_PAUSE -> stg_game.viewport.update(width, height)
            GameState.SHOW_GAME_START -> stg_start.viewport.update(width, height)
        }
    }

    fun playerLoseLife(){
        player_lives -= 1
        when(player_lives) {
            2 -> player_lives_img[2].remove()
            1 -> player_lives_img[1].remove()
            0 -> {
                player_lives_img[0].remove()
                game_over = true
            }
        }
    }

    fun getInvaders() = invaders

    fun addInvader(invader: Invader) {
        invaders.add(invader)
        invadersUpdated = true
    }

    fun removeInvader(invader: Invader) {
        invaders.remove(invader)
        invadersUpdated = true
    }

    fun getProjectiles() = projectiles

    fun addProjectile(projectile: Projectile) {
        projectiles.add(projectile)
        projectilesUpdated = true
    }

    fun removeProjectile(projectile: Projectile) {
        projectiles.remove(projectile)
        projectilesUpdated = true
    }
}
