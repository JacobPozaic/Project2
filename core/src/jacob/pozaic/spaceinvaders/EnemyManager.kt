package jacob.pozaic.spaceinvaders

class EnemyManager (private val RL: ResourceLoader, var texture_scale: Float = 0F, var scale_ratio:Float = 0F, var screen_height: Float = 480F) {
    private var invaders = ArrayList<Invader>()

    fun createWave() {
        if(noInvaders()) {
            // Constant offsets, scaled to the screen size
            val x_offset = 20 * scale_ratio
            val y_offset = 60 * scale_ratio
            val spacing_offset = 10 * scale_ratio

            var invader_type = InvaderType.FIGHTER
            for(y in 0..4) {
                // Depending on what row, different invaders will spawn
                when(y){
                    0, 1 -> invader_type = InvaderType.FIGHTER
                    2, 3 -> invader_type = InvaderType.BOMBER
                    4 -> invader_type = InvaderType.MOTHER_SHIP
                }

                for(x in 0..10) {
                    // Get the size of the texture after scaling it
                    val default_texture = RL.getInvaderTextures(invader_type)[0]
                    val texture_width = default_texture.width * texture_scale
                    val texture_height = default_texture.height * texture_scale

                    // Calculate the position of the new Invader in scaled space
                    val posX = (x * texture_width) + (spacing_offset * x) + x_offset
                    val posY = screen_height - texture_height - y_offset - ((y * texture_height) + (spacing_offset * 0.5F * y))

                    invaders.add(Invader(texture_width, posX , posY, invader_type))
                }
            }
        }
    }

    fun step() {
        //TODO: if time has passed then move all enemies
        //TODO: cycle invader texture (fix textures first)
    }

    fun noInvaders(): Boolean {
        return invaders.size == 0
    }

    fun invadersWin(): Boolean {
        return false //TODO: enforce lose condition
    }

    fun getAllInvaders(): ArrayList<Invader> {
        return invaders
    }
}