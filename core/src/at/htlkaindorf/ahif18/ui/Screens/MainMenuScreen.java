package at.htlkaindorf.ahif18.ui.Screens;

import at.htlkaindorf.ahif18.MunoGame;
import at.htlkaindorf.ahif18.bl.Settings;
import at.htlkaindorf.ahif18.ui.Actors.JoinForm;
import at.htlkaindorf.ahif18.ui.Actors.MainMenuCardsActor;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen implements Screen {

    private MunoGame game;

    private OrthographicCamera camera;
    private Batch batch;
    private Stage stage;
    private Viewport viewport;

    private TextureAtlas atlas;
    private Skin skin;

    private Table mainTable;
    private List<TextButton> mainMenuOptions;
    private JoinForm joinForm;

    private Color backgroundColor;

    public MainMenuScreen(MunoGame game)
    {
        this.game = game;

        atlas = new TextureAtlas("ui/vhs-new/vhs_new.atlas");
        skin = new Skin(Gdx.files.internal("ui/vhs-new/vhs_new.json"), atlas);

        batch = new SpriteBatch();
        camera = new OrthographicCamera();

        //TODO Herausfinden wie ExtendViewport funktioniert
        viewport = new FitViewport(MunoGame.SCREEN_SIZE[0], MunoGame.SCREEN_SIZE[1], camera);

        stage = new Stage(viewport, batch);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        backgroundColor = Settings.getInstance().getBackgroundColor();

        //Create title
        Label title = new Label("MUNO", skin.get("title", Label.LabelStyle.class));
        title.setAlignment(Align.center);

        //Create buttons
        TextButton playButton = new TextButton("PLAY", skin);
        TextButton hostButton = new TextButton("HOST", skin);
        TextButton optionsButton = new TextButton("OPTIONS", skin);
        TextButton exitButton = new TextButton("EXIT", skin);

        mainMenuOptions = Arrays.asList(
            playButton,
            hostButton,
            optionsButton,
            exitButton
        );

        //Add listeners to buttons
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                openJoinForm();
            }
        });

        hostButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hostGame();
            }
        });

        optionsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.changeScreen(new OptionScreen(game));
            }
        });

        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeApplication();
            }
        });

        //Create table
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();

        //Add elements to table
        mainTable.add(title).width(125).height(50).padTop(75);
        mainTable.row();
        mainTable.add(new MainMenuCardsActor()).width(1600).height(300).padTop(100).padBottom(100);
        mainTable.row();
        mainTable.add(playButton).height(25);
        mainTable.row();
        mainTable.add(hostButton).height(25).padTop(25);
        mainTable.row();
        mainTable.add(optionsButton).height(25).padTop(25);
        mainTable.row();
        mainTable.add(exitButton).height(25).padTop(25).padBottom(75);

        joinForm = new JoinForm(skin);
        int width = 500, height = 300;
        joinForm.setBounds(MunoGame.SCREEN_SIZE[0]/2 - 250 , -300,  width, height);
        Color formColor = new Color(backgroundColor);
        formColor.sub(0.1f, 0.1f, 0.1f, 0);
        joinForm.setColor(formColor);
        joinForm.setZIndex(1000);
        joinForm.setVisible(true);
        joinForm.setOnCloseListener(this::closeJoinForm);
        joinForm.setFormSubmitListener(this::joinGame);

        //Add table to stage
        stage.addActor(mainTable);
        stage.addActor(joinForm);
    }

    public void openJoinForm(){
//        joinForm.setVisible(true);
        joinForm.addAction(Actions.moveTo(MunoGame.SCREEN_SIZE[0]/2 - 250 , MunoGame.SCREEN_SIZE[1]/2 - 150, 0.5f));


        //disable all other menu buttons buttons
        mainMenuOptions.forEach(button -> {
            button.setTouchable(Touchable.disabled);
        });
    }

    public void closeJoinForm(){
//        joinForm.setVisible(false);
        joinForm.addAction(Actions.moveTo(MunoGame.SCREEN_SIZE[0]/2 - 250 , -300, 0.5f));

        //re-enable buttons
        mainMenuOptions.forEach(button -> {
            button.setTouchable(Touchable.enabled);
        });
    }

    public void joinGame(String ip){
        game.changeScreen(new GameScreen(game, Settings.getInstance().getKeyValue(Settings.Key.PLAYER_NAME), ip));
    }

    public void hostGame(){
        game.changeScreen(new GameScreen(game, Settings.getInstance().getKeyValue(Settings.Key.PLAYER_NAME)));
    }

    @Override
    public void render(float delta) {
        controls(delta);
        viewport.apply();

        ScreenUtils.clear(backgroundColor);

        stage.act(delta);
        stage.draw();
    }

    public void closeApplication()
    {
        this.dispose();
//        Gdx.app.exit();
        System.exit(0);
    }

    public void controls(float delta)
    {
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            openJoinForm();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            closeJoinForm();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.F11))
        {
            Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();

            if(Gdx.graphics.isFullscreen()){
                Gdx.graphics.setWindowedMode(currentMode.width, currentMode.height);
            }
            else{
                Gdx.graphics.setFullscreenMode(currentMode);
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.F1))
        {
            stage.setDebugAll(!stage.isDebugAll());
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            closeApplication();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
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
        skin.dispose();
        atlas.dispose();
        stage.dispose();
    }
}
