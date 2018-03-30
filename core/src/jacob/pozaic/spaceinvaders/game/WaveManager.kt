package jacob.pozaic.spaceinvaders.game

import jacob.pozaic.spaceinvaders.ai.LinearMove
import jacob.pozaic.spaceinvaders.ai.MoveGroup
import jacob.pozaic.spaceinvaders.ai.Pos
import jacob.pozaic.spaceinvaders.entity.Invader

class WaveManager(private val game: SpaceInvaders) {
    private var current_wave: List<MoveGroup>? = null

    fun update() {
        createWave()
        // Each frame update the Wave, in turn updating all Invaders
        if(current_wave != null)
            current_wave!!.forEach { move_group -> move_group.move() }
    }

    private fun createWave(){
        if(game.getInvaders().size == 0) {
            wave_number++
            current_wave = when(wave_number % 5){
                1 -> defaultWave()
                2 -> defaultWave()
                3 -> defaultWave()
                4 -> defaultWave()
                5 -> defaultWave()
                else -> return // This will never happen
            }
        }
    }

    private fun defaultWave(): List<MoveGroup> {
        // Create the MoveGroup for the wave
        val move_pattern = MoveGroup(game)

        var invader_type = EntityType.FIGHTER
        for(y in 0..4) {
            // Depending on what row, different invaders will spawn
            when(y){
                0, 1, 2 -> invader_type = EntityType.BOMBER
                3, 4    -> invader_type = EntityType.FIGHTER
            }

            val tex_default = game.entity(invader_type).textures[0]
            val texture_width = tex_default.regionWidth * texture_scale
            val texture_height = tex_default.regionHeight * texture_scale

            for(x in 0..10) {
                // Calculate the position of the new Invader in scaled space
                val posX = (texture_width * x) + (spacing_offset * x) + screen.x_offset
                val posY = screen.height - texture_height - screen.y_offset - ((texture_height * y) + (spacing_offset * 0.5F * y))
                // Create a new invader
                val invader = Invader(invader_type, posX, posY, texture_scale, move_pattern)
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
            start(adjustPoint(Pos(screen.right, move_across_1.end.y), g_width, g_height))
            end(adjustPoint(Pos(screen.right, move_across_1.end.y - 20), g_width, g_height))
            setStepByDistance(0.5F, 20F)
        }
        move_pattern.addMovement(move_down_1)

        val move_across_2 = LinearMove()
        with(move_across_2) {
            start(adjustPoint(Pos(screen.right, move_down_1.end.y), g_width, g_height))
            end(adjustPoint(Pos(screen.left, move_down_1.end.y), g_width, g_height))
            setStepByDistance(0.5F, 10F)
        }
        move_pattern.addMovement(move_across_2)

        val move_down_2 = LinearMove()
        with(move_down_2){
            start(adjustPoint(Pos(screen.left, move_across_2.end.y), g_width, g_height))
            end(adjustPoint(Pos(screen.left, move_across_2.end.y - 20), g_width, g_height))
            setStepByDistance(0.5F, 20F)
        }
        move_pattern.addMovement(move_down_2)

        val move_across_3 = LinearMove()
        with(move_across_3) {
            start(adjustPoint(Pos(screen.left, move_down_2.end.y), g_width, g_height))
            end(adjustPoint(Pos(screen.right, move_down_2.end.y), g_width, g_height))
            setStepByDistance(0.5F, 10F)
        }
        move_pattern.addMovement(move_across_3)

        val move_down_3 = LinearMove()
        with(move_down_3){
            start(adjustPoint(Pos(screen.right, move_across_3.end.y), g_width, g_height))
            end(adjustPoint(Pos(screen.right, move_across_3.end.y - 20), g_width, g_height))
            setStepByDistance(0.5F, 20F)
        }
        move_pattern.addMovement(move_down_3)

        val move_across_4 = LinearMove()
        with(move_across_4) {
            start(adjustPoint(Pos(screen.right, move_down_3.end.y), g_width, g_height))
            end(adjustPoint(Pos(screen.left, move_down_3.end.y), g_width, g_height))
            setStepByDistance(0.5F, 10F)
        }
        move_pattern.addMovement(move_across_4)

        val move_down_4 = LinearMove()
        with(move_down_4){
            start(adjustPoint(Pos(screen.left, move_across_4.end.y), g_width, g_height))
            end(adjustPoint(Pos(screen.left, move_across_4.end.y - 20), g_width, g_height))
            setStepByDistance(0.5F, 20F)
        }
        move_pattern.addMovement(move_down_4)

        val move_across_5 = LinearMove()
        with(move_across_5) {
            start(adjustPoint(Pos(screen.left, move_down_4.end.y), g_width, g_height))
            end(adjustPoint(Pos(screen.right, move_down_4.end.y), g_width, g_height))
            setStepByDistance(0.5F, 10F)
        }
        move_pattern.addMovement(move_across_5)

        val move_down_5 = LinearMove()
        with(move_down_5){
            start(adjustPoint(Pos(screen.right, move_across_5.end.y), g_width, g_height))
            end(adjustPoint(Pos(screen.right, move_across_5.end.y - 20), g_width, g_height))
            setStepByDistance(0.5F, 20F)
        }
        move_pattern.addMovement(move_down_5)

        move_pattern.setTouchEdgeToContinue(true)
        move_pattern.moveGroupToStart()
        move_pattern.calculateGroup()
        return listOf(move_pattern)
    }
}