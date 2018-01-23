package jacob.pozaic.spaceinvaders

import com.badlogic.gdx.graphics.Texture

/**
 * Created by Jacob on 1/22/2018.
 */

class ResourceLoader {
    private var player_texture: Texture? = null
    private var invader1_texture: Texture? = null
    private var invader2_texture: Texture? = null

    fun LoadGameTextures() {
        player_texture = Texture("Sprites/Player_Spaceship.png")
        //TODO: other game resources
    }

    fun DisposeGameTextures() {
        player_texture!!.dispose()
    }

    fun GetPlayer(): Texture? {
        return player_texture
    }
}
