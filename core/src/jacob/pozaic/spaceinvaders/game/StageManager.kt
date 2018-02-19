package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import jacob.pozaic.spaceinvaders.input.GameInputHandler
import jacob.pozaic.spaceinvaders.input.GameOverInputHandler
import jacob.pozaic.spaceinvaders.input.PauseMenuInputHandler

internal val batch = SpriteBatch()
internal val stg_start = Stage(viewport, batch)
internal val stg_options = Stage(viewport, batch)
internal val stg_game = jacob.pozaic.spaceinvaders.game.Stage(viewport, batch)

internal fun startMenu() {
    // Define the UI components
    val table = Table()
    table.setFillParent(true)

    val btn_font = BitmapFont()
    val btn_skin = Skin()
    btn_skin.addRegions(TextureAtlas(Gdx.files.internal("GUI/Buttons.atlas")))

    val btn_start_style = TextButton.TextButtonStyle()
    with(btn_start_style) {
        font = btn_font
        up = btn_skin.getDrawable("button-up")
        down = btn_skin.getDrawable("button-down")
        checked = btn_skin.getDrawable("button-up")
    }

    val start_button = TextButton("Start Game", btn_start_style)
    start_button.addListener(object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            startGame()
        }
    })
    table.add(start_button).width(300F).height(100F).pad(1F)

    val btn_option_style = TextButton.TextButtonStyle()
    with(btn_option_style) {
        font = btn_font
        up = btn_skin.getDrawable("button-up")
        down = btn_skin.getDrawable("button-down")
        checked = btn_skin.getDrawable("button-up")
    }
    val option_button = TextButton("Options", btn_option_style)
    option_button.addListener(object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            startMenuOptions()
        }
    })
    table.row()
    table.add(option_button).width(300F).height(100F).pad(1F)

    val btn_quit_style = TextButton.TextButtonStyle()
    with(btn_quit_style) {
        font = btn_font
        up = btn_skin.getDrawable("button-up")
        down = btn_skin.getDrawable("button-down")
        checked = btn_skin.getDrawable("button-up")
    }
    val quit_button = TextButton("Quit", btn_quit_style)
    quit_button.addListener(object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            RL.disposeAll()
            Gdx.app.exit()
            // TODO: this doesn't end the process
        }
    })
    table.row()
    table.add(quit_button).width(300F).height(100F).pad(1F)

    stg_start.addActor(table)

    // Set the input processors to use
    input_processors.clear()
    input_processors.addProcessor(stg_start)

    // Set the game state
    game_state = GameState.SHOW_GAME_START
}

internal fun startMenuOptions() {
    // Define the UI components
    val table = Table()
    table.setFillParent(true)

    val btn_font = BitmapFont()
    val btn_skin = Skin()
    btn_skin.addRegions(TextureAtlas(Gdx.files.internal("GUI/Buttons.atlas")))

    val btn_opt_style = TextButton.TextButtonStyle()
    with(btn_opt_style) {
        font = btn_font
        up = btn_skin.getDrawable("button-up")
        down = btn_skin.getDrawable("button-down")
        checked = btn_skin.getDrawable("button-up")
    }

    val start_button = TextButton("Start Game", btn_opt_style)
    start_button.addListener(object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            startGame()
        }
    })
    table.add(start_button).width(300F).height(100F).pad(1F)

    val btn_option_style = TextButton.TextButtonStyle()
    with(btn_option_style) {
        font = btn_font
        up = btn_skin.getDrawable("button-up")
        down = btn_skin.getDrawable("button-down")
        checked = btn_skin.getDrawable("button-up")
    }
    val option_button = TextButton("Options", btn_option_style)
    option_button.addListener(object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            //TODO: show options menu
        }
    })
    table.row()
    table.add(option_button).width(300F).height(100F).pad(1F)

    val btn_quit_style = TextButton.TextButtonStyle()
    with(btn_quit_style) {
        font = btn_font
        up = btn_skin.getDrawable("button-up")
        down = btn_skin.getDrawable("button-down")
        checked = btn_skin.getDrawable("button-up")
    }
    val quit_button = TextButton("Quit", btn_quit_style)
    quit_button.addListener(object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            RL.disposeAll()
            Gdx.app.exit()
            // TODO: this doesn't end the process
        }
    })
    table.row()
    table.add(quit_button).width(300F).height(100F).pad(1F)

    stg_options.addActor(table)

    // Set the input processors to use
    input_processors.clear()
    input_processors.addProcessor(stg_options)

    // Set the game state
    game_state = GameState.SHOW_GAME_OPTIONS
}

internal fun startGame() {
    // Set the input processors to use
    input_processors.clear()
    input_processors.addProcessor(GameInputHandler(EM!!))

    // Init the Entity Manager
    EM!!.init()

    // Set the game state
    game_state = GameState.SHOW_GAME_PLAY
}

internal fun pauseGame() {
    // TODO: pause the game and display the pause menu
    // Set the input processors to use
    input_processors.clear()
    input_processors.addProcessor(PauseMenuInputHandler())

    // Set the game state
    game_state = GameState.SHOW_GAME_PAUSE
}

internal fun gameOver() {
    // TODO: End the game, show the final score, etc.
    // Set the input processors to use
    input_processors.clear()
    input_processors.addProcessor(GameOverInputHandler())

    // Dispose of game textures
    RL.disposeGameTextures()

    // Set the game state
    game_state = GameState.SHOW_GAME_OVER
}