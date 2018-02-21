package jacob.pozaic.spaceinvaders.game

import jacob.pozaic.spaceinvaders.ai.MoveGroup
import jacob.pozaic.spaceinvaders.entity.Invader

class Wave(private val move_groups: ArrayList<MoveGroup>, private val invaders: ArrayList<Invader>){
    fun update(delta: Float){
        move_groups.forEach { it.move(delta) }

        //TODO: cycle texture
    }

    fun removeInvader(invader: Invader) { //TODO: implement
        invaders.remove(invader)
        move_groups.forEach { it.removeInvader(invader) }
    }

    fun hasInvaders(): Boolean = invaders.size > 0
}