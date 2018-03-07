package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import jacob.pozaic.spaceinvaders.entity.Invader
import jacob.pozaic.spaceinvaders.entity.Player
import jacob.pozaic.spaceinvaders.entity.Projectile
import jacob.pozaic.spaceinvaders.resources.ResourceLoader
import java.util.*

internal data class Screen(var width: Float,
                  var height: Float,
                  var left: Float,
                  var right: Float,
                  var top: Float,
                  var bottom: Float,
                  var center_x: Float,
                  var center_y: Float,
                  var x_offset: Float,
                  var y_offset: Float)

// A reference to the game for callbacks
internal var game: SpaceInvaders? = null

// Handles asset loading
internal val RL = ResourceLoader()

// Controls invader creation and movement
internal var WM: WaveManager? = null

// The player
internal var player: Player? = null

// Utility
internal val input_processors = InputMultiplexer()
internal val rand = Random()

// Texture parameters
internal const val default_tex_height = 128
internal const val default_tex_width = 128
internal const val texture_scale = 0.25F
internal const val ui_texture_scale = 0.25F * default_tex_width
internal const val texture_width = default_tex_width * texture_scale
internal const val texture_height = default_tex_height * texture_scale

// Movement parameters
// Constant offsets, scaled to the screen size
internal const val spacing_offset = 10F
internal const val y_offset_lose  = 100F
// The movement constraints of the viewport
internal val screen: Screen = Screen(800F, 480F, 0F, 0F, 0F, 0F, 400F, 240F, 0F, 0F)

// The pixel location on the y axis where the invaders win if reached
internal const val invader_win_distance = y_offset_lose + texture_height //TODO: check this condition

// Player parameters
// The degree to which the screen should be tilted before moving in that direction
internal const val tilt_sensitivity = 1F

// The speed the player moves at
internal const val player_speed = 150F

// Player shoot delay
internal const val shoot_delay = 1000L

// Invader parameters
// Invader shoot parameters
internal const val max_enemy_projectiles = 5
internal const val enemy_shoot_chance = 15
internal const val min_enemy_shoot_delay = 300L
internal const val max_enemy_shoot_delay = 3000L

// Projectile parameters
// Projectile move speeds
internal const val player_projectile_speed = 150F
internal const val fighter_projectile_speed = -150F
internal const val bomber_projectile_speed = -100F
internal const val mother_ship_projectile_speed = -90F

// Game variables
// The state the game is currently in, controls logic and render loops
internal var game_state = GameState.SHOW_GAME_START

internal var enemy_last_shoot_time = 0L

// If the list of invaders or projectiles has been changed since last frame
internal var invadersUpdated = false
internal var projectilesUpdated = false

// True if the invaders have reached the ground
internal var game_over = false

// Disable tilt input and draw arrows to move the player instead
internal var draw_arrows = false

// Game stats
internal var player_lives = 3
internal var player_score = 0 // TODO: display on loss
internal var wave_number = 0 // TODO: display on loss

class SpaceInvaders : ApplicationAdapter() {
    // A list of each invader currently on screen
    private val invaders = ArrayList<Invader>()

    // A list of each projectile currently on screen
    private val projectiles = ArrayList<Projectile>()

    override fun create() {
        game = this

        // Set the viewport constraints
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

        // Create the input handler and set it
        Gdx.input.inputProcessor = input_processors

        // Init the wave manager
        WM = WaveManager(this)

        // Load textures
        RL.loadGameTextures()

        // Create the player
        player = Player(RL.getPlayerTexture(0), screen.center_x, screen.bottom, texture_scale, texture_scale, game!!)
        stg_game.addActor(player!!)

        // Test accelerometer availability and set draw_arrows if it is unavailable
        if (!Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) draw_arrows = true

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

    /**
     * Removes a player life when called, if no lives are left then the game ends
     */
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

    /**
     * Returns true if the game is using the accelerometer
     */
    fun useAccelerometer() = !draw_arrows

    /**
     * Gets the list of invaders
     * NOTE: do not add or remove invaders from here or else render will not be updated properly
     */
    fun getInvaders() = invaders

    /**
     * Adds an invader to the game and notifies the logic loop that the list has been modified
     */
    fun addInvader(invader: Invader) {
        invaders.add(invader)
        invadersUpdated = true
    }

    /**
     * Removes an invader from the game and notifies the logic loop that the list has been modified
     */
    fun removeInvader(invader: Invader) {
        invaders.remove(invader)
        invadersUpdated = true
    }

    /**
     * Gets the list of projectiles
     * NOTE: do not add or remove projectiles from here or else render will not be updated properly
     */
    fun getProjectiles() = projectiles

    /**
     * Adds a projectile to the game and notifies the logic loop that the list has been modified
     */
    fun addProjectile(projectile: Projectile) {
        projectiles.add(projectile)
        projectilesUpdated = true
    }

    /**
     * Removes a projectile from the game and notifies the logic loop that the list has been modified
     */
    fun removeProjectile(projectile: Projectile) {
        projectiles.remove(projectile)
        projectilesUpdated = true
    }
}
