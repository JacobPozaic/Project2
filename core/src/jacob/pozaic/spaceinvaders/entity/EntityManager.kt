package jacob.pozaic.spaceinvaders.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.TimeUtils
import jacob.pozaic.spaceinvaders.resources.ResourceLoader
import jacob.pozaic.spaceinvaders.resources.Sprites

class EntityManager(RL: ResourceLoader,
                    texture_scale: Float,
                    private val screen_width: Float,
                    private val screen_height: Float) {
    // Constant offsets, scaled to the screen size
    private val x_offset       = 20F
    private val spacing_offset = 10F
    private val player_speed   = 120F
    private val y_offset       = 50F
    private val y_offset_lose  = 100F
    private val drop_distance  = 20F
    private val player_projectile_speed = 150F
    // TODO: enemy projectile speed differs based on what type it is

    // Get the size of the texture after scaling it
    private val default_texture = RL.getTexture(Sprites.FIGHTER_1)
    private val texture_width   = default_texture.width * texture_scale
    private val texture_height  = default_texture.height * texture_scale

    private val player_width = RL.getTexture(Sprites.PLAYER).width * texture_scale / 2

    private val collision_size = RL.getTexture(Sprites.PLAYER).width * texture_scale

    // The size of projectiles
    private val projectile_size = RL.getTexture(Sprites.PLAYER_PROJECTILE).width * texture_scale

    // A list of every invader currently alive
    private var invaders = ArrayList<Invader>()

    // A list of each projectile currently on screen
    private val projectiles = ArrayList<Projectile>()

    // The pixel location on the x axis where the invaders should drop and reverse direction
    private val screen_left_cutoff = x_offset
    private val screen_right_cutoff = screen_width - (x_offset + texture_width)
    private val screen_top_cutoff = screen_height

    // The pixel location on the y axis where the invaders win if reached
    private val invader_win_distance = y_offset_lose + texture_height

    // True if the invaders have reached the ground
    private var game_over = false

    // The player
    private var player: Player? = null

    // If the player has shot, this will store the entity for the projectile
    private var player_projectile: Projectile? = null

    fun init() {
        val step_distance = (screen_width - (11F * (texture_width + spacing_offset))) / 38F

        // Set the movement patterns for each type of invader
        setMovement(InvaderType.FIGHTER, step_distance, drop_distance)
        setMovement(InvaderType.BOMBER, step_distance, drop_distance)
        setMovement(InvaderType.MOTHER_SHIP, step_distance, drop_distance)

        // Create new player in the center of the screen
        player = Player(posX = (screen_width / 2) - player_width)
        createWave()
    }

    /**
     * If no invaders are left, create a new wave of invaders
     */
    fun createWave() {
        if(invaders.size == 0) {
            var invader_type = InvaderType.FIGHTER
            for(y in 0..4) {
                // Depending on what row, different invaders will spawn
                when(y){
                    0, 1 -> invader_type = InvaderType.FIGHTER
                    2, 3 -> invader_type = InvaderType.BOMBER
                    4    -> invader_type = InvaderType.MOTHER_SHIP
                }

                for(x in 0..10) {
                    // Calculate the position of the new Invader in scaled space
                    val posX = (texture_width * x) + (spacing_offset * x) + x_offset
                    val posY = screen_height - texture_height - y_offset - ((texture_height * y) + (spacing_offset * 0.5F * y))
                    // Create a new invader
                    invaders.add(Invader(texture_width, posX, posY, invader_type))
                }
            }
        }
    }

    // The last time step was called
    private var last_enemy_step_time = 0L
    // The time between each step
    private val enemy_step_delay = 600L //TODO: less enemies increases speed, also make this 700L when not debugging

    // Should the next step move the invaders to the right
    private var move_direction_right = true

    // The degree to which the screen should be tilted before moving in that direction
    private val tilt_sensitivity = 1F

    private val projectiles_destroyed = ArrayList<Projectile>()

    private var flip_direction = false

    fun update() {
        // Move the enemies if it has been long enough
        if (TimeUtils.timeSinceMillis(last_enemy_step_time) >= enemy_step_delay) {
            last_enemy_step_time = TimeUtils.millis()

            if(flip_direction) {
                flip_direction = false
                invaders.forEach {
                    val location = it.drop()
                    if(location <= invader_win_distance)
                        game_over = true
                }
                move_direction_right = move_direction_right.not()
            } else {
                invaders.forEach {
                    val location = it.step(move_direction_right)
                    if((move_direction_right && location >= screen_right_cutoff) ||
                            (!move_direction_right && location <= screen_left_cutoff))
                        flip_direction = true
                }
            }

            // TODO: cycle invader texture (fix textures first)
        }

        // Move the player
        val gyro_angle = Gdx.input.accelerometerY
        val location = player!!.getX()

        when{
            gyro_angle > tilt_sensitivity ->
                if(location < screen_right_cutoff)
                    player!!.step(player_speed * Gdx.graphics.deltaTime)
            gyro_angle < -tilt_sensitivity ->
                if(location > screen_left_cutoff)
                    player!!.step(-player_speed * Gdx.graphics.deltaTime)
        }

        // Move the players projectile if one exists
        if(player_projectile != null) {
            player_projectile!!.step(player_projectile_speed * Gdx.graphics.deltaTime)
        }

        // TODO: step invader projectiles

        // Check for any collisions in projectiles
        projectiles.forEach nextProjectile@{p ->
            when {
                p.type != Sprites.PLAYER_PROJECTILE ->
                    when {
                        p.getY() <= player!!.getY() -> projectiles_destroyed.add(p) // Invader projectile left screen
                        p.distanceTo(player!!) <= collision_size -> {               // Invader projectile hit player
                            projectiles_destroyed.add(p)
                            // TODO: player loses life
                        } else -> projectiles.forEach { p_invader ->                // Projectile collides with another projectile
                            if (p.distanceTo(p_invader) <= collision_size)
                                projectiles.addAll(arrayOf(p, p_invader))
                        }
                    }
                p.getY() >= screen_top_cutoff -> projectiles_destroyed.add(p)       // Player projectile left screen
                else -> invaders.forEach {invader ->                                // Invader collision
                    if(p.distanceTo(invader) <= collision_size) {
                        projectiles_destroyed.add(p)
                        invaders.remove(invader)
                        // TODO: add score
                        return@nextProjectile
                    }
                }
            }
        }
        // Remove the destroyed projectiles and then clear the list
        projectiles_destroyed.forEach {
            if(projectiles.contains(it)) {
                if (it.type == Sprites.PLAYER_PROJECTILE)
                    player_projectile = null
                projectiles.remove(it)
            }
        }
        projectiles_destroyed.clear()
    }

    // Player shoots a projectile
    fun playerShoot(){
        if(player_projectile == null) {
            val location = player!!.getX() + player_width
            player_projectile = Projectile(projectile_size, location, player!!.getY(), Sprites.PLAYER_PROJECTILE)
            projectiles.add(player_projectile!!)
            // TODO: projectile animate
        }
    }

    fun invadersWin(): Boolean = game_over

    fun getPlayer(): Player = player!!

    fun getAllInvaders(): ArrayList<Invader> = invaders

    fun getProjectiles(): ArrayList<Projectile> = projectiles
}