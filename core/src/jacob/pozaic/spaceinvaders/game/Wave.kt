package jacob.pozaic.spaceinvaders.game

import jacob.pozaic.spaceinvaders.ai.MoveGroup

class Wave(private val game: SpaceInvaders, private val move_groups: ArrayList<MoveGroup>){
    fun update(delta: Float){
        move_groups.forEach { it.move(delta) }
    }

    fun hasInvaders(): Boolean {
        var count = 0
        move_groups.forEach {group ->
            count += game.getInvaders().filter {invaders ->
                invaders.move_group != group
            }.size
        }
        return count != 0
    }
}