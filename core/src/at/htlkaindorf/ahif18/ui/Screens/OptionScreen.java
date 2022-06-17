package at.htlkaindorf.ahif18.ui.Screens;

import at.htlkaindorf.ahif18.MunoGame;
import at.htlkaindorf.ahif18.bl.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Screen which lets users change their preferences
 *
 * <br><br>
 * Last changed: 2022-06-16
 * @author Andreas Kurz
 */
public class OptionScreen implements Screen {

    private MunoGame game;

    private Skin skin;
    private TextureAtlas atlas;
    private Viewport viewport;

    private Stage stage;
    private OrthographicCamera camera;

    private Table tableMain;
    private Table tableSettings;

    private Color backgroundColor;

    public OptionScreen(MunoGame game)
    {
        this.game = game;

        atlas = new TextureAtlas("ui/vhs/skin/vhs-ui.atlas");
        skin = new Skin(Gdx.files.internal("ui/vhs/skin/vhs-ui.json"), atlas);

        camera = new OrthographicCamera();
        viewport = new FitViewport(MunoGame.SCREEN_SIZE[0], MunoGame.SCREEN_SIZE[1], camera);

        stage = new Stage(viewport);
    }

    @Override
    public void show() {

        backgroundColor = Settings.getInstance().getBackgroundColor();

        Label title = new Label("OPTIONS", skin);
        title.setFontScale(2);

        Label lbPlayerName = new Label("Player name: ", skin);
        TextField tfPlayerName = new TextField("", skin);
        tfPlayerName.setText(Settings.getInstance().getKeyValue(Settings.Key.PLAYER_NAME));
        tfPlayerName.setMaxLength(15);
        tfPlayerName.setTextFieldListener((textField, c) -> {
            Settings.getInstance().saveValue(Settings.Key.PLAYER_NAME, textField.getText());
        });

        Label lbBackgroundColor = new Label("Background Color: ", skin);
        TextField tfColorInput = new TextField("", skin);
        tfColorInput.setText("#" + backgroundColor.toString());
        tfColorInput.setMaxLength(9);
        tfColorInput.setTextFieldListener((textField, c) -> {
            Settings.getInstance().setBackgroundColor(textField.getText());
            backgroundColor = Settings.getInstance().getBackgroundColor();
        });

        Label lbCheckboxTest = new Label("Checkbox Test: ", skin);
        CheckBox cbCheckboxTest = new CheckBox("", skin);
        cbCheckboxTest.toggle();

        TextButton btnReset = new TextButton("Reset", skin);
        btnReset.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resetSettings();
            }
        });

        TextButton btnBack = new TextButton("Back", skin);
        btnBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                returnToMenu();
            }
        });

        tableMain = new Table();
        tableMain.setFillParent(true);

        //--- Fill Table ---//
        tableMain.add(title).padTop(50).expandX().align(Align.center);
        tableMain.row();

        //--- Create SettingsTable ---//
        tableSettings = new Table();
        tableSettings.left();
        tableSettings.columnDefaults(0).align(Align.left).width(450).padBottom(15);
        tableSettings.columnDefaults(1).align(Align.left).width(200).expandX();

        tableSettings.add(lbPlayerName);
        tableSettings.add(tfPlayerName).width(400);
        tableSettings.row();
        tableSettings.add(lbBackgroundColor);
        tableSettings.add(tfColorInput);
        tableSettings.row();
        tableSettings.add(lbCheckboxTest);
        tableSettings.add(cbCheckboxTest);
        //--- Finish SettingsTable

        tableMain.add(tableSettings).align(Align.left).padTop(50).width(MunoGame.SCREEN_SIZE[0]);
        tableMain.row();
        tableMain.add(btnReset).expandY().align(Align.bottom);
        tableMain.row();
        tableMain.add(btnBack).padTop(20).padBottom(50);
        //--- Finish Main Table

        stage.addActor(tableMain);
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Swaps back to the main menu
     */
    public void returnToMenu(){
        game.changeScreen(new MainMenuScreen(game));
    }

    /**
     * Resets all Settings present
     */
    public void resetSettings(){
        Settings.getInstance().restoreDefaults();
        game.changeScreen(new OptionScreen(game));
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.F1))
        {
            tableMain.setDebug(!tableMain.getDebug());
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F2))
        {
            tableSettings.setDebug(!tableSettings.getDebug());
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
        {
            returnToMenu();
            return;
        }

        ScreenUtils.clear(backgroundColor);

        viewport.apply();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        atlas.dispose();
        skin.dispose();

        stage.dispose();
    }
}
