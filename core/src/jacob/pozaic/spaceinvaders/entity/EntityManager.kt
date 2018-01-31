package jacob.pozaic.spaceinvaders.entity

import jacob.pozaic.spaceinvaders.resources.ResourceLoader
import jacob.pozaic.spaceinvaders.resources.Sprites

class EntityManager(private val RL: ResourceLoader,
                    scale_ratio_width: Float,
                    scale_ratio_height: Float,
                    private val texture_scale: Float,
                    private val screen_width: Float,
                    private val screen_height: Float) {
    // Constant offsets, scaled to the screen size
    private val x_offset = 20 * scale_ratio_width
    private val y_offset = 50 * scale_ratio_height
    private val y_offset_lose = 100 * scale_ratio_height
    private val spacing_offset = 10 * scale_ratio_width
    private val drop_distance = 20 * scale_ratio_height

    // A list of every invader currently alive
    private var invaders = ArrayList<Invader>()

    // The pixel location on the x axis where the invaders should drop and reverse direction
    private var screen_left_cutoff = 0F
    private var screen_right_cutoff = 0F
    // The pixel location on the y axis where the invaders win if reached
    private var invader_win_distance = 0F

    // true if the invaders have reached the ground
    private var game_over = false

    // The player
    private var player: Player? = null
    private val player_width = RL.getTexture(Sprites.PLAYER).width * texture_scale / 2

    fun init() {
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
                    // Get the size of the texture after scaling it
                    val default_texture = RL.getInvaderTextures(invader_type)[0]
                    val texture_width   = default_texture.width * texture_scale
                    val texture_height  = default_texture.height * texture_scale
                    val step_distance   = (screen_width  - (11F * (texture_width + spacing_offset))) / 40F

                    // if cutoffs haven't been calculated yet then do so
                    if(screen_left_cutoff == 0F) {
                        screen_left_cutoff  = x_offset
                        screen_right_cutoff = screen_width - (x_offset + texture_width)
                        invader_win_distance = y_offset_lose + texture_height
                    }

                    // Calculate the position of the new Invader in scaled space
                    val posX = (x * texture_width) + (spacing_offset * x) + x_offset
                    val posY = screen_height - texture_height - y_offset - ((y * texture_height) + (spacing_offset * 0.5F * y))

                    invaders.add(Invader(texture_width, posX, posY, invader_type, step_distance, drop_distance))
                }
            }
        }
    }

    // The degree to which the screen should be tilted before moving in that direction
    private val tilt_sensitivity = 1F
    // The speed at which the player moves
    private val player_move_speed = 10F

    fun stepPlayer(gyro_angle: Float) {
        val location = player!!.getX()

        when{
            gyro_angle > tilt_sensitivity ->
                if(location >= screen_right_cutoff) return
                else player!!.step(player_move_speed)
            gyro_angle < -tilt_sensitivity ->
                if(location <= screen_left_cutoff) return
                else player!!.step(-player_move_speed)
            else -> 0F
        }
    }

    // Player shoots a projectile
    fun playerShoot(){
        val location = player!!.getX() + player_width
        //TODO: create projectile at position, move it and check collision with invader
    }

    // Should the next step move the invaders to the right
    private var move_direction_right = true

    fun stepEnemy() {
        var flip_direction = false
        invaders.forEach {
            val location = it.step(move_direction_right)
            if((move_direction_right && location >= screen_right_cutoff) || (!move_direction_right && location <= screen_left_cutoff))
                flip_direction = true
        }
        if(flip_direction) {
            invaders.forEach {
                val location = it.drop()
                if(location <= invader_win_distance)
                    game_over = true
            }
            move_direction_right = move_direction_right.not()
        }

        //TODO: cycle invader texture (fix textures first)
    }

    fun invadersWin(): Boolean {
        return game_over
    }

    fun getPlayer(): Player {
        return player!!
    }

    fun getAllInvaders(): ArrayList<Invader> {
        return invaders
    }
}