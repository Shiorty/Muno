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
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Server Thread handling new Clients
 * When a new client connects a new ClientConnection Thread is opened
 *
 * Last changed: 2022-06-16
 * @author Andreas Kurz; Jan Mandl
 */
public class Server extends Thread{

    public static final int PORT = 60000;
    public static final int DEFAULT_CARD_AMOUNT = 7;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class Player
    {
        private ClientConnection connection;
        private String name;
        private int playerID;
        private ArrayList<Card> cards;

        public Player(ClientConnection connection, int playerID){
            this.connection = connection;
            this.playerID = playerID;
            generateNewCards(DEFAULT_CARD_AMOUNT);
        }

        public void generateNewCards(int amount)
        {
            Random r = new Random();

            cards  = new ArrayList<>(amount);
            for(int i = 0; i < amount; i++){
                cards.add(Card.randomCard());
            }
        }

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

    @Override
    public void run()
    {
        try {
            runServer();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @SneakyThrows
    @Override
    public void interrupt() {
        //System.out.println("Server Interrupted");

        players.stream().map(Player::getConnection).forEach(Thread::interrupt);
        socket.close();
        super.interrupt();
    }

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

    private Player findPlayerOfID(int id){
        return players.stream().filter(player ->
            id == player.getPlayerID()
        ).findFirst().get();
    }

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

    public int getCurrentPlayerID(){
        return players.get(currentPlayerIndex.currentValue()).getPlayerID();
    }

    // --- Player Triggered Events --- //

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
