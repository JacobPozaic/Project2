package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import jacob.pozaic.spaceinvaders.entity.Invader
import jacob.pozaic.spaceinvaders.entity.Player
import jacob.pozaic.spaceinvaders.entity.Projectile

private val invaders = Group()
private val player = Group()
private val projectiles = Group()

class Stage(viewport: FitViewport?, batch: SpriteBatch): Stage(viewport, batch) {
    init {
        addActor(invaders)
        addActor(player)
        addActor(projectiles)
    }

    fun addInvader(invader: Invader) {
        invaders.addActor(invader)
    }

    fun noInvaders() = !invaders.hasChildren()

    fun getInvaders() = invaders.children

    fun removeInvader(invader: Invader) {
        invaders.removeActor(invader)
    }

    fun addPlayer(p: Player) {
        player.addActor(p)
    }

    fun addProjectile(projectile: Projectile) {
        projectiles.addActor(projectile)
    }
}