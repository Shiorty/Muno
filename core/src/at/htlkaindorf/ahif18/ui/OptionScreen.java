package at.htlkaindorf.ahif18.ui;

import at.htlkaindorf.ahif18.MunoGame;
import at.htlkaindorf.ahif18.data.Settings;
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

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener(){
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
        tableSettings.columnDefaults(0).align(Align.left).width(450);
        tableSettings.columnDefaults(1).align(Align.left).width(200).expandX();

        tableSettings.add(lbBackgroundColor);
        tableSettings.add(tfColorInput);
        tableSettings.row();
        tableSettings.add(lbCheckboxTest);
        tableSettings.add(cbCheckboxTest);
        //--- Finish SettingsTable

        tableMain.add(tableSettings).align(Align.left).padTop(50).width(MunoGame.SCREEN_SIZE[0]);
        tableMain.row();
        tableMain.add(backButton).padBottom(50).expandY().align(Align.bottom);
        //--- Finish Main Table

        stage.addActor(tableMain);
        Gdx.input.setInputProcessor(stage);
    }

    public void returnToMenu(){
        game.changeScreen(new MainMenuScreen(game));
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
