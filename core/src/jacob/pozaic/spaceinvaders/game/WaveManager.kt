package jacob.pozaic.spaceinvaders.game

import jacob.pozaic.spaceinvaders.ai.ClassicMovement
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
                1 -> if(play_classic) classicWave() else defaultWave()
                2 -> if(play_classic) classicWave() else defaultWave()
                3 -> if(play_classic) classicWave() else defaultWave()
                4 -> if(play_classic) classicWave() else defaultWave()
                5 -> if(play_classic) classicWave() else defaultWave()
                else -> return // This will never happen
            }
        }
    }

    private fun classicWave(): List<ClassicMovement> {
        val group = ClassicMovement(game)

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
                val invader = Invader(invader_type, posX, posY, texture_scale, group)
                game.addInvader(invader)
            }
        }

        return listOf(group)
    }

    private fun defaultWave(): List<MoveGroup> {
        // Create the MoveGroup for the wave
        val move_pattern_1 = MoveGroup(game)
        val move_pattern_2 = MoveGroup(game)
        val move_pattern_3 = MoveGroup(game)
        val move_pattern_4 = MoveGroup(game)

        val patterns = listOf(move_pattern_1, move_pattern_2, move_pattern_3, move_pattern_4)

        patterns.forEach { pattern ->
            var invader_type = EntityType.FIGHTER
            for(y in 0..1) {
                val tex_default = game.entity(invader_type).textures[0]
                val texture_width = tex_default.regionWidth * texture_scale
                val texture_height = tex_default.regionHeight * texture_scale

                for(x in 0..1) {
                    // Calculate the position of the new Invader in scaled space
                    val posX = (texture_width * x) + (spacing_offset * x)
                    val posY = (texture_height * y) + (spacing_offset * 0.5F * y)
                    // Create a new invader
                    val invader = Invader(invader_type, posX, posY, texture_scale, pattern)
                    invader.can_touch_ground = false
                    game.addInvader(invader)
                }
            }
            pattern.calculateGroup()
        }

        // Add components of the MoveGroup
        // First group
        val move_across_1 = LinearMove()
        with(move_across_1) {
            start(Pos(getOffScreenLeft(move_pattern_1), 350F))
            end(Pos(getOffScreenRight(move_pattern_1), 350F))
            setContinious(20F)
        }
        move_pattern_1.addMovement(move_across_1)
        move_pattern_1.moveGroupToStart()

        // Second group
        val move_across_2 = LinearMove()
        with(move_across_2) {
            start(Pos(getOffScreenRight(move_pattern_2), 325F))
            end(Pos(getOffScreenLeft(move_pattern_2), 325F))
            setContinious(30F)
        }
        move_pattern_2.addMovement(move_across_2)
        move_pattern_2.moveGroupToStart()

        // Third group
        val wait_3 = LinearMove()
        with(wait_3) {
            start(Pos(getOffScreenLeft(move_pattern_3), 300F))
            end(Pos(getOffScreenRight(move_pattern_3), 300F))
            setStationary(3F)
        }
        val move_across_3 = LinearMove()
        with(move_across_3) {
            start(wait_3.start)
            end(wait_3.end)
            setStepByDistance(1F, 35F)
        }
        move_pattern_3.addMovement(wait_3)
        move_pattern_3.addMovement(move_across_3)
        move_pattern_3.moveGroupToStart()

        // Fourth group
        val wait_4 = LinearMove()
        with(wait_4) {
            start(Pos(getOffScreenRight(move_pattern_4), 250F))
            end(Pos(getOffScreenLeft(move_pattern_4), 250F))
            setStationary(5F)
        }
        val move_across_4 = LinearMove()
        with(move_across_4) {
            start(wait_4.start)
            end(wait_4.end)
            setContinious(40F)
        }
        move_pattern_4.addMovement(wait_4)
        move_pattern_4.addMovement(move_across_4)
        move_pattern_4.moveGroupToStart()

        return patterns
    }

    fun reduceMoveDelay() {
        if(current_wave != null) current_wave!!.forEach { wave -> wave.decrementStepDelay() }
    }

    private fun getOffScreenLeft(move_pattern: MoveGroup) = screen.left - move_pattern.getGroupWidth() - 20
    private fun getOffScreenRight(move_pattern: MoveGroup) = screen.right + move_pattern.getGroupWidth() + 20
}