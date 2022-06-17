package at.htlkaindorf.ahif18.ui.Screens;

import at.htlkaindorf.ahif18.MunoGame;
import at.htlkaindorf.ahif18.bl.I_Notifiable;
import at.htlkaindorf.ahif18.bl.Settings;
import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.data.PlayerInfo;
import at.htlkaindorf.ahif18.network.Client;
import at.htlkaindorf.ahif18.network.NetworkBuffer;
import at.htlkaindorf.ahif18.network.Server;
import at.htlkaindorf.ahif18.ui.Actors.CardCollectionActor;
import at.htlkaindorf.ahif18.ui.Actors.PlayerScrollElement;
import at.htlkaindorf.ahif18.ui.Actors.UnoCard;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages the Uno Game itself and displays everything that is necessary to play it.
 *
 * <br><br>
 * Last changed: 2022-06-17
 * @author Jan Mandl; Andreas Kurz
 */
public class GameScreen implements Screen, I_Notifiable, CardCollectionActor.CardClickedListener {

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
    private List<Thread> threads;
    private Client client;

    /**
     * Creates a new GameScreen which is used to host a game
     * @param game The Instance of the MunoGame class
     * @param playerName The name of the player
     */
    public GameScreen(MunoGame game, String playerName)
    {
        this(game, playerName, null);
    }

    /**
     * Creates a new GameScreen that is used to join a game
     * @param game The Instance of the MunoGame class
     * @param playerName The name of the player
     * @param hostIP the IP of the server<br>
     *               The game will be hosted if omitted
     */
    public GameScreen(MunoGame game, String playerName, String hostIP)
    {
        this.game = game;

        //load background color from settings
        backgroundColor = Settings.getInstance().getBackgroundColor();

        //load game skin
        skin = new Skin(Gdx.files.internal("ui/vhs-new/vhs_new.json"));

        //create new stage
        viewport = new FitViewport(MunoGame.SCREEN_SIZE[0], MunoGame.SCREEN_SIZE[1]);
        stage = new Stage(viewport);

        //set the stage as the input processor
        Gdx.input.setInputProcessor(stage);

        //setup gui components
        menuButtons = new TextButton[2];

        //create Menu button
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

        //create chat button
        menuButtons[1] = new TextButton("Chat", skin.get("border", TextButton.TextButtonStyle.class));
        menuButtons[1].setSize(175, 50);
        menuButtons[1].setPosition(225, 825);//y position is desired position - height
        menuButtons[1].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        stage.addActor(menuButtons[1]);

        //setup the scrollbar
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

        cardsInHand = new CardCollectionActor(this);
        cardsInHand.setBounds(25, 25, 900, 150);
        stage.addActor(cardsInHand);


        //--- Network Stuff ---//
        {

            threads = new ArrayList<>();

            nwb = new NetworkBuffer(this);
            client = new Client( nwb, playerName, hostIP);
            threads.add(client);

            if(hostIP == null)
            {
                Server server = new Server();
                threads.add(server);
            }

            threads.forEach(Thread::start);
        }
        //--- End Network Stuff ---//
    }

    /**
     * Action that retuns to the main menu<br>
     * Will send a leave message to the server if a client is active
     */
    public void returnToMenu(){
        if(client != null && client.isAlive()){
            client.leave();
        }

        game.changeScreen(new MainMenuScreen(game));
    }

    /**
     * Sends draw card request to server
     */
    public void drawCard()
    {
        client.drawCard();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(backgroundColor);

        stage.act(delta);
        controls(delta);

        stage.draw();
    }

    /**
     * Tests if any hotkey was pressed
     * @param delta time elapsed since the last frame (in ms)
     */
    public void controls(float delta)
    {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            returnToMenu();
            return;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.F1)){
            stage.setDebugAll(!stage.isDebugAll());
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.F3)){
            //get current debug state of the first element
            boolean isDebugModeEnabled = scrollElements.size() > 0 && scrollElements.get(0).getDebug();

            scrollElements.forEach(playerScrollElement ->
                playerScrollElement.setDebug(!isDebugModeEnabled)
            );
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

        for(Thread t : threads){
            t.interrupt();
        }
    }

    @Override
    public void cardClicked(CardCollectionActor cardsInHand, int playedCardIndex) {
        Card playedCard = cardsInHand.getCard(playedCardIndex);

        //test if card can be played
        if(playedCard.hasEqualGroup(lastPlayedCard.getCard()))
        {
            //send play card request
            client.playCard(playedCard);
        }
    }

    @Override
	public void notifyElement() {
        Gdx.app.postRunnable(() -> {
            lastPlayedCard.setCard(nwb.fetchLastPlayedCard());
            cardsInHand.setCards(nwb.fetchAllCards());

            boolean myTurn = nwb.getCurrentPlayerID() == client.getPlayerID();
            cardsInHand.setActive(myTurn);
            deck.setActive(myTurn);

            scrollElements = nwb.fetchAllPlayers()
                    .stream()
                    .map(PlayerScrollElement::new)
                    .collect(Collectors.toList());

            scrollElements.stream().filter(scrollElements -> scrollElements.getPlayer().getPlayerID() == client.getPlayerID()).findFirst().ifPresent(player -> player.setLocalPlayer(true));
            scrollElements.stream().filter(scrollElements -> scrollElements.getPlayer().getPlayerID() == nwb.getCurrentPlayerID()).findFirst().ifPresent(player -> player.setCurrentPlayer(true));

            scrollTable.clear();

            scrollElements.forEach(element -> {
                scrollTable.add(element);
                scrollTable.row();
            });
        });
	}
}
