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
    private val player_textures: HashMap<Sprites, TextureRegion> = HashMap()
    private val invader_textures: HashMap<Sprites, TextureRegion> = HashMap()
    private val projectile_textures: HashMap<ProjectileType, TextureRegion> = HashMap()

    /**
     * Loads textures used by the game
     */
    fun loadGameTextures() {
        val invader_tex = TextureAtlas(Gdx.files.internal("Sprites/Invaders.atlas"))
        invader_textures[Sprites.FIGHTER_1]     = invader_tex.findRegion("Bomber-1")
        invader_textures[Sprites.FIGHTER_2]     = invader_tex.findRegion("Bomber-2")
        invader_textures[Sprites.BOMBER_1]      = invader_tex.findRegion("Bomber-1")
        invader_textures[Sprites.BOMBER_2]      = invader_tex.findRegion("Bomber-2")
        invader_textures[Sprites.MOTHER_SHIP_1] = invader_tex.findRegion("Bomber-1")
        invader_textures[Sprites.MOTHER_SHIP_2] = invader_tex.findRegion("Bomber-2")

        projectile_textures[ProjectileType.PLAYER] = invader_tex.findRegion("Bomber-1")

        val player_tex = TextureAtlas(Gdx.files.internal("Sprites/Player.atlas"))
        player_textures[Sprites.PLAYER] = player_tex.findRegion("Player_Spaceship")

        textures[Sprites.BACKGROUND] = Texture("Background.png")
    }

    /**
     * Unloads all game textures
     */
    fun disposeGameTextures() {
        textures.forEach{ it.value.dispose() }
        textures.clear()
    }

    fun getTexture(type: Sprites): Texture {
        return if (textures[type] != null) textures[type]!!
        else Texture("Error.png")
    }

    fun getPlayerTexture() = player_textures[Sprites.PLAYER]!!

    fun getInvaderTexture(type: InvaderType, index: Int): TextureRegion {
        return when(type) {
            InvaderType.FIGHTER     -> listOf(invader_textures[Sprites.FIGHTER_1]!!, invader_textures[Sprites.FIGHTER_2]!!)
            InvaderType.BOMBER      -> listOf(invader_textures[Sprites.BOMBER_1]!!, invader_textures[Sprites.BOMBER_2]!!)
            InvaderType.MOTHER_SHIP -> listOf(invader_textures[Sprites.MOTHER_SHIP_1]!!, invader_textures[Sprites.MOTHER_SHIP_2]!!)
        }[index]
    }

    fun getProjectileTex(type: ProjectileType, index: Int): TextureRegion {
        return when(type) {
            ProjectileType.PLAYER -> listOf(projectile_textures[ProjectileType.PLAYER]!!)
        }[index]
    }

    fun nextTexture(invader: Invader): TextureRegion {
        //TODO: if there is ever more than 2 textures...
        if(invader.current_texture == 1) invader.current_texture = 0
        else invader.current_texture++
        return getInvaderTexture(invader.type, invader.current_texture)
    }

    fun disposeAll() {

    }
}
