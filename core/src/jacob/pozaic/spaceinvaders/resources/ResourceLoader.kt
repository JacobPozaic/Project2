package jacob.pozaic.spaceinvaders.resources

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import jacob.pozaic.spaceinvaders.entity.Invader
import jacob.pozaic.spaceinvaders.entity.InvaderType

class ResourceLoader {
    // Textures used in the game
    private val textures: HashMap<Sprites, Texture> = HashMap()
    private val player_textures: ArrayList<TextureRegion> = ArrayList()
    private val invader_textures: HashMap<Sprites, ArrayList<TextureRegion>> = HashMap()
    private val projectile_textures: HashMap<ProjectileType, TextureRegion> = HashMap()

    fun loadGameTextures() {
        val invader_tex = TextureAtlas(Gdx.files.internal("Sprites/Invaders.atlas"))
        val player_tex = TextureAtlas(Gdx.files.internal("Sprites/Player.atlas"))

        invader_textures[Sprites.FIGHTER] = ArrayList()
        invader_textures[Sprites.BOMBER] = ArrayList()
        invader_textures[Sprites.MOTHER_SHIP] = ArrayList()

        invader_textures[Sprites.FIGHTER]!!.add(invader_tex.findRegion("Bomber-1"))
        invader_textures[Sprites.FIGHTER]!!.add(invader_tex.findRegion("Bomber-2"))
        invader_textures[Sprites.BOMBER]!!.add(invader_tex.findRegion("Bomber-1"))
        invader_textures[Sprites.BOMBER]!!.add(invader_tex.findRegion("Bomber-2"))
        invader_textures[Sprites.MOTHER_SHIP]!!.add(invader_tex.findRegion("Bomber-1"))
        invader_textures[Sprites.MOTHER_SHIP]!!.add(invader_tex.findRegion("Bomber-2"))

        player_textures.add(player_tex.findRegion("Player-1"))

        projectile_textures[ProjectileType.PLAYER] = invader_tex.findRegion("Bomber-1")

        textures[Sprites.BACKGROUND] = Texture("Background.png")
    }

    fun disposeGameTextures() {
        textures.forEach{ it.value.dispose() }
        textures.clear()
    }

    fun getTexture(type: Sprites): Texture {
        return if (textures[type] != null) textures[type]!!
        else Texture("Error.png")
    }

    fun getPlayerTexture(index: Int): TextureRegion {
        return player_textures[index]
    }

    fun getInvaderTexture(type: InvaderType, index: Int): TextureRegion {
        val i = index % invader_textures[Sprites.FIGHTER]!!.size
        return when(type) {
            InvaderType.FIGHTER     -> invader_textures[Sprites.FIGHTER]!![i]
            InvaderType.BOMBER      -> invader_textures[Sprites.BOMBER]!![i]
            InvaderType.MOTHER_SHIP -> invader_textures[Sprites.MOTHER_SHIP]!![i]
        }
    }

    fun getProjectileTex(type: ProjectileType, index: Int): TextureRegion {
        return when(type) {
            ProjectileType.PLAYER -> listOf(projectile_textures[ProjectileType.PLAYER]!!)
        }[index]
    }

    fun disposeAll() {

    }
}
