package at.htlkaindorf.ahif18.ui.Screens;

import at.htlkaindorf.ahif18.MunoGame;
import at.htlkaindorf.ahif18.ui.Actors.CardCollectionActor;
import at.htlkaindorf.ahif18.ui.Actors.PlayerScrollElement;
import at.htlkaindorf.ahif18.ui.Actors.UnoCard;
import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.network.NetworkBuffer;
import at.htlkaindorf.ahif18.bl.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Jan Mandl; Andreas Kurz
 */
public class GameScreen implements Screen {

    //Framework variables
    private MunoGame game;
    private Stage stage;
    private Skin skin;
    private Viewport viewport;
    private Table scrollTable;
    private Color backgroundColor;
  
    //Elements
    private TextButton[] menuButtons;
    private UnoCard lastPlayedCard;
    private UnoCard deck;
    private CardCollectionActor cardsInHand;
    private List<PlayerScrollElement> scrollElements;

    //Other Variables
    //buffers the network
    private NetworkBuffer nwb;

    public GameScreen(MunoGame game)
    {
        this.game = game;
        skin = new Skin(Gdx.files.internal("ui/vhs-new/vhs_new.json"));
        viewport = new FitViewport(MunoGame.SCREEN_SIZE[0], MunoGame.SCREEN_SIZE[1]);
        stage = new Stage(viewport);
        //stage.setDebugAll(true);

        backgroundColor = Settings.getInstance().getBackgroundColor();

        Gdx.input.setInputProcessor(stage);

        menuButtons = new TextButton[2];
        menuButtons[0] = new TextButton("Menu", skin.get("border", TextButton.TextButtonStyle.class));
        menuButtons[0].setSize(175, 50);
        menuButtons[0].setPosition(20, 825);//y position is desired position - height
        menuButtons[0].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                returnToMenu();
            }
        });
        stage.addActor(menuButtons[0]);
        menuButtons[1] = new TextButton("Chat", skin.get("border", TextButton.TextButtonStyle.class));
        menuButtons[1].setSize(175, 50);
        menuButtons[1].setPosition(225, 825);//y position is desired position - height
        menuButtons[1].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        stage.addActor(menuButtons[1]);

        scrollTable = new Table();
        scrollTable.columnDefaults(0).padTop(10).padBottom(10).width(600).height(80).expandX();
        ScrollPane scrollPane = new ScrollPane(scrollTable, skin);
        scrollPane.setPosition(1600 - 650, 0);
        scrollPane.setHeight(900);
        scrollPane.setWidth(650);
        scrollPane.setFadeScrollBars(false);

        stage.addActor(scrollPane);

        //Make the scrollpane scrollable without having to click on it first
        stage.setScrollFocus(scrollPane);

        lastPlayedCard = new UnoCard(Card.CARD_BACK);
        final int CARD_WIDTH = 150;
        lastPlayedCard.setBounds(950 / 2 - CARD_WIDTH/2, 400, CARD_WIDTH, CARD_WIDTH / 2 * 3);
        stage.addActor(lastPlayedCard);

        deck = new UnoCard(Card.CARD_BACK);
        final int DECK_WIDTH = 110;
        deck.setBounds((950 / 2 - CARD_WIDTH/2) + CARD_WIDTH + 25, 400 + (CARD_WIDTH / 2 * 3 - (DECK_WIDTH / 2 * 3)) / 2, DECK_WIDTH, DECK_WIDTH / 2 * 3);
        deck.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                drawCard();
            }
        });
        stage.addActor(deck);

        cardsInHand = new CardCollectionActor();
        cardsInHand.setBounds(25, 25, 900, 150);
        cardsInHand.addCard(Card.G4);
        cardsInHand.addCard(Card.B8);
        cardsInHand.addCard(Card.G7);
        cardsInHand.addCard(Card.R0);
        stage.addActor(cardsInHand);

        nwb = new NetworkBuffer();

        scrollElements = nwb.fetchAllPlayers()
                            .stream()
                            .map(PlayerScrollElement::new)
                            .collect(Collectors.toList());

        scrollElements.forEach(element -> {
            scrollTable.add(element);
            scrollTable.row();
        });

        scrollPane.validate();
//        scrollPane.setScrollPercentY(20f / Card.values().length);
        scrollPane.updateVisualScroll();
    }

    public void returnToMenu(){
        game.changeScreen(new MainMenuScreen(game));
    }

    /**
     * Adds Random card to Hand
     */
    public void drawCard()
    {
        Random r = new Random();
        cardsInHand.addCard(Card.values()[r.nextInt(Card.values().length)]);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(backgroundColor);
        stage.act(delta);

        controls(delta);

        /*
        Batch b = stage.getBatch();
        b.begin();
        b.draw(Card.RED_3.getTexture(), 20, 20, 100, 100);
        b.end();
        */

        stage.draw();
    }

    public void controls(float delta)
    {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            returnToMenu();
            return;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.F1)){
            stage.setDebugAll(!stage.isDebugAll());
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.F2)){
            scrollTable.setDebug(!scrollTable.getDebug());
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.F3)){
            //get current debug state of the first element
            boolean isDebugModeEnabled = scrollElements.size() > 0 && scrollElements.get(0).getDebug();

            scrollElements.forEach(playerScrollElement ->
                playerScrollElement.setDebug(!isDebugModeEnabled)
            );
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.F12)){
            drawCard();
        }

        int playedCardIndex = cardsInHand.retrievePlayedCard();
        if(playedCardIndex != -1)
        {
            Card playedCard = cardsInHand.getCard(playedCardIndex);

            if(playedCard.hasEqualGroup(lastPlayedCard.getCard())) {
                lastPlayedCard.setCard(playedCard);
                cardsInHand.removeCard(playedCardIndex);
            }
        }
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
        skin.dispose();
    }
}
