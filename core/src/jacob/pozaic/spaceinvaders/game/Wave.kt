package jacob.pozaic.spaceinvaders.game

import jacob.pozaic.spaceinvaders.ai.MovePattern
import jacob.pozaic.spaceinvaders.entity.Invader

class Wave(private val move_patterns: ArrayList<MovePattern>, private val invaders: ArrayList<Invader>){
    fun update(delta: Float){
        move_patterns.forEach { it.move(delta) }

        //TODO: cycle texture
    }

    fun removeInvader(invader: Invader) { //TODO: implement
        invaders.remove(invader)
        move_patterns.forEach { it.removeInvader(invader) }
    }

    fun hasInvaders(): Boolean = invaders.size > 0
}