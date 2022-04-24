package at.htlkaindorf.ahif18.ui;

import at.htlkaindorf.ahif18.GameScreen;
import at.htlkaindorf.ahif18.MunoGame;
import at.htlkaindorf.ahif18.data.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

    private Color backgroundColor;

    public MainMenuScreen(MunoGame game)
    {
        this.game = game;

        atlas = new TextureAtlas("ui/vhs/skin/vhs-ui.atlas");
        skin = new Skin(Gdx.files.internal("ui/vhs/skin/vhs-ui.json"), atlas);

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
        TextButton optionsButton = new TextButton("OPTIONS", skin);
        TextButton exitButton = new TextButton("EXIT", skin);

        //Add listeners to buttons
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getScreen().dispose();
                game.setScreen(new GameScreen(game));
            }
        });

        optionsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getScreen().dispose();
                game.setScreen(new OptionScreen(game));
            }
        });

        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
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
        mainTable.add(optionsButton).height(25).padTop(25);
        mainTable.row();
        mainTable.add(exitButton).height(25).padTop(25).padBottom(75);

        //Add table to stage
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        controls(delta);
        viewport.apply();

        ScreenUtils.clear(backgroundColor);

//        stage.getBatch().begin();
//        stage.getBatch().draw(Card.BLUE_0.getTexture(), 0, 0, 1600, 900);
//        stage.getBatch().end();

        stage.act(delta);
        stage.draw();
    }

    public void controls(float delta)
    {
        if(Gdx.input.isKeyJustPressed(Input.Keys.F1))
        {
            mainTable.setDebug(!mainTable.getDebug());
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
