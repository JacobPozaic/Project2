package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import jacob.pozaic.spaceinvaders.entity.Invader
import jacob.pozaic.spaceinvaders.entity.Player
import jacob.pozaic.spaceinvaders.entity.Projectile

private val invaders_gp = Group()
private val player_gp = Group()
private val projectiles_gp = Group()

class Stage(viewport: FitViewport?, batch: SpriteBatch): Stage(viewport, batch) {
    init {
        addActor(invaders_gp)
        addActor(player_gp)
        addActor(projectiles_gp)
    }

    fun addInvader(invader: Invader) {
        invaders_gp.addActor(invader)
    }

    fun getInvaders() = invaders_gp.children!!

    fun removeInvader(invader: Invader) {
        invaders_gp.removeActor(invader)
    }

    fun addPlayer(p: Player) {
        player_gp.addActor(p)
    }

    fun addProjectile(projectile: Projectile) {
        projectiles_gp.addActor(projectile)
    }
}