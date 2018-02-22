package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.Gdx
import jacob.pozaic.spaceinvaders.entity.Invader
import jacob.pozaic.spaceinvaders.entity.Projectile
import jacob.pozaic.spaceinvaders.resources.ProjectileType

// A list of each projectile currently on screen
private val projectiles = ArrayList<Projectile>()

// If the player has shot, this will store the entity for the projectile
private var player_projectile: Projectile? = null

private val projectiles_destroyed = ArrayList<Projectile>()

internal fun logicLoop() {
    // Determine what logic path to use
    when(game_state) {
        GameState.SHOW_GAME_PLAY  -> gamePlayLogic()
        GameState.SHOW_GAME_PAUSE -> gamePauseLogic()
        GameState.SHOW_GAME_OVER  -> gameOverLogic()
    }
}

private fun gamePlayLogic() {
    // Lose condition
    if (game_over) gameOver()

    // TODO: sounds

    // Update locations
    //TODO: fix this
    // Move the players projectile if one exists
    if(player_projectile != null) {
        player_projectile!!.step(player_projectile_speed * Gdx.graphics.deltaTime)
    }

    // TODO: step invader projectiles

    // Check for any collisions in projectiles
    projectiles.forEach nextProjectile@{p ->
        //TODO collision size
        when {
            p.type != ProjectileType.PLAYER ->
                when {
                    p.y <= player!!.y -> projectiles_destroyed.add(p)   // Invader projectile left screen
                    p.collidesWith(player!!) -> {                       // Invader projectile hit player
                        projectiles_destroyed.add(p)
                        // TODO: player loses life
                    } else -> projectiles.forEach { p_invader ->        // Projectile collides with another projectile
                    if (p.collidesWith(p_invader))
                        projectiles.addAll(arrayOf(p, p_invader))
                }
                }
            p.y >= screen.top -> projectiles_destroyed.add(p)    // Player projectile left screen
            else -> stg_game.getInvaders().forEach {invader ->          // Invader collision
                val invader = invader as Invader
                if(p.collidesWith(invader)) {
                    projectiles_destroyed.add(p)
                    stg_game.removeInvader(invader)
                    WM.removeInvader(invader)
                    // TODO: add score
                    return@nextProjectile
                }
            }
        }
    }
    // Remove the destroyed projectiles and then clear the list
    projectiles_destroyed.forEach {
        if(projectiles.contains(it)) {
            if (it.type == ProjectileType.PLAYER)
                player_projectile = null
            projectiles.remove(it)
        }
    }
    projectiles_destroyed.clear()

    stg_game.act(Gdx.graphics.deltaTime)
}

private fun gamePauseLogic() {
    //TODO:
}

private fun gameOverLogic() {
    //TODO:
}

//TODO: deal with this
// Player shoots a projectile
fun playerShoot(){
    if(player_projectile == null) {
        val pos = player!!.getCenter()
        player_projectile = Projectile(RL.getProjectileTex(ProjectileType.PLAYER, 0), pos.x, pos.y, texture_scale, texture_scale, ProjectileType.PLAYER)
        projectiles.add(player_projectile!!)
    }
}