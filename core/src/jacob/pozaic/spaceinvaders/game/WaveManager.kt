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
        // Create the MoveGroup for the wave
        val move_pattern = MoveGroup(game)

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
                // Get the default texture for the invader
                val invader_tex = RL.getInvaderTexture(invader_type, 0)
                // Create a new invader
                val invader = Invader(invader_tex, posX, posY, texture_scale, texture_scale, move_pattern, 500L, invader_type)
                game.addInvader(invader)
            }
        }

        // Add components of the MoveGroup
        val move_across_1 = LinearMove()
        val g_width = move_pattern.getGroupWidth() / 2
        val g_height = move_pattern.getGroupHeight() / 2
        with(move_across_1) {
            start(adjustPoint(Pos(screen.left, 350F), g_width, g_height))
            end(adjustPoint(Pos(screen.right, 350F), g_width, g_height))
            setStepByDistance(0.5F, 10F)
        }
        move_pattern.addMovement(move_across_1)

        val move_down_1 = LinearMove()
        with(move_down_1){
            start(adjustPoint(move_across_1.end, g_width, g_height))
            end(adjustPoint(Pos(move_across_1.end.x, move_across_1.end.y - 20), g_width, g_height))
            setStepByDistance(0.5F, 20F)
        }
        move_pattern.addMovement(move_down_1)

        val move_across_2 = LinearMove()
        with(move_across_2) {
            start(adjustPoint(move_down_1.end, g_width, g_height))
            end(adjustPoint(Pos(screen.left, move_down_1.end.y), g_width, g_height))
            setStepByDistance(0.5F, 10F)
        }
        move_pattern.addMovement(move_across_2)

        val move_down_2 = LinearMove()
        with(move_down_2){
            start(adjustPoint(move_across_2.end, g_width, g_height))
            end(adjustPoint(Pos(move_across_2.end.x, move_across_2.end.y - 20), g_width, g_height))
            setStepByDistance(0.5F, 20F)
        }
        move_pattern.addMovement(move_down_1)
        //TODO: finish adding movement parts

        move_pattern.moveGroupToStart()
        return Wave(game, listOf(move_pattern))
    }

    fun update(delta: Float) {
        // Each frame update the Wave, in turn updating all Invaders
        current_wave.update(delta)
    }
}