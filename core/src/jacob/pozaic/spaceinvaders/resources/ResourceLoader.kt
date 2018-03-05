package jacob.pozaic.spaceinvaders.resources

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import jacob.pozaic.spaceinvaders.entity.InvaderType

//TODO: can use AssetManager
class ResourceLoader {
    // Textures used in the game
    private val textures: HashMap<Sprites, Texture> = HashMap()
    private val player_textures: ArrayList<TextureRegion> = ArrayList()
    private val invader_textures: HashMap<Sprites, ArrayList<TextureRegion>> = HashMap()
    private val projectile_textures: HashMap<ProjectileType, ArrayList<TextureRegion>> = HashMap()

    fun loadGameTextures() {
        val invader_tex = TextureAtlas(Gdx.files.internal("Sprites/Invaders.atlas"))
        val player_tex = TextureAtlas(Gdx.files.internal("Sprites/Player.atlas"))
        val projectile_tex = TextureAtlas(Gdx.files.internal("Sprites/Projectiles.atlas"))

        invader_textures[Sprites.FIGHTER] = ArrayList()
        invader_textures[Sprites.BOMBER] = ArrayList()
        invader_textures[Sprites.MOTHER_SHIP] = ArrayList()

        invader_textures[Sprites.FIGHTER]!!.add(invader_tex.findRegion("Fighter-1"))
        invader_textures[Sprites.FIGHTER]!!.add(invader_tex.findRegion("Fighter-2"))
        invader_textures[Sprites.BOMBER]!!.add(invader_tex.findRegion("Bomber-1"))
        invader_textures[Sprites.BOMBER]!!.add(invader_tex.findRegion("Bomber-2"))
        invader_textures[Sprites.MOTHER_SHIP]!!.add(invader_tex.findRegion("Bomber-1"))
        invader_textures[Sprites.MOTHER_SHIP]!!.add(invader_tex.findRegion("Bomber-2"))

        player_textures.add(player_tex.findRegion("Player-1"))

        projectile_textures[ProjectileType.PLAYER] = ArrayList()
        projectile_textures[ProjectileType.FIGHTER] = ArrayList()
        projectile_textures[ProjectileType.BOMBER] = ArrayList()
        projectile_textures[ProjectileType.MOTHER_SHIP] = ArrayList()

        projectile_textures[ProjectileType.PLAYER]!!.add(projectile_tex.findRegion("Projectile-Player-1"))
        projectile_textures[ProjectileType.PLAYER]!!.add(projectile_tex.findRegion("Projectile-Player-2"))

        //TODO: invader projectile textures

        textures[Sprites.BACKGROUND] = Texture("Background.png")
    }

    fun getTexture(type: Sprites): Texture {
        return if (textures[type] != null) textures[type]!!
        else Texture("Error.png")
    }

    fun getPlayerTexture(index: Int): TextureRegion {
        return player_textures[index]
    }

    fun getInvaderTexture(type: InvaderType, index: Int): TextureRegion {
        return when(type) {
            InvaderType.FIGHTER     -> invader_textures[Sprites.FIGHTER]!![index % invader_textures[Sprites.FIGHTER]!!.size]
            InvaderType.BOMBER      -> invader_textures[Sprites.BOMBER]!![index % invader_textures[Sprites.BOMBER]!!.size]
            InvaderType.MOTHER_SHIP -> invader_textures[Sprites.MOTHER_SHIP]!![index % invader_textures[Sprites.MOTHER_SHIP]!!.size]
        }
    }

    fun getProjectileTex(type: ProjectileType, index: Int): TextureRegion {
        return projectile_textures[type]!![index % projectile_textures[type]!!.size]
    }
}
