package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas

internal val invader_tex = TextureAtlas(Gdx.files.internal("Sprites/Invaders.atlas"))
internal val player_tex = TextureAtlas(Gdx.files.internal("Sprites/Player.atlas"))
internal val projectile_tex = TextureAtlas(Gdx.files.internal("Sprites/Projectiles.atlas"))

fun loadGameTextures(entity_params: HashMap<EntityType, EntityParams>) {
    entity_params[EntityType.FIGHTER]!!.textures.add(invader_tex.findRegion("Fighter-1"))
    entity_params[EntityType.FIGHTER]!!.textures.add(invader_tex.findRegion("Fighter-2"))
    entity_params[EntityType.BOMBER]!!.textures.add(invader_tex.findRegion("Bomber-1"))
    entity_params[EntityType.BOMBER]!!.textures.add(invader_tex.findRegion("Bomber-2"))
    entity_params[EntityType.MOTHER_SHIP]!!.textures.add(invader_tex.findRegion("Bomber-1")) //TODO: mother ship tex
    entity_params[EntityType.MOTHER_SHIP]!!.textures.add(invader_tex.findRegion("Bomber-2"))

    entity_params[EntityType.EXPLODE]!!.textures.add(invader_tex.findRegion("Explode-1"))
    entity_params[EntityType.EXPLODE]!!.textures.add(invader_tex.findRegion("Explode-2"))

    entity_params[EntityType.PLAYER]!!.textures.add(player_tex.findRegion("Player-1"))

    entity_params[EntityType.PLAYER_PROJECTILE]!!.textures.add(projectile_tex.findRegion("Projectile-Player-1"))
    entity_params[EntityType.PLAYER_PROJECTILE]!!.textures.add(projectile_tex.findRegion("Projectile-Player-2"))

    //TODO: invader projectile textures
    entity_params[EntityType.FIGHTER_PROJECTILE]!!.textures.add(projectile_tex.findRegion("Projectile-Player-1"))
    entity_params[EntityType.FIGHTER_PROJECTILE]!!.textures.add(projectile_tex.findRegion("Projectile-Player-2"))
    entity_params[EntityType.BOMBER_PROJECTILE]!!.textures.add(projectile_tex.findRegion("Projectile-Player-1"))
    entity_params[EntityType.BOMBER_PROJECTILE]!!.textures.add(projectile_tex.findRegion("Projectile-Player-2"))
    entity_params[EntityType.MOTHER_SHIP_PROJECTILE]!!.textures.add(projectile_tex.findRegion("Projectile-Player-1"))
    entity_params[EntityType.MOTHER_SHIP_PROJECTILE]!!.textures.add(projectile_tex.findRegion("Projectile-Player-2"))
}
