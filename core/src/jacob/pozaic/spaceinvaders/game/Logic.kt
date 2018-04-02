package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.TimeUtils
import jacob.pozaic.spaceinvaders.entity.Explode
import jacob.pozaic.spaceinvaders.entity.Invader
import jacob.pozaic.spaceinvaders.entity.Projectile
import java.util.*

// Stores a list of removed projectiles so that when checking collisions all projectiles are still in-tact until iterated through
internal val projectiles_destroyed = ArrayList<Projectile>()

// The time that the invaders and player last shot
internal var enemy_last_shoot_time = 0L
internal var player_last_shot_time = 0L

/**
 * Called every frame before render
 */
internal fun logicLoop() {
    if(shouldRestart) game_man!!.restartGame()

    // Update player position if using movement arrows
    if(draw_arrows){
        val x_pos = player!!.getCenter().x
        val distance = game!!.entity(EntityType.PLAYER).move_Speed * Gdx.graphics.deltaTime
        if(arrow_left!!.isPressed && x_pos > screen.left) player!!.step(-distance)
        else if(arrow_right!!.isPressed && x_pos < screen.right) player!!.step(distance)
    }

    // Determine what logic path to use
    when(game_state) {
        GameState.SHOW_GAME_PLAY  -> gamePlayLogic()
    }
}

/**
 * Called when the game is playing (not in menu)
 */
private fun gamePlayLogic() {
    // Gets the time since the last frame
    val frame_delta = Gdx.graphics.deltaTime

    // TODO: sounds

    // Move the projectiles
    game!!.getProjectiles().forEach { p -> p.step(game!!.entity(p.type).move_Speed * frame_delta) }

    // Check for any collisions in projectiles
    game!!.getProjectiles().forEach nextProjectile@{p ->
        when {
            p.type != EntityType.PLAYER_PROJECTILE ->
                when {
                    p.y <= player!!.y -> projectiles_destroyed.add(p)           // Invader projectile left screen
                    p.collidesWith(player!!) -> {                               // Invader projectile hit player
                        projectiles_destroyed.add(p)
                        game!!.playerLoseLife()
                    } else -> game!!.getProjectiles()                           // Projectile collides with another projectile
                            .filter { p_player ->  p_player.type == EntityType.PLAYER_PROJECTILE }
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
                    val score = game!!.entity(invader.type).score_value
                    player_score += score
                    stg_game.addActor(Explode(score, invader.getCenter(), invader.scaleX))
                    return@nextProjectile
                }
            }
        }
    }

    // Remove the destroyed projectiles and then clear the list
    projectiles_destroyed.filter { p_destroyed -> game!!.getProjectiles().contains(p_destroyed) }
            .forEach {p_to_remove -> game!!.removeProjectile(p_to_remove) }
    projectiles_destroyed.clear()

    // Update the list of projectiles
    if(projectilesUpdated) {
        stg_game.actors.filter { actor -> actor is Projectile }
                .forEach { actor -> actor.remove() }
        game!!.getProjectiles().forEach { projectile -> stg_game.addActor(projectile) }
        projectilesUpdated = false
    }

    // Update the list of invaders
    if(invadersUpdated) {
        stg_game.actors.filter { actor -> actor is Invader }.forEach { actor -> actor.remove() }
        game!!.getInvaders().forEach { invader -> stg_game.addActor(invader) }

        val size = stg_game.actors.filter { actor -> actor is Invader }.size - game!!.getInvaders().size
        for(i in 0..size) WM!!.reduceMoveDelay()

        invadersUpdated = false
    }

    // Update the wave and move all Invaders, done after updating what invaders are on screen so prevent movement being affect by removed invaders
    WM!!.update()

    // Invader shoot projectiles
    val num_invaders = game!!.getInvaders().size
    if(num_invaders > 0) {
        // Check if the maximum number of invader projectiles has been reached
        if(game!!.getProjectiles().filter { p -> p.type != EntityType.PLAYER_PROJECTILE }.size < max_enemy_projectiles) {
            // Check if the time since the last enemy shot is greater than the minimum shoot delay and give chance to an enemy firing, or if the time since the last shot is greater than the max delay then fire
            if((TimeUtils.timeSinceMillis(enemy_last_shoot_time) > min_enemy_shoot_delay && enemy_shoot_chance * frame_delta >= rand.nextInt(100))
                    || TimeUtils.timeSinceMillis(enemy_last_shoot_time) > max_enemy_shoot_delay) {
                enemy_last_shoot_time = TimeUtils.millis()
                // Randomly choose an invader to shoot the projectile
                val invader_shooting = game!!.getInvaders()[rand.nextInt(num_invaders)]
                val pos = invader_shooting.getCenter()
                val projectile_type = game!!.entity(invader_shooting.type).projectile_type
                game!!.addProjectile(Projectile(projectile_type!!, pos.x, pos.y, texture_scale))
            }
        }
    }

    // Update score
    if(score_label != null) score_label!!.setText("Score: $player_score")

    // Lose condition
    if(game_over) gameOver()

    // Get each actor on the stage to act
    stg_game.act(Gdx.graphics.deltaTime)
}

/**
 * Makes the player shoot a projectile if the last shot was long enough
 */
fun playerShoot(){
    if(TimeUtils.timeSinceMillis(player_last_shot_time) >= shoot_delay){
        player_last_shot_time = TimeUtils.millis()
        val pos = player!!.getCenter()
        game!!.addProjectile(Projectile(EntityType.PLAYER_PROJECTILE, pos.x, pos.y, texture_scale))
    }
}