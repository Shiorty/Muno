package at.htlkaindorf.ahif18;

import at.htlkaindorf.ahif18.Actors.ScrollElement;
import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.data.NetworkBuffer;
import at.htlkaindorf.ahif18.data.PlayerInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameScreen implements Screen {

    //Framework variables
    private MunoGame game;
    private Stage stage;
    private Skin skin;

    //Other Variables
    //buffers the network
    private NetworkBuffer nwa;

    public GameScreen(MunoGame game)
    {
        this.game = game;

        skin = new Skin(Gdx.files.internal("ui/metal/skin/metal-ui.json"));

        stage = new Stage(new StretchViewport(MunoGame.SCREEN_SIZE[0], MunoGame.SCREEN_SIZE[1]));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();

        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setPosition(1920 - Card.RED_3.getTexture().getWidth() * 31, 0);
        scrollPane.setHeight(1080f);
        scrollPane.setWidth(1000f);
        scrollPane.setFadeScrollBars(false);
        stage.addActor(scrollPane);

        nwa = new NetworkBuffer();

        for(PlayerInfo c : nwa.fetchAllPlayers()) {
            table.add(new ScrollElement(c));
            table.row();
        }

        scrollPane.validate();
        scrollPane.setScrollPercentY(20f / Card.values().length);
        scrollPane.updateVisualScroll();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);
        stage.act(delta);

        /*
        Batch b = stage.getBatch();
        b.begin();
        b.draw(Card.RED_3.getTexture(), 20, 20, 100, 100);
        b.end();
        */

        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }
}
