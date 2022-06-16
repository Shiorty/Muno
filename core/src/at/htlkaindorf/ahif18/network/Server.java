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
 * Last changed: 2022-06-03
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
            currentPlayerIndex = new LoopingSequence(
                    0,
                    currentPlayerIndex.getEnd() + 1,
                    currentPlayerIndex.currentValue() == -1 ? 1 : currentPlayerIndex.currentValue() + 1
            );

            ClientConnection connection = new ClientConnection(this, clientSocket, playerID);

            players.add(new Player(
                connection,
                playerID
            ));

            connection.start();
        }
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

    // --- Gameplay --- //

    public void playerJoined(int id, String name){
        Player newPlayer = findPlayerOfID(id);
        newPlayer.setName(name);

        List<PlayerInfo> playerInfos = players.stream()
                                    .filter((player) -> player.getPlayerID() != id)
                                    .map(Player::getPlayerInfo)
                                    .collect(Collectors.toList());

        newPlayer.getConnection().sendInitResponse(lastPlayedCard, playerInfos, newPlayer.getCards());

        for(Player player : players){
            if(player.getPlayerID() == newPlayer.getPlayerID()){
                continue;
            }

            player.getConnection().playerJoined(newPlayer.getPlayerInfo());
        }
    }

    public void cardPlayed(int id, Card cardPlayed) {

        int playerIndex = findIndexOfPlayer(id);

        if(playerIndex == -1)
        {
            //Player doesn't exist?
            return;
        }

        System.out.println(currentPlayerIndex);
        if(playerIndex != currentPlayerIndex.currentValue())
        {
            //Its not the players turn
            System.out.println("Server:cardPlayed -> Gegnerischer Zug");
            currentPlayerIndex.nextValue();
            return;
        } else {
            System.out.println("Server:cardPlayed -> Eigener Zug");
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

        Player currentPlayer = players.get(playerIndex);

        lastPlayedCard = cardPlayed;
        currentPlayer.cards.remove(lastPlayedCard);

        players.forEach(player -> {
           player.getConnection().sendCardPlayed(currentPlayer.getPlayerInfo(), cardPlayed);
        });
        currentPlayerIndex.nextValue();
    }

    public void drawCard(int playerID) {

        Player currentPlayer = findPlayerOfID(playerID);

//        if(player.getPlayerID() != players.get(currentPlayerIndex).getPlayerID())
//        {
//            return;
//        }

        Card newCard = Card.randomCard();

        currentPlayer.cards.add(newCard);
        currentPlayer.connection.sendDrawnCard(newCard);

        for(Player player : players)
        {
            if(player.getPlayerID() == currentPlayer.getPlayerID()){
                continue;
            }

            //TODO implement current player stuff
            player.connection.sendPlayerUpdate(currentPlayer.getPlayerInfo(), currentPlayerIndex.currentValue());
        }
    }
}
