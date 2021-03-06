package at.htlkaindorf.ahif18.network;

import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.data.ConcurrentQueue;
import at.htlkaindorf.ahif18.data.PlayerInfo;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * Connection from Client to Server
 * Sending and Receiving are handled in seperate threads
 *
 * <br><br>
 * Last changed: 2022-06-16
 * @author Andreas Kurz; Jan Mandl
 */
public class Client extends Thread implements MessageConverter.ServerMessageListener {

    public static final String DEFAULT_IP = "127.0.0.1";

    private ConcurrentQueue<SendTask> sendTasks = new ConcurrentQueue<>();

    private String name;
    private String hostIP;

    private Thread receiveThread;
    private Socket serverConnection;
    private NetworkBuffer networkBuffer;

    @Getter
    private int playerID;

    /**
     * Creates a new Client
     * @param nwb NetworkBuffer that gets written to
     * @param name The Player Name of the client
     * @param hostIP the hostIP to connect to
     *               if omitted will use the Loopback interface
     */
    public Client(NetworkBuffer nwb, String name, String hostIP){
        networkBuffer = nwb;

        Thread.currentThread().setName(name);
        this.name = name;
        this.hostIP = hostIP == null ? DEFAULT_IP : hostIP;
    }

    @Override
    public void run() {
        try
        {
            serverConnection = new Socket(hostIP, Server.PORT);
            receiveThread = new Thread(this::receiveTask);
            receiveThread.start();

            MessageConverter.sendClientInit(serverConnection.getOutputStream(), name);

            while(!isInterrupted())
            {
                sendTasks.pop().execute();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts an infinite Receive Loop
     */
    public void receiveTask()
    {
        try
        {
            while(!Thread.currentThread().isInterrupted())
            {
                MessageConverter.receiveServerRequest(this, serverConnection.getInputStream());
            }
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    @SneakyThrows
    @Override
    public void interrupt() {
        //System.out.println("Client interrupted");

        if(serverConnection != null && serverConnection.isConnected()){
            serverConnection.close();
        }
        super.interrupt();
    }

    /**
     * Sends the card played message to the server
     * @param card Card to be played
     */
    public void playCard(Card card) {
        sendTasks.push(() -> {
            MessageConverter.sendClientCardPlayed(serverConnection.getOutputStream(), card);
        });
    }

    /**
     * Asks the Server to Draw a Card
     */
    public void drawCard(){
        sendTasks.push(() -> {
            MessageConverter.sendClientDrawCard(serverConnection.getOutputStream());
        });
    }

    /**
     * Sends the server the End Message
     */
    public void leave(){
        sendTasks.push(() -> {
            MessageConverter.sendClientEnd(serverConnection.getOutputStream());
        });
    }

    //----- Receive Methods -----//

    @Override
    public void receiveInit(int playerID, Card lastPlayedCard, List<Card> cards, List<PlayerInfo> otherPlayers) {
        this.playerID = playerID;

        networkBuffer.setCards(cards);
        networkBuffer.setLastPlayedCard(lastPlayedCard);
        networkBuffer.setPlayers(otherPlayers);
    }

    @Override
    public void receivePlayerJoined(PlayerInfo newPlayer) {
        networkBuffer.addPlayer(newPlayer);
        //System.out.println(message + " New Player Joined: " + newPlayer);
    }

    @Override
    public void receivePlayerLeft(PlayerInfo deadPlayer) {
        networkBuffer.removePlayer(deadPlayer);
    }

    @Override
    public void receiveCardPlayed(PlayerInfo player, Card card) {
        networkBuffer.setLastPlayedCard(card);
        networkBuffer.playerUpdate(player);

        if(this.playerID == player.getPlayerID())
        {
            //THATS ME!
            networkBuffer.removeCard(card);
        }
    }

    @Override
    public void receiveCardDrawn(Card card) {
        networkBuffer.addCard(card);
    }

    @Override
    public void receivePlayerUpdate(PlayerInfo playerInfo) {
        networkBuffer.playerUpdate(playerInfo);
    }

    @Override
    public void receiveCurrentPlayerUpdate(int currentPlayer) {
        networkBuffer.setCurrentPlayerID(currentPlayer);
    }
}