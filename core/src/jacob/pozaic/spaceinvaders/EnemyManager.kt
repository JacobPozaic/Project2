package jacob.pozaic.spaceinvaders

/**
 * Created by Jacob on 1/24/2018.
 */
class EnemyManager() {
    private var invaders = ArrayList<Invader>()
    init {
       //Todo: control enemy AI
    }

    fun step() {
        //TODO: if time has passed then move all enemies
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