package jacob.pozaic.spaceinvaders.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import jacob.pozaic.spaceinvaders.input.GameInputHandler

internal var batch = SpriteBatch()
internal var stg_start = Stage(FitViewport(screen.width, screen.height), batch)
internal var stg_options = Stage(FitViewport(screen.width, screen.height), batch)
internal var stg_game = Stage(FitViewport(screen.width, screen.height), batch)
internal var stg_game_over = Stage(FitViewport(screen.width, screen.height), batch)

private val ui_font = BitmapFont()

internal var score_label: Label? = null

internal var arrow_left: Button? = Button()
internal var arrow_right: Button? = Button()

internal fun startMenu() {
    // Define the UI components
    val table = Table()
    table.setFillParent(true)

    val misc_skin = Skin()
    misc_skin.addRegions(TextureAtlas(Gdx.files.internal("GUI/Misc.atlas")))
    val title = Image(misc_skin.getDrawable("game-title"))
    table.add(title).width(450F).height(150F).pad(5F).row()

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
    table.add(start_button).width(280F).height(100F).pad(5F)

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
    table.add(option_button).width(280F).height(100F).pad(5F)

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
            // TODO: make sure to dispose all resources
            System.exit(0)
        }
    })
    table.row()
    table.add(quit_button).width(280F).height(100F).pad(5F)

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

    val btn_skin = Skin()
    btn_skin.addRegions(TextureAtlas(Gdx.files.internal("GUI/Buttons.atlas")))

    val btn_use_acc_style = TextButton.TextButtonStyle()
    with(btn_use_acc_style) {
        font = ui_font
        up = btn_skin.getDrawable("button-up")
        down = btn_skin.getDrawable("button-up")
        checked = btn_skin.getDrawable("button-down")
    }
    val btn_use_acc = TextButton("Don't use Accelerometer", btn_use_acc_style)
    btn_use_acc.addListener(object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            if (btn_use_acc.isChecked){
                draw_arrows = true
            }
        }
    })
    table.add(btn_use_acc).width(280F).height(100F).pad(5F).row()

    val btn_exp_mode = TextButton("Non-Classic mode", btn_use_acc_style)
    btn_exp_mode.addListener(object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            if (btn_exp_mode.isChecked){
                play_classic = false
            }
        }
    })
    table.add(btn_exp_mode).width(280F).height(100F).pad(5F)

    if(draw_arrows) {
        btn_use_acc.isDisabled = true
        btn_use_acc.isChecked = true
    }

    val btn_ret_main_style = TextButton.TextButtonStyle()
    with(btn_ret_main_style) {
        font = ui_font
        up = btn_skin.getDrawable("button-up")
        down = btn_skin.getDrawable("button-down")
        checked = btn_skin.getDrawable("button-up")
    }
    val btn_ret_main = TextButton("Main Menu", btn_ret_main_style)
    btn_ret_main.addListener(object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            startMenu()
            game_state = GameState.SHOW_GAME_START
        }
    })
    table.row()
    table.add(btn_ret_main).width(280F).height(100F).pad(5F)

    stg_options.addActor(table)

    // Set the input processors to use
    input_processors.clear()
    input_processors.addProcessor(stg_options)

    // Set the game state
    game_state = GameState.SHOW_GAME_OPTIONS
}

internal fun startGame() {
    val input = InputMultiplexer()

    val label_skin_style = Label.LabelStyle()
    label_skin_style.font = ui_font

    val player_lives_table = Table()
    player_lives_table.setFillParent(true)

    val lives_label = Label("Lives: ", label_skin_style)
    with(player_lives_table) {
        add(lives_label)
        player_lives_img!!.forEach { image -> add(image).size(ui_texture_scale).padLeft(5F) }
        padLeft(5F).padTop(5F).left().top()
    }
    stg_game.addActor(player_lives_table)

    val player_score_table = Table()
    player_score_table.setFillParent(true)
    score_label = Label("Score: 0", label_skin_style)
    player_score_table.add(score_label).width(100F)
    player_score_table.padRight(5F).padTop(5F).right().top()
    stg_game.addActor(player_score_table)

    if(draw_arrows) {
        val btn_skin = Skin()
        btn_skin.addRegions(TextureAtlas(Gdx.files.internal("GUI/ManualInput.atlas")))

        val arrow_style_left = Button.ButtonStyle()
        with(arrow_style_left) {
            up = btn_skin.getDrawable("Move-left")
            down = btn_skin.getDrawable("Move-left")
            checked = btn_skin.getDrawable("Move-left")
        }
        arrow_left = Button(arrow_style_left)

        val arrow_style_right = Button.ButtonStyle()
        with(arrow_style_right) {
            up = btn_skin.getDrawable("Move-right")
            down = btn_skin.getDrawable("Move-right")
            checked = btn_skin.getDrawable("Move-right")
        }
        arrow_right = Button(arrow_style_right)

        val arrow_table = Table()
        arrow_table.setFillParent(true)
        with(arrow_table){
            add(arrow_left).width(45F).height(35F).expand().left().bottom().pad(15F)
            add(arrow_right).width(45F).height(35F).expand().right().bottom().pad(15F)
        }
        stg_game.addActor(arrow_table)
        input.addProcessor(stg_game)
    }

    input.addProcessor(GameInputHandler())

    // Set the input processors to use
    input_processors.clear()
    input_processors.addProcessor(input)

    // Set the game state
    game_state = GameState.SHOW_GAME_PLAY
}

internal fun gameOver() {
    // Define the UI components
    val table = Table()
    table.setFillParent(true)

    val misc_skin = Skin()
    misc_skin.addRegions(TextureAtlas(Gdx.files.internal("GUI/Misc.atlas")))
    val title = Image(misc_skin.getDrawable("game-over"))
    table.add(title).width(450F).height(150F).pad(5F).row()

    val label_style = Label.LabelStyle()
    label_style.font = ui_font

    val final_score_label = Label("Final Score: $player_score", label_style)
    final_score_label.setAlignment(Align.center)
    table.add(final_score_label).width(450F).height(30F).pad(5F).row()

    val waves_completed_label = Label("Waves Completed: ${wave_number - 1}", label_style)
    waves_completed_label.setAlignment(Align.center)
    table.add(waves_completed_label).width(450F).height(30F).pad(5F).row()

    val btn_skin = Skin()
    btn_skin.addRegions(TextureAtlas(Gdx.files.internal("GUI/Buttons.atlas")))

    val btn_quit_style = TextButton.TextButtonStyle()
    with(btn_quit_style) {
        font = ui_font
        up = btn_skin.getDrawable("button-up")
        down = btn_skin.getDrawable("button-down")
        checked = btn_skin.getDrawable("button-up")
    }
    val restart_button = TextButton("Restart Game", btn_quit_style)
    restart_button.addListener(object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            shouldRestart = true
        }
    })
    table.add(restart_button).width(280F).height(100F).pad(5F).row()
    val quit_button = TextButton("Quit", btn_quit_style)
    quit_button.addListener(object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            Gdx.app.exit()
            // TODO: make sure to dispose all resources
            System.exit(0)
        }
    })
    table.add(quit_button).width(280F).height(100F).pad(5F)

    stg_game_over.addActor(table)

    // Set the input processors to use
    input_processors.clear()
    input_processors.addProcessor(stg_game_over)

    // Set the game state
    game_state = GameState.SHOW_GAME_OVER
}