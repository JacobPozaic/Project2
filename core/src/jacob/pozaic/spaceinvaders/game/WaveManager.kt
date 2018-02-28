package jacob.pozaic.spaceinvaders.game

import jacob.pozaic.spaceinvaders.ai.LinearMove
import jacob.pozaic.spaceinvaders.ai.MoveGroup
import jacob.pozaic.spaceinvaders.ai.Pos
import jacob.pozaic.spaceinvaders.entity.Invader
import jacob.pozaic.spaceinvaders.entity.InvaderType

class WaveManager(private val game: SpaceInvaders) {
    private var current_wave = Wave(game, ArrayList())

    fun createWave(wave_number: Int){
        if(current_wave.hasInvaders()) return
        current_wave = when(wave_number % 5 + 1){
            1 -> defaultWave()
            2 -> return
            3 -> return
            4 -> return
            5 -> return
            else -> return
        }
    }

    private fun defaultWave(): Wave {
        var invader_type = InvaderType.FIGHTER
        for(y in 0..4) {
            // Depending on what row, different invaders will spawn
            when(y){
                0, 1 -> invader_type = InvaderType.FIGHTER
                2, 3 -> invader_type = InvaderType.BOMBER
                4    -> invader_type = InvaderType.MOTHER_SHIP
            }

            val tex_default = RL.getInvaderTexture(invader_type, 0)
            val texture_width = tex_default.regionWidth * texture_scale
            val texture_height = tex_default.regionHeight * texture_scale

            for(x in 0..10) {
                // Calculate the position of the new Invader in scaled space
                val posX = (texture_width * x) + (spacing_offset * x) + screen.x_offset
                val posY = screen.height - texture_height - screen.y_offset - ((texture_height * y) + (spacing_offset * 0.5F * y))
                // Create a new invader
                game.addInvader(Invader(RL.getInvaderTexture(invader_type, 0), posX, posY, texture_scale, texture_scale, 500L, invader_type))
            }
        }

        // Create the MoveGroup for the wave
        val move_patterns = ArrayList<MoveGroup>()
        val move_pattern = MoveGroup(game)

        // Assign each invader to the MoveGroup NOTE: Invaders need to be added before the Move component
        game.getInvaders().forEach { it.move_group = move_pattern } // TODO: this can be kinda boxed in with the invader creation

        // Add components of the MoveGroup
        val move_across = LinearMove()
        with(move_across) {
            start(Pos(screen.left, 350F))
            adjustStart(move_pattern.group_width / 2, move_pattern.group_height / 2)
            end(Pos(screen.right, 350F))
            adjustEnd(move_pattern.group_width / 2, move_pattern.group_height / 2)
            setStepByDistance(0.5F, 10F)
        }
        move_pattern.addMovement(move_across)
        // moveGroupToStart() only works after the first Movement has been added
        move_pattern.moveGroupToStart()

        //TODO: finish adding movement parts
        move_patterns.add(move_pattern)

        return Wave(game, move_patterns)
    }

    fun update(delta: Float) {
        // Each frame update the Wave, in turn updating all Invaders
        current_wave.update(delta)
    }
}