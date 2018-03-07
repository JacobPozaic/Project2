package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.TimeUtils
import jacob.pozaic.spaceinvaders.entity.Invader
import jacob.pozaic.spaceinvaders.entity.InvaderType
import jacob.pozaic.spaceinvaders.entity.Projectile
import jacob.pozaic.spaceinvaders.resources.ProjectileType
import java.util.*

private val projectiles_destroyed = ArrayList<Projectile>()

internal fun logicLoop() {
    // Update player position if using movement arrows
    if(draw_arrows){
        val x_pos = player!!.getCenter().x
        if(arrow_left!!.isPressed && x_pos > screen.left){
            player!!.step(-Gdx.graphics.deltaTime * player_speed)
        } else if(arrow_right!!.isPressed && x_pos < screen.right) {
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
    val frame_delta = Gdx.graphics.deltaTime
    // Update the wave and move all Invaders
    WM!!.update(frame_delta)

    // Invader shoot projectiles
    val num_invaders = game!!.getInvaders().size
    if(num_invaders > 0) {
        val enemy_projectiles = game!!.getProjectiles().filter { p -> p.type != ProjectileType.PLAYER }.size
        if(enemy_projectiles < max_enemy_projectiles) {
            if((TimeUtils.timeSinceMillis(enemy_last_shoot_time) > min_enemy_shoot_delay && enemy_shoot_chance * frame_delta >= rand.nextInt(100))
                    || TimeUtils.timeSinceMillis(enemy_last_shoot_time) > max_enemy_shoot_delay) {
                enemy_last_shoot_time = TimeUtils.millis()
                val invader_shooting = game!!.getInvaders()[rand.nextInt(num_invaders)]
                val pos = invader_shooting.getCenter()
                val projectile_type = getProjectileType(invader_shooting.type)
                val tex = RL.getProjectileTex(projectile_type, 0)
                game!!.addProjectile(Projectile(tex, pos.x, pos.y, texture_scale, texture_scale, projectile_type, 200))
                println("yes")
            }
        }
    }

    // TODO: sounds

    // Move the projectiles
    game!!.getProjectiles().forEach { p -> p.step(getProjectileSpeed(p.type) * frame_delta) }

    // Check for any collisions in projectiles
    game!!.getProjectiles().forEach nextProjectile@{p ->
        when {
            p.type != ProjectileType.PLAYER ->
                when {
                    p.y <= player!!.y -> projectiles_destroyed.add(p)           // Invader projectile left screen
                    p.collidesWith(player!!) -> {                               // Invader projectile hit player
                        projectiles_destroyed.add(p)
                        game!!.playerLoseLife()
                    } else -> game!!.getProjectiles()                           // Projectile collides with another projectile
                            .filter { p_player ->  p_player.type == ProjectileType.PLAYER }
                            .forEach { p_player -> if (p.collidesWith(p_player)){
                                projectiles_destroyed.add(p)
                                projectiles_destroyed.add(p_player)
                            }
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

private fun getProjectileType(type: InvaderType): ProjectileType {
    return when(type) {
        InvaderType.FIGHTER -> ProjectileType.FIGHTER
        InvaderType.BOMBER -> ProjectileType.BOMBER
        InvaderType.MOTHER_SHIP -> ProjectileType.MOTHER_SHIP
    }
}

private fun getProjectileSpeed(type: ProjectileType): Float {
    return when(type) {
        ProjectileType.PLAYER -> player_projectile_speed
        ProjectileType.FIGHTER -> fighter_projectile_speed
        ProjectileType.BOMBER -> bomber_projectile_speed
        ProjectileType.MOTHER_SHIP -> mother_ship_projectile_speed
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