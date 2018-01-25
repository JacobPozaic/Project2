package jacob.pozaic.spaceinvaders

import com.badlogic.gdx.graphics.Texture
import jacob.pozaic.spaceinvaders.Sprites.*

class ResourceLoader {
    // Textures used in the game
    private var textures: HashMap<Sprites, Texture> = HashMap()

    /**
     * Loads textures used by the game
     */
    fun LoadGameTextures() {
        textures.put(BACKGROUND, Texture("Background.png"))
        textures.put(PLAYER, Texture("Sprites/Player_Spaceship.png"))
        textures.put(FIGHTER_1, Texture("Sprites/Invader_1_Temp.png"))
        //TODO: new textures
        textures.put(FIGHTER_2, Texture("Sprites/Invader_1_Temp.png"))
        textures.put(BOMBER_1, Texture("Sprites/Invader_1_Temp.png"))
        textures.put(BOMBER_2, Texture("Sprites/Invader_1_Temp.png"))
        textures.put(MOTHER_SHIP_1, Texture("Sprites/Invader_1_Temp.png"))
        textures.put(MOTHER_SHIP_2, Texture("Sprites/Invader_1_Temp.png"))
    }

    fun DisposeGameTextures() {
        textures.forEach{ it.value.dispose() }
        textures.clear()
    }

    fun getTexture(type: Sprites): Texture {
        return if (textures[type] != null) textures[type]!!
        else Texture("Error.png") //TODO: create this image
    }

    fun getInvaderTextures(type: InvaderType): List<Texture> {
        return when(type) {
            InvaderType.FIGHTER -> listOf(textures[FIGHTER_1]!!, textures[FIGHTER_2]!!)
            InvaderType.BOMBER -> listOf(textures[BOMBER_1]!!, textures[BOMBER_2]!!)
            InvaderType.MOTHER_SHIP -> listOf(textures[MOTHER_SHIP_1]!!, textures[MOTHER_SHIP_2]!!)
        }
    }
}
