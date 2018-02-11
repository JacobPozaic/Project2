package jacob.pozaic.spaceinvaders.resources

import com.badlogic.gdx.graphics.Texture
import jacob.pozaic.spaceinvaders.entity.InvaderType
import jacob.pozaic.spaceinvaders.resources.Sprites.*

class ResourceLoader {
    // Textures used in the game
    private var textures: HashMap<Sprites, Texture> = HashMap()

    /**
     * Loads textures used by the game
     */
    fun loadGameTextures() {
        textures[BACKGROUND]    = Texture("Background.png")
        textures[PLAYER]        = Texture("Sprites/Player_Spaceship.png")
        textures[FIGHTER_1]     = Texture("Sprites/Invader_1_Temp.png")
        //TODO: new textures
        textures[FIGHTER_2]     = Texture("Sprites/Invader_1_Temp.png")
        textures[BOMBER_1]      = Texture("Sprites/Invader_1_Temp.png")
        textures[BOMBER_2]      = Texture("Sprites/Invader_1_Temp.png")
        textures[MOTHER_SHIP_1] = Texture("Sprites/Invader_1_Temp.png")
        textures[MOTHER_SHIP_2] = Texture("Sprites/Invader_1_Temp.png")

        textures[PLAYER_PROJECTILE] = Texture("Sprites/Invader_1_Temp.png")
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

    fun getInvaderTextures(type: InvaderType): List<Texture> {
        return when(type) {
            InvaderType.FIGHTER     -> listOf(textures[FIGHTER_1]!!, textures[FIGHTER_2]!!)
            InvaderType.BOMBER      -> listOf(textures[BOMBER_1]!!, textures[BOMBER_2]!!)
            InvaderType.MOTHER_SHIP -> listOf(textures[MOTHER_SHIP_1]!!, textures[MOTHER_SHIP_2]!!)
        }
    }

    fun disposeAll() {

    }
}
