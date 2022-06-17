package at.htlkaindorf.ahif18.network;

import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.data.LoopingSequence;
import at.htlkaindorf.ahif18.data.PlayerInfo;
import at.htlkaindorf.ahif18.data.Sequence;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Server Thread handling new Clients
 * When a new client connects a new ClientConnection Thread is opened
 *
 * <br><br>
 * Last changed: 2022-06-16
 * @author Andreas Kurz; Jan Mandl
 */
public class Server extends Thread{

    public static final int PORT = 60000;
    public static final int DEFAULT_CARD_AMOUNT = 7;

    /**
     * Class containing information about a player
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Player
    {
        private ClientConnection connection;
        private String name;
        private int playerID;
        private List<Card> cards;

        public Player(ClientConnection connection, int playerID){
            this.connection = connection;
            this.playerID = playerID;
            generateNewCards(DEFAULT_CARD_AMOUNT);
        }

        /**
         * Generates a new deck for the player
         * @param amount amount of cards
         */
        public void generateNewCards(int amount)
        {
            cards = IntStream
                    .range(0, amount)
                    .mapToObj(i -> Card.randomCard())
                    .collect(Collectors.toList());
        }

        /**
         * Returns a PlayerInfo object containting all the information about the client
         * @return information about the player
         */
        public PlayerInfo getPlayerInfo()
        {
            return new PlayerInfo(playerID, name, cards.size());
        }
    }

    //Network
    private ServerSocket socket;

    //Game Information
    private LoopingSequence currentPlayerIndex;
    private List<Player> players;
    private Sequence playerIDSequence;
    private Card lastPlayedCard;

    public Server()
    {
        players = new ArrayList<>();
        currentPlayerIndex = new LoopingSequence(0,0);
        playerIDSequence = new Sequence();

        lastPlayedCard = Card.randomCard();
    }

    @SneakyThrows
    @Override
    public void interrupt() {
        //System.out.println("Server Interrupted");

        players.stream().map(Player::getConnection).forEach(Thread::interrupt);
        socket.close();
        super.interrupt();
    }

    @Override
    public void run()
    {
        try
        {
            runServer();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Starts the server thread<br>
     * The server thread will wait for a client to connect
     * @throws Exception when an error occurs
     */
    public void runServer() throws Exception
    {
        socket = new ServerSocket(PORT);

        while(!isInterrupted())
        {
            Socket clientSocket = socket.accept();

            int playerID = playerIDSequence.nextValue();

            ClientConnection connection = new ClientConnection(this, clientSocket, playerID);

            players.add(new Player(
                connection,
                playerID
            ));

            connection.start();
        }
    }

    /**
     * Creates a new Player Sequence Object<br>
     * should always be called when the number of player changes
     */
    private void reconfigureCurrentPlayerSequence()
    {
        int currentIndex = currentPlayerIndex.currentValue();

        if(currentIndex == -1){
            currentIndex = 0;
        }

        if(currentIndex >= players.size()){
            currentIndex = 0;
        }

        currentPlayerIndex = new LoopingSequence(
                0,
                players.size(),
                currentIndex
        );
    }

    /**
     * Searches for the player object with a certain ID
     * @param id the ID of the player
     * @return the player object representing the player
     */
    private Player findPlayerOfID(int id){
        return players.stream().filter(player ->
            id == player.getPlayerID()
        ).findFirst().get();
    }

    /**
     * Searches for the index that the player object is located at
     * @param id the id of the player
     * @return the index of the player
     */
    private int findIndexOfPlayer(int id){
        for(int i = 0; i < players.size(); i++)
        {
            if(players.get(i).getPlayerID() == id)
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Gets the ID of the player who's turn it currently is
     * @return the id of the current player
     */
    public int getCurrentPlayerID(){
        return players.get(currentPlayerIndex.currentValue()).getPlayerID();
    }

    // --- Player Triggered Events --- //

    /**
     * Handles a player Join Event<br>
     * Replies to the player and informs all other players
     * @param id the id of the new player
     * @param name the name of the new player
     */
    public void playerJoined(int id, String name){
        Player newPlayer = findPlayerOfID(id);
        newPlayer.setName(name);

        List<PlayerInfo> playerInfos = players.stream()
                                    .map(Player::getPlayerInfo)
                                    .collect(Collectors.toList());

        newPlayer.getConnection().sendInitResponse(lastPlayedCard, playerInfos, newPlayer.getCards());

        reconfigureCurrentPlayerSequence();

        players.forEach(player -> {
            player.getConnection().sendPlayerJoined(newPlayer.getPlayerInfo());
            player.getConnection().sendCurrentPlayerUpdate(getCurrentPlayerID());
        });
    }

    /**
     * Informs all other players about the player leaving
     * @param id the id of the leaving player
     */
    public void playerLeft(int id) {

        Player playerThatWantsToLeave = findPlayerOfID(id);

        players.remove(findIndexOfPlayer(id));

        reconfigureCurrentPlayerSequence();

        playerThatWantsToLeave.getConnection().interrupt();

        players.forEach(player -> {
            player.getConnection().sendPlayerLeft(playerThatWantsToLeave.getPlayerInfo());
            player.getConnection().sendCurrentPlayerUpdate(getCurrentPlayerID());
        });

    }

    /**
     * Decides if a card can be played<br>
     * Also replies to all clients if the card has been played
     * @param id the id of the player wanting to play a card
     * @param cardPlayed the card that the player wants to plays
     */
    public void cardPlayed(int id, Card cardPlayed) {

        int playerIndex = findIndexOfPlayer(id);

        if(playerIndex == -1)
        {
            //Player doesn't exist?
            return;
        }

        if(id != getCurrentPlayerID())
        {
            //Its not the players turn
            System.out.println("Its not the players turn | Current Player is: " + getCurrentPlayerID() + " | your id is: " + id);
            return;
        }

        if(!findPlayerOfID(id).cards.contains(cardPlayed))
        {
            //Hacker guy wants to play a card he doesnt own
            return;
        }

        if(!lastPlayedCard.hasEqualGroup(cardPlayed))
        {
            //Card can not be played
            return;
        }

        //Get information about the player
        Player currentPlayer = players.get(playerIndex);

        //play the card
        lastPlayedCard = cardPlayed;
        currentPlayer.cards.remove(lastPlayedCard);

        //notify all players about the card play
        players.forEach(player -> {
           player.getConnection().sendCardPlayed(currentPlayer.getPlayerInfo(), cardPlayed);
        });

        if( cardPlayed == Card.BSKIP ||
            cardPlayed == Card.YSKIP ||
            cardPlayed == Card.RSKIP ||
            cardPlayed == Card.GSKIP)
        {
            currentPlayerIndex.nextValue();
        }

        //tell all players who the new current player is
        currentPlayerIndex.nextValue();
        players.forEach(player -> {
            player.getConnection().sendCurrentPlayerUpdate(getCurrentPlayerID());
        });
    }

    /**
     * Decides if a player can play a card<br>
     * It then answers the player and informs all the other players
     * @param playerID id of the payer wanting to draw a card
     */
    public void drawCard(int playerID) {

        Player currentPlayer = findPlayerOfID(playerID);

        if(currentPlayer.getPlayerID() != getCurrentPlayerID())
        {
            //its not the players turn
            return;
        }

        //Get a random card
        Card newCard = Card.randomCard();

        currentPlayer.cards.add(newCard);
        currentPlayer.connection.sendDrawnCard(newCard);

        players.forEach(player -> {
            player.connection.sendPlayerUpdate(currentPlayer.getPlayerInfo());
        });

        currentPlayerIndex.nextValue();
        players.forEach(player -> {
            player.getConnection().sendCurrentPlayerUpdate(getCurrentPlayerID());
        });
    }
}
