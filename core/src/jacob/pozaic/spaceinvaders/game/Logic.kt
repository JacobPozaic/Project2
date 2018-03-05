package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.TimeUtils
import jacob.pozaic.spaceinvaders.entity.Invader
import jacob.pozaic.spaceinvaders.entity.InvaderType
import jacob.pozaic.spaceinvaders.entity.Projectile
import jacob.pozaic.spaceinvaders.resources.ProjectileType

private val projectiles_destroyed = ArrayList<Projectile>()

internal fun logicLoop() {
    // Update player position if using movement arrows
    if(draw_arrows){
        if(arrow_left!!.isPressed){
            player!!.step(-Gdx.graphics.deltaTime * player_speed)
        } else if(arrow_right!!.isPressed) {
            player!!.step(Gdx.graphics.deltaTime * player_speed)
        }
    }

    // Determine what logic path to use
    when(game_state) {
        GameState.SHOW_GAME_PLAY  -> gamePlayLogic()
        GameState.SHOW_GAME_PAUSE -> gamePauseLogic()
        GameState.SHOW_GAME_OVER  -> gameOverLogic()
    }
}

private fun gamePlayLogic() {
    // Update the wave and move all Invaders
    WM!!.update(Gdx.graphics.deltaTime)

    // TODO: sounds

    // Move the players projectile if one exists
    game!!.getProjectiles().filter { p -> p.type == ProjectileType.PLAYER }
            .forEach { player_p -> player_p.step(player_projectile_speed * Gdx.graphics.deltaTime) }

    // TODO: step invader projectiles

    // Check for any collisions in projectiles
    game!!.getProjectiles().forEach nextProjectile@{p ->
        when {
            p.type != ProjectileType.PLAYER ->
                when {
                    p.y <= player!!.y -> projectiles_destroyed.add(p)           // Invader projectile left screen
                    p.collidesWith(player!!) -> {                               // Invader projectile hit player
                        projectiles_destroyed.add(p)
                        game!!.playerLoseLife()
                    } else -> game!!.getProjectiles().forEach { p_invader ->    // Projectile collides with another projectile
                        if (p.collidesWith(p_invader))
                            projectiles_destroyed.addAll(arrayOf(p, p_invader))
                    }
                }
            p.y >= screen.top -> projectiles_destroyed.add(p)                   // Player projectile left screen
            else -> game!!.getInvaders().forEach {invader ->                    // Invader collision
                if(p.collidesWith(invader)) {
                    projectiles_destroyed.add(p)
                    game!!.removeInvader(invader)
                    val score = addScore(invader.type)
                    player_score += score
                    //TODO: show toast with score, and invader death animation
                    return@nextProjectile
                }
            }
        }
    }

    // Remove the destroyed projectiles and then clear the list
    projectiles_destroyed.filter { p_destroyed -> game!!.getProjectiles().contains(p_destroyed) }
            .forEach {p_to_remove -> game!!.removeProjectile(p_to_remove) }
    projectiles_destroyed.clear()

    if(projectilesUpdated) {
        stg_game.actors.filter { actor -> actor is Projectile }
                .forEach { actor -> actor.remove() }
        game!!.getProjectiles().forEach { projectile -> stg_game.addActor(projectile) }
        projectilesUpdated = false
    }

    if(invadersUpdated) {
        stg_game.actors.filter { actor -> actor is Invader }
                .forEach { actor -> actor.remove() }
        game!!.getInvaders().forEach { invader -> stg_game.addActor(invader) }
        invadersUpdated = false
    }

    WM!!.createWave()

    // Lose condition
    if(game_over) gameOver()

    stg_game.act(Gdx.graphics.deltaTime)
}

private fun gamePauseLogic() {
    //TODO:
}

private fun gameOverLogic() {
    //TODO:
}

private fun addScore(type: InvaderType): Int {
    return when(type) {
        InvaderType.FIGHTER -> 100
        InvaderType.BOMBER -> 150
        InvaderType.MOTHER_SHIP -> 200
    }
}

private var time_since_last_shot = 0L

// Player shoots a projectile
fun playerShoot(){
    if(TimeUtils.timeSinceMillis(time_since_last_shot) >= shoot_delay){
        time_since_last_shot = TimeUtils.millis()
        val pos = player!!.getCenter()
        game!!.addProjectile(Projectile(RL.getProjectileTex(ProjectileType.PLAYER, 0), pos.x, pos.y, texture_scale, texture_scale, ProjectileType.PLAYER, 200))
    }
}