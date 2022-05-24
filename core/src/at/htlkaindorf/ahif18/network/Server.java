package at.htlkaindorf.ahif18.network;

import at.htlkaindorf.ahif18.data.*;
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
 * Last changed: 2022-05-16
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
                cards.add(Card.values()[r.nextInt(Card.values().length)]);
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
    private int currentPlayer;
    private List<Player> players;
    private Sequence playerIDSequence;

    public Server()
    {
        players = new ArrayList<>();
        currentPlayer = -1;
        playerIDSequence = new Sequence();
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
        System.out.println("Server Interrupted");

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

    private Player findPlayerOfID(int id){
        return players.stream().filter(player ->
            id == player.getPlayerID()
        ).findFirst().get();
    }

    // --- Gameplay --- //

    public void playerJoined(int id, String name){
        Player newPlayer = findPlayerOfID(id);
        newPlayer.setName(name);

        List<PlayerInfo> playerInfos = players.stream()
                                    .filter((player) -> player.getPlayerID() != id)
                                    .map(Player::getPlayerInfo)
                                    .collect(Collectors.toList());

        newPlayer.getConnection().sendInitResponse(playerInfos, newPlayer.getCards());

        for(Player player : players){
            if(player.getPlayerID() == newPlayer.getPlayerID()){
                continue;
            }

            player.getConnection().playerJoined(newPlayer.getPlayerInfo());
        }
    }
}
