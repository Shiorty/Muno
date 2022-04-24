package at.htlkaindorf.ahif18;

import at.htlkaindorf.ahif18.Actors.CardActor;
import at.htlkaindorf.ahif18.Actors.PlayerScrollElement;
import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.data.NetworkBuffer;
import at.htlkaindorf.ahif18.data.PlayerInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameScreen implements Screen {

    //Framework variables
    private MunoGame game;
    private Stage stage;
    private Skin skin;
    //Buttons
    private TextButton menuButtons[];
    private CardActor lastPlayedCard;

    //Other Variables
    //buffers the network
    private NetworkBuffer nwb;

    public GameScreen(MunoGame game)
    {
        this.game = game;
        skin = new Skin(Gdx.files.internal("ui/metal/skin/metal-ui.json"));
        stage = new Stage(new StretchViewport(MunoGame.SCREEN_SIZE[0], MunoGame.SCREEN_SIZE[1]));

        Gdx.input.setInputProcessor(stage);

        menuButtons = new TextButton[2];
        menuButtons[0] = new TextButton("Menu", skin);
        menuButtons[0].setSize(350, 100);
        menuButtons[0].setPosition(0, 800);//y position is desired position - height
        menuButtons[0].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        stage.addActor(menuButtons[0]);
        menuButtons[1] = new TextButton("Chat", skin);
        menuButtons[1].setSize(350, 100);
        menuButtons[1].setPosition(400, 800);//y position is desired position - height
        menuButtons[1].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        stage.addActor(menuButtons[1]);

        Table table = new Table();
        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setPosition(1600 - 650, 0);
        scrollPane.setHeight(900);
        scrollPane.setWidth(650);
        scrollPane.setFadeScrollBars(false);
        stage.addActor(scrollPane);

        lastPlayedCard = new CardActor(520, 450, 280);
        lastPlayedCard.setCard(Card.RED_1);
        stage.addActor(lastPlayedCard);

        nwb = new NetworkBuffer();

        for(PlayerInfo c : nwb.fetchAllPlayers()) {
            table.add(new PlayerScrollElement(c));
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
