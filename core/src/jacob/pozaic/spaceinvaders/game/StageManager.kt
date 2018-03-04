package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.FitViewport
import jacob.pozaic.spaceinvaders.input.GameInputHandler
import jacob.pozaic.spaceinvaders.input.GameOverInputHandler
import jacob.pozaic.spaceinvaders.input.PauseMenuInputHandler

internal val batch = SpriteBatch()
internal val stg_start = Stage(FitViewport(screen.width, screen.height), batch)
internal val stg_options = Stage(FitViewport(screen.width, screen.height), batch)
internal val stg_game = Stage(FitViewport(screen.width, screen.height), batch)

private val ui_font = BitmapFont()

private val player_life_image = RL.getPlayerTexture(0)
internal val player_lives_img = listOf(Image(player_life_image), Image(player_life_image), Image(player_life_image))

internal fun startMenu() {
    // Define the UI components
    val table = Table()
    table.setFillParent(true)

    val btn_skin = Skin()
    btn_skin.addRegions(TextureAtlas(Gdx.files.internal("GUI/Buttons.atlas")))

    val btn_start_style = TextButton.TextButtonStyle()
    with(btn_start_style) {
        font = ui_font
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
        font = ui_font
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
        font = ui_font
        up = btn_skin.getDrawable("button-up")
        down = btn_skin.getDrawable("button-down")
        checked = btn_skin.getDrawable("button-up")
    }
    val quit_button = TextButton("Quit", btn_quit_style)
    quit_button.addListener(object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            Gdx.app.exit()
            // TODO: this doesn't end the process, also make sure to dispose all resources
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

    //TODO: make into options menu, not this
    val btn_skin = Skin()
    btn_skin.addRegions(TextureAtlas(Gdx.files.internal("GUI/Buttons.atlas")))

    val btn_opt_style = TextButton.TextButtonStyle()
    with(btn_opt_style) {
        font = ui_font
        up = btn_skin.getDrawable("button-up")
        down = btn_skin.getDrawable("button-down")
        checked = btn_skin.getDrawable("button-up")
    }

    val btn_option_style = TextButton.TextButtonStyle()
    with(btn_option_style) {
        font = ui_font
        up = btn_skin.getDrawable("button-up")
        down = btn_skin.getDrawable("button-down")
        checked = btn_skin.getDrawable("button-up")
    }
    val option_button = TextButton("Options", btn_option_style)
    option_button.addListener(object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
        }
    })
    table.row()
    table.add(option_button).width(300F).height(100F).pad(1F)

    stg_options.addActor(table)

    // Set the input processors to use
    input_processors.clear()
    input_processors.addProcessor(stg_options)

    // Set the game state
    game_state = GameState.SHOW_GAME_OPTIONS
}

internal fun startGame() {
    val player_lives_table = Table()
    player_lives_table.setFillParent(true)

    val lives_skin_style = Label.LabelStyle()
    lives_skin_style.font = ui_font
    val lives_label = Label("Lives: ", lives_skin_style)
    with(player_lives_table) {
        add(lives_label)
        player_lives_img.forEach { image -> add(image).size(ui_texture_scale).padLeft(5F) }
        padLeft(5F).padTop(5F).left().top()
    }
    stg_game.addActor(player_lives_table)

    // Set the input processors to use
    input_processors.clear()
    input_processors.addProcessor(GameInputHandler())

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